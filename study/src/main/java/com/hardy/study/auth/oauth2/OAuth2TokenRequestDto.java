package com.hardy.study.auth.oauth2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2TokenRequestDto {
    private String signInType;
    private String provider;
    private String providerId;
    private Map<String, Object> profileObj;
    private Map<String, Object> tokenObj;

}
