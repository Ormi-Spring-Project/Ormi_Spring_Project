package com.team8.Spring_Project.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/v1/login","/v1/main", "/v1/signup", "/v1/login", "/v1/posts/article-items").permitAll() // 권한 없이 접근 가능한 자원
                .requestMatchers("/v1/admin/**").hasRole("ADMIN") // 관리자 관련은 관리자만
                .requestMatchers("/v1/posts/post/**").hasAnyRole("USER", "ADMIN") // 일반 게시글은 유저, 관리자만
                .anyRequest().authenticated() // 나머지 경우는 전부 인증 필요.
        ).formLogin(form -> form
                .loginPage("/v1/login")
                .loginProcessingUrl("/v1/login")
                .defaultSuccessUrl("/v1/main", true) // 로그인 성공하면 무조건 main으로.
                .failureUrl("/v1/login?error=true")
                .usernameParameter("email") // signIn.html의 email input의 name명과 동일해야한다.
                .passwordParameter("password")
                .permitAll()
        ).logout(logout -> logout
                .logoutUrl("/v1/logout")
                .logoutSuccessUrl("/v1/main")
                .invalidateHttpSession(true)
                .permitAll()
        ).csrf(csrf -> csrf.disable()); // csrf?


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


}
