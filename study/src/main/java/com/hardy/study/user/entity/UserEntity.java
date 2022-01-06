package com.hardy.study.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.hardy.study.common.enums.Role;
import com.sun.istack.NotNull;
import com.hardy.study.common.entity.BaseEntity;
import com.hardy.study.common.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
@SuperBuilder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
//public class UserEntity extends BaseEntity implements UserDetails {
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(unique = true)
    @NotNull
    private String userId;

    @NotNull
    private String userPassword;

    @NotNull
    private String userName;

    @Column(unique = true)
    @NotNull
    private String userNickName;

    @NotNull
    @Email
    private String userEmail;

    //폰번호
    @NotNull
    private String userPhoneNumber;

    //전화번호
    private String userTelephone;

    @NotNull
    private String userAddress;

    @NotNull
    private  String userZipcode;

    private  String userAddressDetail;

    @NotNull
    private boolean userTermsAgreement;

//    @NotNull
    private boolean userMarketingAgreement;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Role userRole;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        GrantedAuthority grantedAuthority = new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return String.valueOf(userRole);
//            }
//        };
//        authorities.add(grantedAuthority);
//
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return userPassword;
//    }
//
//    @Override
//    public String getUsername() {
//        return userName;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
