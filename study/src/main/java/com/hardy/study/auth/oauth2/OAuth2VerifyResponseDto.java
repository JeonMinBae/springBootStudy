package com.hardy.study.auth.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2VerifyResponseDto {
    private String issued_to;
    private String audience;
    private String user_id;
    private String scope;
    private String expires_in;
    private String email;
    private String verified_email;
    private String access_type;
}
