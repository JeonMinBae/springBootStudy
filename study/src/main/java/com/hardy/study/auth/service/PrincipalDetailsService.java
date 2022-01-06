package com.hardy.study.auth.service;

import com.hardy.study.auth.PrincipalDetails;
import com.hardy.study.user.entity.UserEntity;
import com.hardy.study.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

   private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("------------------------------------------------------------------");
        System.out.println("loadUserByUsername");
        System.out.println("username " + username);
        UserEntity userEntity = userRepository.findByUserId(username)
            .orElseThrow(() -> new UsernameNotFoundException("UserName Not Found"));

        return new PrincipalDetails(userEntity);
//        User.UserBuilder builder = User.builder();
//        builder
//            .username(userEntity.getUserName())
//            .password(userEntity.getUserPassword())
//            .accountExpired(false)
//            .authorities(userEntity.getUserRole().toString())
//            .credentialsExpired(false)
//            .disabled(false)
//            .accountLocked(false)
//            .credentialsExpired(false)
//            .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
//        ;

    }
}
