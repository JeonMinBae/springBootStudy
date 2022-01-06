//package com.hardy.study.user.exception;
//
//import com.hardy.study.common.dto.ErrorResponse;
//import com.hardy.study.common.enums.HttpErrorCode;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ControllerAdvice
//public class UserExceptionAdvice {
//    @ResponseBody
//    @ResponseStatus(HttpStatus.CONFLICT)
//    @ExceptionHandler(UserAlreadyExistException.class)
//    public ErrorResponse handleUserAlreadyExistException(Exception e){
//        return ErrorResponse.builder()
//            .httpErrorCode(HttpErrorCode.ALREADY_EXISTS)
//            .message(e.getMessage())
//            .build();
//    }
//
//    @ResponseBody
//    @ResponseStatus(HttpStatus.CONFLICT)
//    @ExceptionHandler(UserNickNameAlreadyExistException.class)
//    public ErrorResponse handleUserNickNameAlreadyExistException(Exception e){
//        return ErrorResponse.builder()
//            .httpErrorCode(HttpErrorCode.ALREADY_EXISTS)
//            .message(e.getMessage())
//            .build();
//    }
//
//    @ResponseBody
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @ExceptionHandler(UserNotFoundException.class)
//    public ErrorResponse handleUserNotFoundException(Exception e){
//        return ErrorResponse.builder()
//            .httpErrorCode(HttpErrorCode.NO_CONTENT)
//            .message(e.getMessage())
//            .build();
//    }
//
//}
