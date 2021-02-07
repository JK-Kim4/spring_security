package com.demo.securityDemo.config.auth;

// security가 /login 요청을 가로채 로그인 진행
// 로그인 진행이 완료되면 security session 생성 (Security ContextHolder)
// Object -> Authentication Type 객체
// Authentication 객체 내부에 User 정보가 있어야됨
// User Object의 타입 => UserDetails 타입 객체

// Security Session 영역 => Authentication 객체 => UserDetails(PrincipalDetails) Type 으로 User Object접근

import com.demo.securityDemo.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    //컴포지션
    private User user;
    private Map<String, Object> attributes;

    //일반회원 로그인
    public PrincipalDetails (User user){
        this.user = user;
    }

    //SNS로그인 (OAuth 로그인)
    public PrincipalDetails (User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    //해당 User의 권한(String role)을 return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
