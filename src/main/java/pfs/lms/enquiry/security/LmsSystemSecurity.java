package pfs.lms.enquiry.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

/**
 * Authentication used for startup {@link org.springframework.boot.CommandLineRunner}s, scheduled jobs, and other
 * in-process work that is not tied to a signed-in user. Never assigned to HTTP sessions.
 */
public final class LmsSystemSecurity {

    public static final String INTERNAL_AUTHORITY = "LmsSystem.Internal";

    /**
     * Appended to repository {@code @PreAuthorize} SpEL so internal callers can access data.
     */
    public static final String OR_INTERNAL = " or hasAuthority('" + INTERNAL_AUTHORITY + "')";

    private static final Authentication SYSTEM_AUTHENTICATION = new UsernamePasswordAuthenticationToken(
            "lms-system", "n/a", Collections.singletonList(new SimpleGrantedAuthority(INTERNAL_AUTHORITY)));

    private LmsSystemSecurity() {
    }

    public static void run(Runnable action) {
        SecurityContext previous = SecurityContextHolder.getContext();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(SYSTEM_AUTHENTICATION);
        try {
            SecurityContextHolder.setContext(context);
            action.run();
        } finally {
            SecurityContextHolder.setContext(previous);
        }
    }

    public static void runThrowing(LmsSystemCallback action) throws Exception {
        SecurityContext previous = SecurityContextHolder.getContext();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(SYSTEM_AUTHENTICATION);
        try {
            SecurityContextHolder.setContext(context);
            action.run();
        } finally {
            SecurityContextHolder.setContext(previous);
        }
    }

    @FunctionalInterface
    public interface LmsSystemCallback {
        void run() throws Exception;
    }
}