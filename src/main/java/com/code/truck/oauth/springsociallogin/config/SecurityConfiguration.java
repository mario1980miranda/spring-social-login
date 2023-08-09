package com.code.truck.oauth.springsociallogin.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(Customizer.withDefaults())
                .oauth2Login(oc -> oc.userInfoEndpoint(
                        ui -> ui.userService(oauth2LoginHandler()).oidcUserService(oidcLoginHandler())))
                .authorizeHttpRequests(c -> c.anyRequest().authenticated())
                .build();
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcLoginHandler() {
        return userRequest -> {
            OidcUserService delegate = new OidcUserService();
            // custom AppUser
            return delegate.loadUser(userRequest);
        };
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2LoginHandler() {
        return userRequest -> {
            DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
            return delegate.loadUser(userRequest);
        };
    }

    /*
     * @Bean
     * UserDetailsService inMemoryUsers() {
     * InMemoryUserDetailsManager users = new InMemoryUserDetailsManager();
     * 
     * var bob = new User("bob", passwordEncoder().encode("1234"),
     * Collections.emptyList());
     * var bil =
     * User.builder().username("bil").password(passwordEncoder().encode("321")).
     * roles("USER")
     * .authorities("read").build();
     * 
     * users.createUser(bob);
     * users.createUser(bil);
     * 
     * return users;
     * }
     */

    @Bean
    ApplicationListener<AuthenticationSuccessEvent> successLogger() {
        return event -> {
            log.info("success: {}", event.getAuthentication());
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
