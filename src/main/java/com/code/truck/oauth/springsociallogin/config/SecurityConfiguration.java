package com.code.truck.oauth.springsociallogin.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.code.truck.oauth.springsociallogin.services.AppUserService;

import lombok.extern.log4j.Log4j2;

@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfiguration {

    private static final RequestMatcher PROTECTECTED_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/"),
            new AntPathRequestMatcher("/login"),
            new AntPathRequestMatcher("/user/signup"),
            new AntPathRequestMatcher("/error"));

    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/images/**", "/**.css", "/**.js")
                .authorizeHttpRequests(c -> c.anyRequest().permitAll())
                .securityContext(c -> c.disable())
                .sessionManagement(c -> c.disable())
                .requestCache(c -> c.disable())
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, /* MvcRequestMatcher.Builder mvc, */
            AppUserService appUserService) throws Exception {
        return http
                .formLogin(c -> c
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticate")
                        .usernameParameter("user")
                        .passwordParameter("pass")
                        .defaultSuccessUrl("/user"))
                .requestCache(c -> c.disable())
                .logout(c -> c.logoutSuccessUrl("/?logout"))
                .oauth2Login(oc -> oc
                        .loginPage("/login")
                        .defaultSuccessUrl("/user")
                        .userInfoEndpoint(ui -> ui
                                .userService(appUserService.oauth2LoginHandler())
                                .oidcUserService(appUserService.oidcLoginHandler())))
                .authorizeHttpRequests(c -> c
                        .requestMatchers(PROTECTECTED_URLS
                        /*
                         * mvc.pattern("/"),
                         * mvc.pattern("/login"),
                         * mvc.pattern("/user/signup"),
                         * mvc.pattern("/error")
                         */
                        )
                        .permitAll()
                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> successLogger() {
        return event -> {
            log.info("success: {}", event.getAuthentication());
        };
    }

    @Scope("prototype")
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
