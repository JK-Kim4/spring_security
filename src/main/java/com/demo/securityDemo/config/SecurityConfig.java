package com.demo.securityDemo.config;

// 1.코드 받기(인증) 2.엑세스토큰(권한)
// 3. 사용자 프로필 정보를 가져오고
// 4-1. 가져온 정보를 토대로 회원가입을 자동으로 진행시키기도 함.
// 4-2 (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집주소),백화점몰 -> (vip등급, 일반등급)
import com.demo.securityDemo.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() //인증받은 사용자는 접근할 수 있는 메뉴
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .usernameParameter("userName")
                .passwordParameter("userPassword")
                .loginProcessingUrl("/login") //login주소가 호출되면 시큐리티가 대신 로그인 진행
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService); //구글 로그인이 완료된 뒤의 후처리가 필요함. Tip. 코드X ( 엑세스 토큰 + 사용자 프로필 정보 O )
    }
}
