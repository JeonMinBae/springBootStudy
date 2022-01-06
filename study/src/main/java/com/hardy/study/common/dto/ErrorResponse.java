package com.hardy.study.common.dto;

import com.hardy.study.common.enums.ErrorCodeAndMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> createResponseEntityBy(ErrorCodeAndMessage errorCodeAndMessage){
        return ResponseEntity
                .status(errorCodeAndMessage.getHttpStatus())
                .body(
                    ErrorResponse
                        .builder()
                        .status(errorCodeAndMessage.getHttpStatus().value())
                        .error(errorCodeAndMessage.getHttpStatus().name())
                        .code(errorCodeAndMessage.name())
                        .message(errorCodeAndMessage.getMessage())
                        .build()
                );
    }
}
