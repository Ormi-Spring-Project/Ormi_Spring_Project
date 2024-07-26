package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.UserDTO;
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

        return userDTO;
    }
}
