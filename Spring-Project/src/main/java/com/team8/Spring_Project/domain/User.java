package com.team8.Spring_Project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String nickname;
    private String password;
    private String phoneNumber;
    private Authority authority;

    public void updateUser(String email, String nickname, String password, String phoneNumber) {
        if (email != null) this.email = email;
        if (nickname != null) this.nickname = nickname;
        if (password != null) this.password = password;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
    }

    public void banUser(Authority authority) {
        if (this.authority == Authority.USER) {
            this.authority = Authority.BANNED;
        }
    }

    public void activateUser(Authority authority) {
        if (this.authority == Authority.BANNED) {
            this.authority = Authority.USER;
        }
    }
}
