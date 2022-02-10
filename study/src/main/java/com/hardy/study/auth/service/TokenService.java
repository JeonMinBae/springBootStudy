package com.hardy.study.auth.service;

import com.hardy.study.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;



    public String createToken(Authentication authentication){


        return jwtTokenProvider.generateToken(authentication);

    }

}
