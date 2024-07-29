package com.team8.Spring_Project.infrastructure.persistence;

import com.team8.Spring_Project.domain.Authority;
import com.team8.Spring_Project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByAuthorityNot(Authority authority);

    void deleteUserById(Long id);
}
