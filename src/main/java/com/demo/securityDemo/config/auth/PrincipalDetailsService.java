package com.demo.securityDemo.config.auth;

import com.demo.securityDemo.model.User;
import com.demo.securityDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUserName(username);
        System.out.println("User@Service = " + userEntity);
        if(userEntity != null){
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
