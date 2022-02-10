package com.hardy.study.auth.jwt;

import com.hardy.study.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class JwtToken {

    private String token;
    private String userId;
    private Role role;
    private Date ExpireDate;

}
