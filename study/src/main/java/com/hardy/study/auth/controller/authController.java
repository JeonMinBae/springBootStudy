package com.hardy.study.auth.controller;

import com.hardy.study.common.enums.ErrorCodeAndMessage;
import com.hardy.study.user.exception.CustomException;
import com.hardy.study.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class authController {

    private final UserService userService;

//    JwtAuthenticationFilter에서 처리
//    @PostMapping("/sign-in")
//    public ResponseEntity<String> signIn(@Valid @RequestBody SignInDto signInDto){
//        UserEntity userEntity = userService.getUserById(signInDto.getUserId());
//        userService.passwordCheck(userEntity, signInDto.getUserPassword());
//
//        System.out.println("loign success");
//        return ResponseEntity.ok("login");
//    }


    @GetMapping("")
    public ResponseEntity<String> test() {
//        throw new CustomException(ErrorCodeAndMessage.TEST_EXCEPTION);

        return ResponseEntity.ok("test");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp() {

        return ResponseEntity.ok("sign up");
    }

    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut(){
        return ResponseEntity.ok("logout");
    }

}
