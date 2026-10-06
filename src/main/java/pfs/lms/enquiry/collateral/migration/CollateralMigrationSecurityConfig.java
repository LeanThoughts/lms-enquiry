package pfs.lms.enquiry.collateral.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Opens the collateral migration API (/api/collaterals/migration/**) without sign-in, so that the SAP ABAP program
 * can call it. It is a separate security chain checked before the portal's own security configuration, which is
 * not changed; every other path keeps requiring sign-in.
 * <p>
 * Active while {@code collateral.migration.enabled} is true (default). Set it to false after the migration.
 */
@Configuration
@Order(1)
@ConditionalOnProperty(name = "collateral.migration.enabled", havingValue = "true", matchIfMissing = true)
public class CollateralMigrationSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(CollateralMigrationSecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.warn("Collateral migration API /api/collaterals/migration/** is open without sign-in "
                + "(collateral.migration.enabled=true). Switch it off after the migration.");
        http
                .requestMatchers().antMatchers("/api/collaterals/migration/**")
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().anyRequest().permitAll();
    }
}
