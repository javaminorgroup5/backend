package nl.hro.cookbook.config;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.security.Role;
import nl.hro.cookbook.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USER_ID_PATH = "/users/{id}/**";
    private static final String ADMIN_PATH = "/admin/**";
    private static final String H2_CONSOLE_PATH = "/h2-console/**";
    private static final String CREATE_USER_PATH = "/users/create";
    private static final String IMAGE_PATH = "/image/**";

    private final UserDetailService userDetailService;

    @Bean
    protected AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setUserDetailsService(userDetailService);
        return authProvider;
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        assert auth != null;
        auth.authenticationProvider(authenticationProvider(passwordEncoder()));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();
        http
            .csrf()
                .disable()
            .cors()
            .and()
            .authorizeRequests()
                .antMatchers(CREATE_USER_PATH).permitAll()
                .antMatchers(H2_CONSOLE_PATH).permitAll()
                .antMatchers(IMAGE_PATH).permitAll()
                .antMatchers(ADMIN_PATH).hasRole(Role.ADMIN.name())
//                .antMatchers(HttpMethod.GET, USER_ID_PATH).access("@guard.checkReadAccess(authentication,#id)")
//                .antMatchers(USER_ID_PATH).access("@guard.checkWriteAccess(authentication,#id)")
                .anyRequest().authenticated()
            .and()
                .httpBasic()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}