package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.UserDTO;
import com.team8.Spring_Project.domain.Authority;
import com.team8.Spring_Project.domain.User;
import com.team8.Spring_Project.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO login(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());

        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(userDTO.getPassword())) {
            return null;
        }

        return userDTO.fromEntity(user);
    }

    public UserDTO signUp(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());

        if (user != null) {
            return null;
        }

        userDTO.setAuthority(Authority.USER);
        userRepository.save(userDTO.toEntity());
        return userDTO;
    }

    public UserDTO findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id를 가진 User가 존재하지 않습니다."));

        UserDTO userDTO = new UserDTO();
        return userDTO.fromEntity(user);
    }
}
