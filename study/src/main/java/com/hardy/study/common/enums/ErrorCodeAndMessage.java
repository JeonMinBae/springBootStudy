package com.hardy.study.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCodeAndMessage {
    TEST_EXCEPTION(HttpStatus.LOCKED, "Test Lock"),


    USER_NOT_FOUNT(HttpStatus.NOT_FOUND, "User Not Found"),
    ALREADY_EXIST_USER_ID(HttpStatus.CONFLICT, "Already Exist User Id"),
    ALREADY_EXIST_USER_NICKNAME(HttpStatus.CONFLICT, "Already Exist User Nickname"),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "Full authentication is required to access this resource"),

    //login
    LOGIN_FAILED(HttpStatus.NO_CONTENT, "Login Failed"),

    //token
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Invalid JWT token"),
    TOKEN_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "Invalid JWT signature"),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "Unsupported JWT token"),
    TOKEN_CLAIMS_EMPTY(HttpStatus.UNAUTHORIZED, "JWT claims string is empty"),

    //oauth2
    GOOGLE_OAUTH2_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Google OAuth2 Access Token Invalid"),

    ;


    private final HttpStatus httpStatus;
    private final String message;
}
