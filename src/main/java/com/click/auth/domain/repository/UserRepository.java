package com.click.auth.domain.repository;

import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository
        extends JpaRepository<User, UUID> {

    Optional<User> findByUserIdentityAndUserIdentityType(String identity, UserIdentityType type);
}