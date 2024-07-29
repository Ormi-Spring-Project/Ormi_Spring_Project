package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.Authority;
import com.team8.Spring_Project.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private String nickname;
    private String password;
    private String phoneNumber;
    private Authority authority;

    public User toEntity() {
        return User.builder()
                .email(this.email)
                .nickname(this.nickname)
                .password(this.password)
                .phoneNumber(this.phoneNumber)
                .authority(this.authority)
                .build();
    }

    public UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getAuthority()
        );
    }
}
