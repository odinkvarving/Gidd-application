package ntnu.idatt2106.group8.gidd.config;

import ntnu.idatt2106.group8.gidd.filter.JwtFilter;
import ntnu.idatt2106.group8.gidd.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * A configuration class that is used to add Spring Security to the application.
 * Disallows all HTTP requests except registering a user request and login request
 * unless the HTTP request includes a Authorization key with a valid JWT token. How
 * the JWT token is validated can be seen in the JwtUtil.java file. The filter checks
 * if the HTTP request contains the "Bearer" namespace and uses this to find out
 * whether the Authorization value is a token or not. If it is, it extracts the token
 * and validates it
 *
 * @author Magnus Bredeli
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //All urls that can be reached with http requests from outside the server.
    private static final String[] AUTHORIZED_URLS = {
            "/accounts/register",
            "/accounts/login",
            "/h2-console/*",
            "/activities/",
            "/activityTypes/",
            "/levels/",
            "/activities/{id}/accounts/count",
            "/activities/{id}/accounts/queue/count",
            "/reset/{mail}",
            "/activities/{id}/messages",
            "/accounts/{id}/info",
            "/activities/{id}/notify-edit",
            "/levels/",
            "/activityTypes/",
            "/activities/{id}/",
            "/accounts/notifications/{notificationId}",
            "/reset/{mail}",
            "/activities/{id}/messages",
            "/accounts/{id}/info",
            "/messages/{id}",
            "/accounts/{id}/profilepicture",
            "/profilepictures/{filename}",

            "/profilepictures/",
            "/profilepictures",

            // Documentation
            "/gidd-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();

        http.headers()
            .frameOptions()
            .disable();

        http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers(AUTHORIZED_URLS)
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .exceptionHandling()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
