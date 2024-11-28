package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String username;  // 회원 아이디
    @JsonIgnore
    private String password;
    @ToString.Exclude
    @JsonIgnore
    private String re_password;  // 비밀번호 확인 입력
    private String name;  // 회원 이름
    @JsonIgnore
    private String email;
    @JsonIgnore
    private LocalDateTime regDate;

    // User: Authority = N:M
    // 특정 User 의 권한(들)
//    @ToString.Exclude  // toString() 결과에서는 제외.
//    @JsonIgnore
//    private List<Authority> authorities = new ArrayList<>();


    //-------------------------------------------------------

    //OAuth2 client
    private String provider;
    private String providerId;



}








