package com.demo.securityDemo.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Generated;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class User {

    private int id;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String role;
    private Timestamp createDate;

    //SNS_login_information
    private String provider;
    private String providerId;

    @Builder
    public User(String userName,
                String userPassword,
                String userEmail,
                String role,
                Timestamp createDate,
                String provider,
                String providerId) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.role = role;
        this.createDate = createDate;
        this.provider = provider;
        this.providerId = providerId;
    }
}
