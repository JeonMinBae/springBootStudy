package com.hardy.study.auth.service;

import com.hardy.study.auth.dto.SignUpDto;
import com.hardy.study.auth.oauth2.OAuth2TokenRequestDto;
import com.hardy.study.auth.oauth2.OAuth2VerifyResponseDto;
import com.hardy.study.common.enums.ErrorCodeAndMessage;
import com.hardy.study.common.enums.Role;
import com.hardy.study.user.entity.UserEntity;
import com.hardy.study.user.entity.UserRepository;
import com.hardy.study.user.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public boolean existOAuth2User(String provider, String providerId){
        return userRepository.existsByProviderAndProviderId(provider, providerId);
    }

    @Transactional
    public UserEntity signUp(SignUpDto signUpDto){
        if(userRepository.existsByUserId(signUpDto.getUserId())) {
            throw new CustomException(ErrorCodeAndMessage.ALREADY_EXIST_USER_ID);
        }
        if(userRepository.existsByUserNickName(signUpDto.getUserNickName())){
            throw new CustomException(ErrorCodeAndMessage.ALREADY_EXIST_USER_NICKNAME);
        }

        String userId = signUpDto.getUserId();
        String userPassword = passwordEncoder.encode(signUpDto.getUserPassword());
        String userName = signUpDto.getUserName();
        String userNickName = signUpDto.getUserNickName();
        String userEmail = signUpDto.getUserEmail();
        String userPhoneNumber = signUpDto.getUserPhoneNumber();
        String userTelephone = signUpDto.getUserTelephone();
        String userAddress = signUpDto.getUserAddress();
        String userZipcode = signUpDto.getUserZipcode();
        String userAddressDetail = signUpDto.getUserAddressDetail();
        boolean userMarketingAgreement =
            Objects.isNull(signUpDto.isUserMarketingAgreement()) ? false : signUpDto.isUserMarketingAgreement();


        UserEntity.UserEntityBuilder builder = UserEntity.builder()
            .userId(userId)
            .userPassword(userPassword)
            .userName(userName)
            .userNickName(userNickName)
            .userEmail(userEmail)
            .userPhoneNumber(userPhoneNumber)
            .userTelephone(userTelephone)
            .userAddress(userAddress)
            .userZipcode(userZipcode)
            .userAddressDetail(userAddressDetail)
            .userTermsAgreement(true)
            .userMarketingAgreement(userMarketingAgreement)
            .userRole(Role.ROLE_CONSUMER)
            ;


        return userRepository.save(builder.build());
    }


    @Transactional
    public UserEntity oAuth2SignUp(OAuth2TokenRequestDto oAuth2TokenRequestDto,
                                   OAuth2VerifyResponseDto oAuth2VerifyResponseDto){
        if(userRepository.existsByProviderAndProviderId(oAuth2TokenRequestDto.getProvider(), oAuth2TokenRequestDto.getProviderId())) {
            throw new CustomException(ErrorCodeAndMessage.ALREADY_EXIST_USER_ID);
        }

        Map<String, Object> profile = oAuth2TokenRequestDto.getProfileObj();
        Map<String, Object> token = oAuth2TokenRequestDto.getTokenObj();


        String userId = oAuth2TokenRequestDto.getProvider() + "_"+ oAuth2TokenRequestDto.getProviderId();
        String userPassword = passwordEncoder.encode(oAuth2TokenRequestDto.getProviderId() + "t728hvtw3u3vqh1jws");
        String userName = String.valueOf(profile.get("familyName")) + profile.get("givenName");
        String userNickName = String.valueOf(profile.get("email"));
        String userEmail = String.valueOf(profile.get("email"));


        UserEntity.UserEntityBuilder builder = UserEntity.builder()
            .userId(userId)
            .userPassword(userPassword)
            .userName(userName)
            .userNickName(userNickName)
            .userEmail(userEmail)
            .userTermsAgreement(true)
            .userRole(Role.ROLE_CONSUMER)
            .userMarketingAgreement(false)
            .provider(oAuth2TokenRequestDto.getProvider())
            .providerId(oAuth2TokenRequestDto.getProviderId())
            ;


        return userRepository.save(builder.build());
    }

    //@Transactional
    public boolean comparePassword(UserEntity userEntity, String rawPassword){
        if(!passwordEncoder.matches(rawPassword, userEntity.getUserPassword())){
            throw new CustomException(ErrorCodeAndMessage.LOGIN_FAILED);
        }

        return true;
    }

    public boolean checkVaildAccount(UserEntity userEntity){
        //만료
        //잠금
        //비밀번호만료
        //사용불가
        return false;
    }


}
