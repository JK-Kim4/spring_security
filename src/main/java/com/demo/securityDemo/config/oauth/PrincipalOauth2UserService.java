package com.demo.securityDemo.config.oauth;

import com.demo.securityDemo.config.auth.PrincipalDetails;
import com.demo.securityDemo.model.User;
import com.demo.securityDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    //SNS로그인 (google) 후 처리 함수
    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest.getClientRegi : " + userRequest.getClientRegistration());
        System.out.println("userRequest.getAccessTokenValue : " + userRequest.getAccessToken().getTokenValue());
        System.out.println("userRequset.getclient : " +userRequest.getClientRegistration());
        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println("super.loadUser : " + super.loadUser(userRequest).getAttributes());
        // 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code 리턴 (OAuth-Client 라이브러리) -> AccessToken요청
        // userRequest 정보 -> loadUser 함수 호출 -> google로부터 회원 프로필 받아옴

        String provider = userRequest.getClientRegistration().getClientId(); //google
        String providerId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        String username = provider + "_" + providerId; // ex)google_123123123123123
        String password = bCryptPasswordEncoder.encode("비밀번호"); //임의 비밀번호 생성하여 insert (큰 의미는 없음)
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUserName(username);

        if(userEntity == null){
            System.out.println("구글 로그인 최초사용자 / 자동 회원가입");
            userEntity = User.builder()
                    .userName(username)
                    .userPassword(password)
                    .userEmail(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.insertUser(userEntity);
        }else{
            System.out.println("이미 로그인 한 사용자");
        }

        return new PrincipalDetails(userEntity);
    }
}
