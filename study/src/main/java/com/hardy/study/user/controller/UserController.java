package com.hardy.study.user.controller;


import com.hardy.study.user.dto.UserCreateDto;
import com.hardy.study.user.dto.UserDeleteListDto;
import com.hardy.study.user.dto.UserUpdateDto;
import com.hardy.study.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hardy.study.user.service.UserService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

//    @Autowired
    private final UserService userService;

    @GetMapping("/test")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_GUEST')")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("hello");
    }


    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserEntity> createUser(@Valid @RequestBody UserCreateDto userCreateDto) throws URISyntaxException {
        UserEntity userEntity = userService.createUser(userCreateDto);
        userEntity.setRegisterUserIdx(userEntity.getUserIdx());

        URI uri = new URI("/users/" + userCreateDto.getUserId());
        return ResponseEntity.created(uri).body(userEntity);

    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_GUEST')")
    public ResponseEntity<List<UserEntity>> getUsers(){
        List<UserEntity> userEntity = userService.getUsers();

        return ResponseEntity.ok(userEntity);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_GUEST')")
    public ResponseEntity<UserEntity> getUser(@PathVariable String userId){
        UserEntity userEntity = userService.getUserById(userId);

        return ResponseEntity.ok(userEntity);
    }

    @PutMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserEntity> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto){
        UserEntity userEntity = userService.getUserById(userUpdateDto.getUserId());
        userService.updateUser(userEntity, userUpdateDto);

        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDeleteListDto> deleteUsers(@RequestBody UserDeleteListDto userDeleteListDto){
        userService.delete(userDeleteListDto);

        return ResponseEntity.ok(userDeleteListDto);
    }

}
