package com.hardy.study.auth.oauth2;

public interface IOAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
