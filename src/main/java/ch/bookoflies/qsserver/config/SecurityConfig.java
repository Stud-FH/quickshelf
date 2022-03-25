package ch.bookoflies.qsserver.config;

import ch.bookoflies.qsserver.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.security.Principal;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // localhost:8080/oauth2/authorization/google

        http.authorizeRequests()
                .antMatchers("/", "/login", "/register", "/oauth/**", "/h2/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();
        http.oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(authenticationService)
                .and()
                .successHandler((request, response, authentication) -> {
                    authenticationService.processOAuthPostLogin(request.getUserPrincipal());

                    response.sendRedirect("/profile");
                });

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Autowired
    private AuthenticationService authenticationService;
}
