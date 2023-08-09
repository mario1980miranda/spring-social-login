package com.code.truck.oauth.springsociallogin.entities;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.code.truck.oauth.springsociallogin.emuns.LoginProvider;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUser implements UserDetails, OidcUser {

    String username;
    String password;
    String email;
    String userId;
    String name;
    String imageUrl;

    LoginProvider provider;

    Map<String, Object> attributes;
    Collection<? extends GrantedAuthority> authorities;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return Objects.nonNull(name) ? name : username;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
