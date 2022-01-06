package com.hardy.study.auth;

import com.hardy.study.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private UserEntity userEntity;
    public PrincipalDetails(UserEntity userEntity){
        this.userEntity = userEntity;
    }

    public UserEntity getUser(){ return userEntity; }

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
}
