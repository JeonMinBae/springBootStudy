package com.hardy.study.user.exception;

import com.hardy.study.common.enums.ErrorCodeAndMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCodeAndMessage errorCodeAndMessage;


    public CustomException(ErrorCodeAndMessage errorCodeAndMessage) {
        super(errorCodeAndMessage.getMessage());
        this.errorCodeAndMessage = errorCodeAndMessage;
    }
}
