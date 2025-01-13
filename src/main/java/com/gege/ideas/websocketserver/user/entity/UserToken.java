package com.gege.ideas.websocketserver.user.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "usertoken")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTokenId;

    @Column
    private String token;

    @Column
    private Long userId;

    @Column
    private Date generationDate;

    @Column
    private Date expirationDate;

    public Long getUserTokenId() {
        return userTokenId;
    }

    public void setUserTokenId(Long userTokenId) {
        this.userTokenId = userTokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserToken() {


    }
}
