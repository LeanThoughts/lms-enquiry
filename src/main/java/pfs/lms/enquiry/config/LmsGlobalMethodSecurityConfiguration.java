package pfs.lms.enquiry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Enables {@link org.springframework.security.access.prepost.PreAuthorize} on controllers and repositories.
 * Startup and scheduled jobs run under {@link pfs.lms.enquiry.security.LmsSystemSecurity}.
 * Named explicitly to avoid clashing with Activiti's {@code org.activiti.spring.boot.MethodSecurityConfig}.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class LmsGlobalMethodSecurityConfiguration {
}