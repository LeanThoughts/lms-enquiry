package pfs.lms.enquiry.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.http.HttpStatus.FORBIDDEN;

class GraphGroupMembershipLookupTest {

    private static final String TENANT = "b94a5e24-22c4-4af5-82eb-554731d35dbc";
    private static final String GROUP = "f2a82b28-d18a-4387-ae80-6de7adfe0caa";
    private static final String TOKEN_URL =
            "https://login.microsoftonline.com/" + TENANT + "/oauth2/v2.0/token";
    private static final String GRAPH_URL =
            "https://graph.microsoft.com/v1.0/groups/" + GROUP
                    + "/transitiveMemberOf/microsoft.graph.group?$select=id";

    private RestTemplate restTemplate;
    private MockRestServiceServer server;
    private GraphGroupMembershipLookup lookup;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        lookup = new GraphGroupMembershipLookup(
                new ObjectMapper(), restTemplate, true, TENANT, "client", "secret");
    }

    @Test
    void readsMembershipGroupIdsFromGraph() {
        server.expect(requestTo(TOKEN_URL))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"access_token\":\"tok\",\"expires_in\":3600}", MediaType.APPLICATION_JSON));
        server.expect(requestTo(GRAPH_URL))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer tok"))
                .andRespond(withSuccess("{\"value\":["
                        + "{\"id\":\"bc44367a-9b1d-4e88-aabf-7b2d32641bb7\"},"
                        + "{\"id\":\"03ef1b62-ccaa-4573-a261-fc00c231798c\"}"
                        + "]}", MediaType.APPLICATION_JSON));

        assertThat(lookup.memberGroupIds(List.of(GROUP)))
                .containsExactly(
                        "bc44367a-9b1d-4e88-aabf-7b2d32641bb7",
                        "03ef1b62-ccaa-4573-a261-fc00c231798c");
        server.verify();
    }

    @Test
    void followsTheNextPage() {
        server.expect(requestTo(TOKEN_URL))
                .andRespond(withSuccess("{\"access_token\":\"tok\",\"expires_in\":3600}", MediaType.APPLICATION_JSON));
        String next = "https://graph.microsoft.com/v1.0/groups/" + GROUP + "/transitiveMemberOf/next";
        server.expect(requestTo(GRAPH_URL))
                .andRespond(withSuccess("{\"value\":[{\"id\":\"bc44367a-9b1d-4e88-aabf-7b2d32641bb7\"}],"
                        + "\"@odata.nextLink\":\"" + next + "\"}", MediaType.APPLICATION_JSON));
        server.expect(requestTo(next))
                .andRespond(withSuccess(
                        "{\"value\":[{\"id\":\"03ef1b62-ccaa-4573-a261-fc00c231798c\"}]}",
                        MediaType.APPLICATION_JSON));

        assertThat(lookup.memberGroupIds(List.of(GROUP)))
                .containsExactly(
                        "bc44367a-9b1d-4e88-aabf-7b2d32641bb7",
                        "03ef1b62-ccaa-4573-a261-fc00c231798c");
        server.verify();
    }

    @Test
    void returnsNoIdsWhenGraphRefuses() {
        server.expect(requestTo(TOKEN_URL))
                .andRespond(withSuccess("{\"access_token\":\"tok\",\"expires_in\":3600}", MediaType.APPLICATION_JSON));
        server.expect(requestTo(GRAPH_URL))
                .andRespond(withStatus(FORBIDDEN));

        assertThat(lookup.memberGroupIds(List.of(GROUP))).isEmpty();
        server.verify();
    }

    @Test
    void doesNotCallGraphWhenTurnedOff() {
        GraphGroupMembershipLookup disabled = new GraphGroupMembershipLookup(
                new ObjectMapper(), restTemplate, false, TENANT, "client", "secret");

        assertThat(disabled.memberGroupIds(List.of(GROUP))).isEmpty();
        server.verify();
    }
}
