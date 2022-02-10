package com.hardy.study.auth.oauth2;

import com.hardy.study.auth.PrincipalDetails;
import com.hardy.study.common.enums.Role;
import com.hardy.study.user.entity.UserEntity;
import com.hardy.study.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User =  super.loadUser(userRequest);

        // code를 통해 구성한 정보
        System.out.println("userRequest clientRegistration : " + userRequest.getClientRegistration());
        // token을 통해 응답받은 회원정보
        System.out.println("oAuth2User : " + oAuth2User);

        return processOAuth2User(userRequest, oAuth2User);

    }


    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User){
        IOAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("google oauth");
            System.out.println("oAuth2User.getAttributes()" + oAuth2User.getAttributes());
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else{
            System.out.println("oauth else");
        }


        //유저가져오기
        Optional<UserEntity> user
            = userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

        UserEntity userEntity;
        if(!user.isPresent()){
        //없으면 회원가입
            userEntity = UserEntity.builder()
                            .userId(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                            .userEmail(oAuth2UserInfo.getEmail())
                            .userName(oAuth2UserInfo.getName())
                            .userRole(Role.ROLE_CONSUMER)
                            .userTermsAgreement(true)
                            .provider(oAuth2UserInfo.getProvider())
                            .providerId(oAuth2UserInfo.getProviderId())
                            .build()
                            ;

            userRepository.save(userEntity);
        }else{
            userEntity = user.get();
            userEntity.setUserEmail(oAuth2UserInfo.getEmail());
            userRepository.save(userEntity);
        }


        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }



}
