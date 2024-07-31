package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.UserDTO;
import com.team8.Spring_Project.domain.Authority;
import com.team8.Spring_Project.domain.User;
import com.team8.Spring_Project.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findByAuthorityNot(Authority.ADMIN);
        List<UserDTO> userDTOList = new ArrayList<>();

        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTOList.add(userDTO.fromEntity(user));
        }

        return userDTOList;
    }

    @Transactional(readOnly = true)
    public UserDTO findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id를 가진 User가 존재하지 않습니다."));

        UserDTO userDTO = new UserDTO();
        return userDTO.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public User findUserEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id를 가진 User가 존재하지 않습니다."));
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 id를 가진 User가 존재하지 않습니다."));

        user.updateUser(
                userDTO.getEmail(),
                userDTO.getNickname(),
                userDTO.getPassword(),
                userDTO.getPhoneNumber()
        );

        return userDTO.fromEntity(user);
    }

    @Transactional
    public void deleteUser(UserDTO userDTO) {
        userRepository.deleteUserById(userDTO.getId());
    }

    @Transactional
    public void changeUserAuthority(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id를 가진 User가 존재하지 않습니다."));

        if (user.getAuthority().equals(Authority.USER)) {
            user.banUser(Authority.BANNED);
            return;
        }

        if (user.getAuthority().equals(Authority.BANNED)) {
            user.activateUser(Authority.USER);
        }
    }
}
