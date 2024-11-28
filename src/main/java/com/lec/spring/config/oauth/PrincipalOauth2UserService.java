package com.lec.spring.config.oauth;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.config.oauth.provider.FacebookUserInfo;
import com.lec.spring.config.oauth.provider.GoogleUserInfo;
import com.lec.spring.config.oauth.provider.NaverUserInfo;
import com.lec.spring.config.oauth.provider.OAuth2UserInfo;
import com.lec.spring.domain.Authority;
import com.lec.spring.domain.User;
import com.lec.spring.repository.AuthorityRepository;
import com.lec.spring.repository.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
/**
 * OAuth2UserService<OAuth2UserRequest, OAuth2User>(I)
 *  └─ DefaultOAuth2UserService
 */
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    // 여기서 인증후 '후처리' 를 해주어야 한다.

    // 회원 조회, 가입 처리를 위한 주입
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public PrincipalOauth2UserService(SqlSession sqlSession){
        this.userRepository = sqlSession.getMapper(UserRepository.class);
        this.authorityRepository = sqlSession.getMapper(AuthorityRepository.class);
    }

    @Value("${app.oauth2.password}")
    private String oauth2Password;  // OAuth2 회원가입시 기본 PW


    // 인증직후 loadUser() 는 구글로 부터 받은 userRequest 데이터에 대한 후처리 진행
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);  // 사용자 프로필 정보 가져오기

        // 어떠한 정보가 넘어오는지 확인해보자.
        System.out.println("""
                [loadUser() 호출]
                  ClientRegistration: %s
                  RegistratoinId: %s
                  AccessToken: %s
                  OAuth2User Attributes: %s
                """.formatted(userRequest.getClientRegistration()  // ClientRegistration
                , userRequest.getClientRegistration().getRegistrationId()  // String. 어떤 OAuth로 로그인 했는지 알수 있다.
                , userRequest.getAccessToken().getTokenValue() // String
                , oAuth2User.getAttributes()  // Map<String, Object> ← 사용자 프로필 정보가 있다.
        ));

        // userRequest에는
        // 구글로그인버튼 클릭 → 구글로그인창 → 로그인완료
        //   → code리턴 (Oauth2 client 라이브러리가 받음) → AccessToken 요청

        // loadUser(userRequest) 는
        //  위 userRequest 정보를 사용하여 → 구글로부터 회원 프로필 받아음.

        // -------------------------------------------------------------------
        // 후처리: 강제로 회원 가입 진행
        String provider = userRequest.getClientRegistration().getRegistrationId();  // "google"

        OAuth2UserInfo oAuth2UserInfo = switch (provider.toLowerCase()){
            case "google" -> new GoogleUserInfo(oAuth2User.getAttributes());
            case "facebook" -> new FacebookUserInfo(oAuth2User.getAttributes());
            case "naver" -> new NaverUserInfo(oAuth2User.getAttributes());
            default -> null;
        };

        String providerId = oAuth2UserInfo.getProviderId();
        // username은 중복되지 않도록 만들어야 한다
        String username = provider + "_" + providerId; // "ex) google_xxxxxxxx"
        String password = oauth2Password;
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();

        System.out.println("""
                [OAuth2인증 회원 정보]
                  username: %s
                  name: %s
                  email: %s
                  password: %s   
                  provider: %s
                  providerId: %s             
                """.formatted(username, name, email, password, provider, providerId));

        // 회원 가입 진행하기 전에
        // 이미 가입한 회원인지, 혹은 비가입자인지 체크하여야 한다
        User user = userRepository.findByUsername(username);
        if(user == null) {  // 미 가입자인 경우에만 회원가입 진행
            User newUser = User.builder()
                    .username(username)
                    .name(name)
                    .email(email)
                    .password(password)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            int cnt = userRepository.save(newUser);
            Authority auth = authorityRepository.findByName("ROLE_MEMBER");
            Long userId = newUser.getId();
            Long authId = auth.getId();
            authorityRepository.addAuthority(userId, authId);

            if (cnt > 0) {
                System.out.println("[OAuth2 인증 회원 가입 성공]");
                user = userRepository.findByUsername(username);  // 다시 읽어와야 한다. id, regDate 등의 정보
            } else {
                System.out.println("[OAuth2 인증 회원 가입 실패]");
            }

        } else {
            System.out.println("[OAuth2 인증.  이미 가입된 회원입니다]");
        }

        PrincipalDetails principalDetails = new PrincipalDetails(user, oAuth2User.getAttributes());
        principalDetails.setAuthorityRepository(authorityRepository);  // 꼭!
        return principalDetails;  // 결국 OAuth2User 를 리턴한 셈!
    }
}












