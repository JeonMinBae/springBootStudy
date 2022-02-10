package com.hardy.study.auth.dto;

import com.hardy.study.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponseDto {

    private String userId;
    private String userName;
    private String userNickName;
    private boolean userMarketingAgreement;
    private Role userRole;
}
