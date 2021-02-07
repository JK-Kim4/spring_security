package com.demo.securityDemo.controller;

import com.demo.securityDemo.config.auth.PrincipalDetails;
import com.demo.securityDemo.model.User;
import com.demo.securityDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth2User){ //DI (의존성 주입)
        System.out.println("===================================test/login===================================");
        System.out.println("authentication : " + authentication.getPrincipal());
//        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); -> classcast Exception
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication.user = " + oAuth2User.getAttributes());
//        System.out.println("userDetails : " + userDetails.getUsername());
        //Annotation으로 casting과 객체 동시에 받아올 수 있음
        System.out.println("oauth2User : " + oauth2User.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping({"", "/"})
    public String index(){
        return "index";
    }

    //OAuth login || UserDetails login(일반로그인) 모두 PrincipalDetails객체로 받을 수 있음
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails : " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/loginForm")
    public String login(){
        return "loginForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println("user>>" + user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getUserPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setUserPassword(encPassword);
        int result = userRepository.insertUser(user);
        System.out.println("result>>" + result);
        return "joinForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }


}
