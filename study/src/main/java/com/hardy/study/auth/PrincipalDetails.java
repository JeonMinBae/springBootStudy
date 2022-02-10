package com.hardy.study.auth;

import com.hardy.study.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PrincipalDetails implements UserDetails, OAuth2User {

    private UserEntity userEntity;
    private Map<String, Object> attributes;


    public PrincipalDetails(UserEntity userEntity){
        this.userEntity = userEntity;
    }

    public PrincipalDetails(UserEntity userEntity, Map<String, Object> attributes){
        this.userEntity = userEntity;
        this.attributes = attributes;
    }

    public UserEntity getUser(){ return userEntity; }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(userEntity.getUserRole());
            }
        };
        authorities.add(grantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return userEntity.getUserPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
//        return user.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return user.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return user.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return user.isEnabled();
        return true;
    }

    @Override
    public String getName() {
        return userEntity.getUserId()+"";
    }
}
