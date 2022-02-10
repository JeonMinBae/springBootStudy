package com.hardy.study.auth.controller;

import com.hardy.study.auth.PrincipalDetails;
import com.hardy.study.auth.dto.SignInDto;
import com.hardy.study.auth.dto.SignInResponseDto;
import com.hardy.study.auth.dto.SignUpDto;
import com.hardy.study.auth.oauth2.OAuth2TokenRequestDto;
import com.hardy.study.auth.oauth2.OAuth2VerifyResponseDto;
import com.hardy.study.auth.service.AuthService;
import com.hardy.study.auth.service.TokenService;
import com.hardy.study.common.enums.ErrorCodeAndMessage;
import com.hardy.study.user.entity.UserEntity;
import com.hardy.study.user.exception.CustomException;
import com.hardy.study.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final TokenService tokenService;

    //    JwtAuthenticationFilter에서 처리
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(@Valid @RequestBody SignInDto signInDto) {

        UserEntity userEntity = userService.getUserById(signInDto.getUserId());
        authService.comparePassword(userEntity, signInDto.getUserPassword());



        SignInResponseDto.SignInResponseDtoBuilder builder
            = SignInResponseDto.builder()
            .userId(userEntity.getUserId())
            .userName(userEntity.getUserName())
            .userNickName(userEntity.getUserNickName())
            .userRole(userEntity.getUserRole())
            .userMarketingAgreement(userEntity.isUserMarketingAgreement())
            ;
        return ResponseEntity.ok(builder.build());
    }


    @GetMapping("")
    public ResponseEntity<String> test() {
//        throw new CustomException(ErrorCodeAndMessage.TEST_EXCEPTION);

        return ResponseEntity.ok("test");
    }

    @PostMapping("/oauth2/sign-in")
    public ResponseEntity<SignInResponseDto> oAuth2SignUp(@RequestBody OAuth2TokenRequestDto oAuth2TokenRequestDto)
        throws URISyntaxException {

        String url = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" +
            oAuth2TokenRequestDto.getTokenObj().get("access_token");

        System.out.println("url: " + url);

        UserEntity oAuth2user = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<OAuth2VerifyResponseDto> responseDtoResponseEntity
                = restTemplate.exchange(url, HttpMethod.POST, null, OAuth2VerifyResponseDto.class);

            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println(responseDtoResponseEntity.getBody());
            //정상적인 토큰


            UserEntity userEntity = userService.getOAuth2UserOrNull(oAuth2TokenRequestDto.getProvider(), oAuth2TokenRequestDto.getProviderId());
            if(Objects.isNull(userEntity)){
                //없을 시 자동회원가입
                oAuth2user = authService.oAuth2SignUp(oAuth2TokenRequestDto, responseDtoResponseEntity.getBody());

            }else {
                //유저 존재 시 로그인
            }





        } catch (HttpClientErrorException e) {
            //토큰 오류
            throw new CustomException(ErrorCodeAndMessage.GOOGLE_OAUTH2_TOKEN_INVALID);
        }



        SignInResponseDto.SignInResponseDtoBuilder builder
            = SignInResponseDto.builder()
            .userId(oAuth2user.getUserId())
            .userName(oAuth2user.getUserName())
            .userNickName(oAuth2user.getUserNickName())
            .userRole(oAuth2user.getUserRole())
            .userMarketingAgreement(oAuth2user.isUserMarketingAgreement())
            ;

        URI uri = new URI("/auth/sign-in");
        return ResponseEntity.created(uri).body(builder.build());
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserEntity> signUp(@Valid @RequestBody SignUpDto signUpDto) throws URISyntaxException {
        UserEntity userEntity = authService.signUp(signUpDto);
        userEntity.setRegisterUserIdx(userEntity.getUserIdx());

        URI uri = new URI("/auth/sign-in");
        return ResponseEntity.created(uri).body(userEntity);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut() {
        return ResponseEntity.ok("logout");
    }

}
