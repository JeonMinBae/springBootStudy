package com.hardy.study.user.dto;

import com.hardy.study.common.enums.Role;
import com.sun.istack.NotNull;
import com.hardy.study.common.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank
    private String userId;

    @NotBlank
    private String userPassword;

    @NotBlank
    private String userName;

    @NotBlank
    private String userNickName;

    @NotBlank
    @Email
    private String userEmail;

    //폰번호
    @NotBlank
    private String userPhoneNumber;

    //전화번호
    private String userTelephone;

    @NotBlank
    private String userAddress;

    @NotBlank
    private  String userZipcode;

    private  String userAddressDetail;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Role userRole = Role.ROLE_CONSUMER;

    @NotNull
    private boolean userTermsAgreement;

//    @NotNull
    private boolean userMarketingAgreement;
}
