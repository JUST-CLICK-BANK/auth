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
@Table(name = "USERS")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "USER_ID")
    private UUID userId;

    @Column(name = "USER_IDENTITY", nullable = false)
    private String userIdentity;

    @Column(name = "USER_IDENTITY_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserIdentityType userIdentityType;

    @Column(name = "USER_NICK_NAME", nullable = false)
    private String userNickName;

    @Column(name = "USER_SIMPLE_PASSWD", nullable = false)
    private String userPasswd;

    @Column(name = "USER_CREDIT_RANK", columnDefinition = "700")
    private Integer userCreditRank;

    @Column(name = "USER_CREATED_AT", columnDefinition = "now()")
    private LocalDateTime userCreatedAt;

    @Column(name = "USER_TOKEN_VERSION", nullable = false)
    private Integer userTokenVersion;

    @Column(name = "USER_DISABLE", columnDefinition = "boolean default false")
    private Boolean isDisable;
}
