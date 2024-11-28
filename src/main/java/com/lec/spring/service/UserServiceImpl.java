package com.lec.spring.service;

import com.lec.spring.domain.Authority;
import com.lec.spring.domain.User;
import com.lec.spring.repository.AuthorityRepository;
import com.lec.spring.repository.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, SqlSession sqlSession){
        this.passwordEncoder = passwordEncoder;
        userRepository = sqlSession.getMapper(UserRepository.class);
        authorityRepository = sqlSession.getMapper(AuthorityRepository.class);
    }


    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username.toUpperCase());
    }

    @Override
    public boolean isExist(String username) {
        User user = findByUsername(username.toUpperCase());
        return (user != null);
    }

    //신규 회원 등록
    // User (username, password)
    @Override
    public int register(User user) {

        user.setUsername(user.getUsername().toUpperCase());   // DB 에는 대문자로 저장.
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // password 는 PasswordEncoder 로 암호화 하여 저장해야 한다.
        userRepository.save(user);   // 회원 저장, id 값 받아온다.

        // 신규회원은 ROLE_MEMBER 권한을 기본적으로 부여하기
        Authority auth = authorityRepository.findByName("ROLE_MEMBER");

        Long userId = user.getId();
        Long authId = auth.getId();
        authorityRepository.addAuthority(userId, authId);

        return 1;
    }

    // 특정 사용자(id) 의 authority(들)
    @Override
    public List<Authority> selectAuthoritiesById(Long id) {

        User user = userRepository.findById(id);
        if (user == null) return null;

        return authorityRepository.findByUser(user);
    }
}
