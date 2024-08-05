package com.click.auth.domain.entity;

import com.click.auth.domain.type.UserIdentityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "USERS", indexes = {
    @Index(name = "CODE_INDEX", columnList = "USER_CODE", unique = true),
    @Index(name = "IDENTITY_INDEX", columnList = "USER_IDENTITY", unique = true)})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "USER_ID")
    private UUID userId;

    @Column(name = "USER_IDENTITY")
    private String userIdentity;

    @Column(name = "USER_IDENTITY_TYPE")
    @Enumerated(EnumType.STRING)
    private UserIdentityType userIdentityType;

    @Column(name = "USER_CODE")
    private String userCode;

    @Column(name = "USER_PROFILE_IMG")
    private String userImg;

    @Column(name = "USER_NICK_NAME")
    private String userNickName;

    @Column(name = "USER_SIMPLE_PASSWD")
    private String userPasswd;

    @Column(name = "USER_SALT")
    private String userSalt;

    @Column(name = "USER_CREDIT_RANK")
    private Integer userCreditRank;

    @Column(name = "USER_CREATED_AT", nullable = false)
    private LocalDateTime userCreatedAt;

    @Column(name = "USER_TOKEN_VERSION")
    private Integer userTokenVersion;

    @Column(name = "USER_DISABLE", nullable = false)
    private Boolean isDisable;

    @Column(name = "USER_MAIN_ACCOUNT")
    private String userMainAccount;


    public void setPassword(String password, String salt) {
        userPasswd = password;
        userSalt = salt;
    }

    public void setImage(String image) {
        userImg = image;
    }

    public void setNickname(String name) {
        userNickName = name;
    }

    public void setRank(Integer rank) {
        userCreditRank = rank;
    }

    public void upTokenVersion() {
        userTokenVersion++;
    }

    public void disable() {
        userIdentity = null;
        userIdentityType = null;
        userCode = null;
        userImg = null;
        userNickName = null;
        userPasswd = null;
        userSalt = null;
        userCreditRank = null;
        userTokenVersion = null;
        isDisable = true;
    }
}
