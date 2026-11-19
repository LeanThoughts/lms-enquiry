package pfs.lms.enquiry.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * Reads Entra group memberships with the application registration already used for sign-in.
 * The registration needs the GroupMember.Read.All application permission and admin consent.
 */
@Component
@Slf4j
public class GraphGroupMembershipLookup implements GroupMembershipLookup {

    private static final Pattern GUID = Pattern.compile(
            "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
    private static final long SUCCESS_TTL_MS = 10 * 60 * 1000L;
    private static final long FAILURE_TTL_MS = 60 * 1000L;
    private static final int MAX_GROUPS = 25;
    private static final int MAX_PAGES = 10;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final boolean enabled;
    private final String tenantId;
    private final String clientId;
    private final String clientSecret;
    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final AtomicBoolean configurationLogged = new AtomicBoolean();

    private volatile String accessToken;
    private volatile long accessTokenExpiresAt;

    @Autowired
    public GraphGroupMembershipLookup(
            ObjectMapper objectMapper,
            @Value("${app.entra.group-memberships.enabled:true}") boolean enabled,
            @Value("${spring.cloud.azure.active-directory.tenant-id:}") String tenantId,
            @Value("${spring.security.oauth2.client.registration.azure.client-id:}") String clientId,
            @Value("${spring.security.oauth2.client.registration.azure.client-secret:}") String clientSecret) {
        this(objectMapper, defaultRestTemplate(), enabled, tenantId, clientId, clientSecret);
    }

    GraphGroupMembershipLookup(ObjectMapper objectMapper, RestTemplate restTemplate, boolean enabled,
                               String tenantId, String clientId, String clientSecret) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.enabled = enabled;
        this.tenantId = tenantId == null ? "" : tenantId.trim();
        this.clientId = clientId == null ? "" : clientId.trim();
        this.clientSecret = clientSecret == null ? "" : clientSecret.trim();
    }

    @Override
    public Collection<String> memberGroupIds(Collection<String> groupIds) {
        if (!configured() || groupIds == null || groupIds.isEmpty()) {
            return List.of();
        }
        String token = accessToken();
        if (token == null) {
            return List.of();
        }
        LinkedHashSet<String> found = new LinkedHashSet<>();
        int queried = 0;
        for (String groupId : groupIds) {
            if (groupId == null || !GUID.matcher(groupId.trim()).matches()) {
                continue;
            }
            if (queried >= MAX_GROUPS) {
                log.warn("Entra group membership lookup stopped after {} groups", MAX_GROUPS);
                break;
            }
            queried++;
            found.addAll(membershipsOf(groupId.trim(), token));
        }
        return found;
    }

    @Override
    public Optional<Collection<String>> groupsOfUser(String userObjectId) {
        if (!configured() || userObjectId == null || !GUID.matcher(userObjectId.trim()).matches()) {
            return Optional.empty();
        }
        String token = accessToken();
        if (token == null) {
            return Optional.empty();
        }
        String userId = userObjectId.trim();
        String key = "user:" + userId.toLowerCase(Locale.ROOT);
        long now = System.currentTimeMillis();
        CacheEntry cached = cache.get(key);
        if (cached != null && now < cached.expiresAt) {
            return Optional.of(cached.ids);
        }
        String url = "https://graph.microsoft.com/v1.0/users/" + userId
                + "/transitiveMemberOf/microsoft.graph.group?$select=id";
        try {
            List<String> ids = readPages(url, token);
            cache.put(key, new CacheEntry(ids, now + SUCCESS_TTL_MS));
            log.info("Entra user {} belongs to {}", userId, ids);
            return Optional.of(ids);
        } catch (RestClientResponseException ex) {
            log.warn("Entra user group lookup failed for {}: HTTP {}", userId, ex.getRawStatusCode());
            return Optional.empty();
        } catch (RestClientException ex) {
            log.warn("Entra user group lookup failed for {}: {}", userId, ex.getMessage());
            return Optional.empty();
        }
    }

    private boolean configured() {
        if (enabled && !tenantId.isEmpty() && !clientId.isEmpty() && !clientSecret.isEmpty()) {
            return true;
        }
        if (configurationLogged.compareAndSet(false, true)) {
            log.info("Entra group membership lookup is off");
        }
        return false;
    }

