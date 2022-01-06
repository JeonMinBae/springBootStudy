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

    LOGIN_FAILED(HttpStatus.NO_CONTENT, "Login Failed"),
    ;


    private final HttpStatus httpStatus;
    private final String message;
}
