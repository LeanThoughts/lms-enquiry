//package pfs.lms.enquiry.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Order(1)
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
//
//
//        /*@Override
//        public void configure(HttpSecurity http) throws Exception {
//            http
//                    .antMatcher("/api/**").authorizeRequests()
//                    .and().authorizeRequests().antMatchers("/api/signup","/api/admin/signup","/api/signup/verify/**").permitAll()
//                    .anyRequest().authenticated();
//        }*/
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http
//
//
//
//                .requestMatcher(new OAuthRequestedMatcher())
//                .authorizeRequests().anyRequest().fullyAuthenticated()
//
//
//                .and().authorizeRequests().antMatchers("/api/password/strength","/api/signup","/api/signup/verify/**", "/api/password/reset", "/signup","/assests","/assets/**","/assets/**/**","/runtime.js","/polyfills.js","/styles.js","/vendor.js","/main.js","/style.css","/favicon.ico","/*.js","/*.css").permitAll()
//                .anyRequest().authenticated();
//
//    }
//
//
//    private static class OAuthRequestedMatcher implements RequestMatcher {
//        public boolean matches(HttpServletRequest request) {
//            String auth = request.getHeader("Authorization");
//            boolean haveOauth2Token = (auth != null) && auth.startsWith("Bearer");
//            boolean haveAccessToken = request.getParameter("access_token")!=null;
//            return haveOauth2Token || haveAccessToken;
//        }
//    }
//}