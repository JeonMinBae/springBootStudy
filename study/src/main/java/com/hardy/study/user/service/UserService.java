package com.hardy.study.user.service;

import com.hardy.study.common.enums.ErrorCodeAndMessage;
import com.hardy.study.common.enums.Role;
import com.hardy.study.common.enums.UserType;
import com.hardy.study.user.dto.UserDeleteListDto;
import com.hardy.study.user.dto.UserUpdateDto;
import com.hardy.study.user.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hardy.study.user.dto.UserCreateDto;
import com.hardy.study.user.entity.UserEntity;
import com.hardy.study.user.entity.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UserEntity getUserByIdx(String idx) {
        UserEntity user = userRepository.findByUserIdx(idx)
            .orElseThrow(() -> new CustomException(ErrorCodeAndMessage.USER_NOT_FOUNT));

        return user;
    }

    @Transactional(readOnly = true)
    public UserEntity getUserById(String id) {
        UserEntity user = userRepository.findByUserId(id)
            .orElseThrow(() -> new CustomException(ErrorCodeAndMessage.USER_NOT_FOUNT));

        return user;
    }

    @Transactional(readOnly = true)
    public UserEntity getOAuth2UserOrNull(String provider, String providerId) {
        Optional<UserEntity> user = userRepository.findByProviderAndProviderId(provider, providerId);
        if(user.isPresent()){
            return user.get();
        }

        return null;
    }

    @Transactional(readOnly = true)
    public boolean existOAuth2User(String provider, String providerId) {
        return userRepository.existsByProviderAndProviderId(provider, providerId);
    }



    @Transactional
    public UserEntity createUser(UserCreateDto userCreateDto) {
        if(userRepository.existsByUserId(userCreateDto.getUserId())) {
            throw new CustomException(ErrorCodeAndMessage.ALREADY_EXIST_USER_ID);
        }
        if(userRepository.existsByUserNickName(userCreateDto.getUserNickName())){
            throw new CustomException(ErrorCodeAndMessage.ALREADY_EXIST_USER_NICKNAME);
        }


        String userId = userCreateDto.getUserId();
        String userPassword = passwordEncoder.encode(userCreateDto.getUserPassword());
        String userName = userCreateDto.getUserName();
        String userNickName = userCreateDto.getUserNickName();
        String userEmail = userCreateDto.getUserEmail();
        String userPhoneNumber = userCreateDto.getUserPhoneNumber();
        String userTelephone = userCreateDto.getUserTelephone();
        String userAddress = userCreateDto.getUserAddress();
        String userZipcode = userCreateDto.getUserZipcode();
        String userAddressDetail = userCreateDto.getUserAddressDetail();
        Role userRole = userCreateDto.getUserRole();
        boolean userTermsAgreement = userCreateDto.isUserTermsAgreement();
        boolean userMarketingAgreement = userCreateDto.isUserMarketingAgreement();


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
            .userTermsAgreement(userTermsAgreement)
            .userMarketingAgreement(userMarketingAgreement)
            .userRole(userRole)
            ;



        return userRepository.save(builder.build());
    }

    @Transactional
    public void updateUser(UserEntity userEntity, UserUpdateDto userUpdateDto) {
        if (Objects.isNull(userEntity)) {
            throw new CustomException(ErrorCodeAndMessage.USER_NOT_FOUNT);
        }

        if (!Objects.isNull(userUpdateDto.getUserName()) &&
            !userEntity.getUserName().equals(userUpdateDto.getUserName())) {
            userEntity.setUserName(userUpdateDto.getUserName());
        }

        if (!Objects.isNull(userUpdateDto.getUserNickName()) &&
            !userEntity.getUserNickName().equals(userUpdateDto.getUserNickName())) {
            if(!Objects.isNull(userRepository.findByUserNickName(userUpdateDto.getUserNickName()))){
                throw new CustomException(ErrorCodeAndMessage.ALREADY_EXIST_USER_NICKNAME);
            }
            userEntity.setUserNickName(userUpdateDto.getUserNickName());
        }

        if (!Objects.isNull(userUpdateDto.getUserEmail()) &&
            !userEntity.getUserEmail().equals(userUpdateDto.getUserEmail())) {
            userEntity.setUserEmail(userUpdateDto.getUserEmail());
        }

        if (!Objects.isNull(userUpdateDto.getUserPhoneNumber()) &&
            !userEntity.getUserPhoneNumber().equals(userUpdateDto.getUserPhoneNumber())) {
            userEntity.setUserPhoneNumber(userUpdateDto.getUserPhoneNumber());
        }
        if (!Objects.isNull(userUpdateDto.getUserTelephone()) &&
            !userEntity.getUserTelephone().equals(userUpdateDto.getUserTelephone())) {
            userEntity.setUserTelephone(userUpdateDto.getUserTelephone());
        }

        if (!Objects.isNull(userUpdateDto.getUserAddress()) &&
            !userEntity.getUserAddress().equals(userUpdateDto.getUserAddress())) {
            userEntity.setUserAddress(userUpdateDto.getUserAddress());
        }

        if (!Objects.isNull(userUpdateDto.getUserZipcode()) &&
            !userEntity.getUserZipcode().equals(userUpdateDto.getUserZipcode())) {
            userEntity.setUserPhoneNumber(userUpdateDto.getUserZipcode());
        }

        if (!Objects.isNull(userUpdateDto.getUserAddressDetail()) &&
            !userEntity.getUserAddressDetail().equals(userUpdateDto.getUserAddressDetail())) {
            userEntity.setUserAddressDetail(userUpdateDto.getUserAddressDetail());
        }

        userRepository.save(userEntity);

    }

    @Transactional
    public void delete(UserDeleteListDto userDeleteListDto){

        userRepository.deleteByUserIdIn(userDeleteListDto.getUserIdList());
    }

    public boolean passwordCheck(UserEntity userEntity, String signInPassword){
        if(passwordEncoder.matches(signInPassword, userEntity.getUserPassword())){
            return true;
        }else{
            throw new CustomException(ErrorCodeAndMessage.LOGIN_FAILED);
        }
    }

}
