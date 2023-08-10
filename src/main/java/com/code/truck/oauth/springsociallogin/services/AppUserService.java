package com.code.truck.oauth.springsociallogin.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.code.truck.oauth.springsociallogin.emuns.LoginProvider;
import com.code.truck.oauth.springsociallogin.entities.AppUser;

import jakarta.annotation.PostConstruct;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

@Service
@Value
@Log4j2
public class AppUserService implements UserDetailsService {

    PasswordEncoder passwordEncoder;

    Map<String, AppUser> users = new HashMap<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.get(username);
    }

    public OAuth2UserService<OidcUserRequest, OidcUser> oidcLoginHandler() {
        return userRequest -> {
            LoginProvider provider = LoginProvider
                    .valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
            OidcUserService delegate = new OidcUserService();
            OidcUser oidcUser = delegate.loadUser(userRequest);
            log.info("oidcUser : {}", oidcUser);
            return AppUser
                    .builder()
                    .provider(provider)
                    .username(oidcUser.getEmail())
                    .name(oidcUser.getFullName())
                    .email(oidcUser.getEmail())
                    .userId(oidcUser.getName())
                    .imageUrl(oidcUser.getAttribute("picture"))
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .attributes(oidcUser.getAttributes())
                    .authorities(oidcUser.getAuthorities())
                    .build();
        };
    }

    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2LoginHandler() {
        return userRequest -> {
            LoginProvider provider = LoginProvider
                    .valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
            DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
            OAuth2User oauth2User = delegate.loadUser(userRequest);
            log.info("oauth2User : {}", oauth2User);
            return AppUser
                    .builder()
                    .provider(provider)
                    .username(oauth2User.getAttribute("login"))
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .name(oauth2User.getAttribute("login"))
                    .userId(oauth2User.getName())
                    .imageUrl(oauth2User.getAttribute("avatar_url"))
                    .attributes(oauth2User.getAttributes())
                    .authorities(oauth2User.getAuthorities())
                    .build();
        };
    }

    @PostConstruct
    private void createHardcodedUsers() {

        var bob = AppUser
                .builder()
                .provider(LoginProvider.APP)
                .username("bob")
                .password(passwordEncoder.encode("1234"))
                .authorities(List.of(new SimpleGrantedAuthority("read")))
                .build();
        var bil = AppUser
                .builder()
                .provider(LoginProvider.APP)
                .username("bil")
                .password(passwordEncoder.encode("321"))
                .authorities(List.of(new SimpleGrantedAuthority("read")))
                .build();

        createUser(bob);
        createUser(bil);
    }

    private void createUser(AppUser user) {
        users.putIfAbsent(user.getUsername(), user);
    }
}
