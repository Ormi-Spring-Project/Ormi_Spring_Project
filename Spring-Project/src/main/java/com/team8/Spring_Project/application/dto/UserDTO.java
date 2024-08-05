package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.Authority;
import com.team8.Spring_Project.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements UserDetails {
    
    private Long id;
    private String email;
    private String nickname;
    private String password;
    private String phoneNumber;
    private Authority authority;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + authority.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    // 이 아래로는 각각 경우에 따른 설정을 해야함. 일단은 true.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User toEntity(PasswordEncoder passwordEncoder) {

        return User.builder()
                .email(this.email)
                .nickname(this.nickname)
                .password(passwordEncoder.encode(this.password))
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