    private List<String> membershipsOf(String groupId, String token) {
        String key = groupId.toLowerCase(Locale.ROOT);
        long now = System.currentTimeMillis();
        CacheEntry cached = cache.get(key);
        if (cached != null && now < cached.expiresAt) {
            return cached.ids;
        }
        try {
            List<String> ids = readMemberships(groupId, token);
            cache.put(key, new CacheEntry(ids, now + SUCCESS_TTL_MS));
            log.info("Entra group {} belongs to {}", groupId, ids);
            return ids;
        } catch (RestClientResponseException ex) {
            log.warn("Entra group membership lookup failed for {}: HTTP {}", groupId, ex.getRawStatusCode());
            cache.put(key, new CacheEntry(List.of(), now + FAILURE_TTL_MS));
            return List.of();
        } catch (RestClientException ex) {
            log.warn("Entra group membership lookup failed for {}: {}", groupId, ex.getMessage());
            cache.put(key, new CacheEntry(List.of(), now + FAILURE_TTL_MS));
            return List.of();
        }
    }

    private List<String> readMemberships(String groupId, String token) {
        String url = "https://graph.microsoft.com/v1.0/groups/" + groupId
                + "/transitiveMemberOf/microsoft.graph.group?$select=id";
        return readPages(url, token);
    }

    private List<String> readPages(String url, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        List<String> ids = new ArrayList<>();
        for (int page = 0; page < MAX_PAGES && url != null; page++) {
            if (!url.startsWith("https://graph.microsoft.com/")) {
                break;
            }
            String body = restTemplate.exchange(
                    URI.create(url), HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
            JsonNode root;
            try {
                root = objectMapper.readTree(body == null ? "{}" : body);
            } catch (Exception ex) {
                throw new RestClientException("Entra group membership response was not JSON", ex);
            }
            JsonNode value = root.get("value");
            if (value != null && value.isArray()) {
                for (JsonNode item : value) {
                    String id = item.path("id").asText("");
                    if (!id.isEmpty()) {
                        ids.add(id);
                    }
                }
            }
            String next = root.path("@odata.nextLink").asText("");
            url = next.startsWith("https://graph.microsoft.com/") ? next : null;
        }
        return List.copyOf(ids);
    }

    private String accessToken() {
        long now = System.currentTimeMillis();
        String current = this.accessToken;
        if (current != null && now < this.accessTokenExpiresAt) {
            return current;
        }
        synchronized (this) {
            now = System.currentTimeMillis();
            if (this.accessToken != null && now < this.accessTokenExpiresAt) {
                return this.accessToken;
            }
            Token token = requestToken();
            if (token == null) {
                return null;
            }
            long lifetimeMs = Math.max(30_000L, (token.expiresInSeconds - 60) * 1000L);
            this.accessToken = token.value;
            this.accessTokenExpiresAt = now + lifetimeMs;
            return this.accessToken;
        }
    }

    private Token requestToken() {
        String url = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("scope", "https://graph.microsoft.com/.default");
        form.add("grant_type", "client_credentials");
        try {
            String body = restTemplate.exchange(
                    URI.create(url), HttpMethod.POST, new HttpEntity<>(form, headers), String.class).getBody();
            JsonNode root = objectMapper.readTree(body == null ? "{}" : body);
            String value = root.path("access_token").asText("");
            if (value.isEmpty()) {
                log.warn("Entra token response had no access token");
                return null;
            }
            return new Token(value, root.path("expires_in").asLong(3600L));
        } catch (RestClientResponseException ex) {
            log.warn("Entra client-credentials token request failed: HTTP {}", ex.getRawStatusCode());
            return null;
        } catch (Exception ex) {
            log.warn("Entra client-credentials token request failed: {}", ex.getMessage());
            return null;
        }
    }

    private static RestTemplate defaultRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        return new RestTemplate(factory);
    }

    private static final class CacheEntry {
        private final List<String> ids;
        private final long expiresAt;

        private CacheEntry(List<String> ids, long expiresAt) {
            this.ids = ids;
            this.expiresAt = expiresAt;
        }
    }

    private static final class Token {
        private final String value;
        private final long expiresInSeconds;

        private Token(String value, long expiresInSeconds) {
            this.value = value;
            this.expiresInSeconds = expiresInSeconds;
        }
    }
}
