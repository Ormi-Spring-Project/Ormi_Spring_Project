package com.team8.Spring_Project.config;

import com.team8.Spring_Project.application.UserService;
import com.team8.Spring_Project.domain.Authority;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

// Bean에 등록.
@Component
public class UserStatusCheckFilter extends OncePerRequestFilter {

    private final UserService userService;

    public UserStatusCheckFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userEmail = userDetails.getUsername();
            Authority currentAuthority = userService.getUserAuthority(userEmail);
            if (currentAuthority == null) return;

            if (!hasAuthority(authentication, currentAuthority)) {
                List<GrantedAuthority> newAuthorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + currentAuthority.name()));
                Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, null, newAuthorities);
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }

        }

        filterChain.doFilter(request, response);
    }

    private boolean hasAuthority(Authentication auth, Authority authority) {
        String role = "ROLE_" + authority.name();

        for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
