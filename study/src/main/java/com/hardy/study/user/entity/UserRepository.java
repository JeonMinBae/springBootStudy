package com.hardy.study.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository 어노테이션이 없어도 IoC가 됨
//JpaRepository을 상속받았기 때문
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAll();

    Optional<UserEntity> findByUserIdx(String idx);

    Optional<UserEntity> findByUserId(String id);

    Optional<UserEntity> findByUserNickName(String nickName);

    UserEntity save(UserEntity userEntity);

    void deleteByUserIdx(String idx);

    void deleteByUserIdIn(List<String> id);

    Boolean existsByUserId(String id);

    Boolean existsByUserNickName(String nickName);


}
