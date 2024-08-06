package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.UserDTO;
import com.team8.Spring_Project.domain.Authority;
import com.team8.Spring_Project.domain.User;
import com.team8.Spring_Project.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("해당 유저 닉네임이 없습니다.");
        }

        return new UserDTO().fromEntity(user);
    }

    public UserDTO signUp(UserDTO userDTO) {
        // 중복 검사하는 로직.
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            return null;
        }

        if (userRepository.findByNickname(userDTO.getNickname()) != null) {
            return null;
        }

        userDTO.setAuthority(Authority.USER); // 유저 생성하는 로직이니까 일단 이렇게 해뒀는데 이거 해결해야함.
        userRepository.save(userDTO.toEntity(passwordEncoder));
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

        String usingNickname = userDTO.getNickname();

        boolean findNickname = userRepository.existsByNickname(usingNickname);

        // 1. 다른 사람하고 겹치면 안된다.
        // 2. 이전과 같은 정보(수정되지 않은 정보)는 유지한다.

        boolean nicknameCheck = !findNickname;

        if (findNickname) {
            if (user.getNickname().equals(usingNickname)) {
                nicknameCheck = true;
            }
        }

        if (!nicknameCheck) {
            return new UserDTO().fromEntity(user);
        }

        user.updateUser(
                userDTO.getNickname(),
                userDTO.getPassword(),
                userDTO.getPhoneNumber(),
                passwordEncoder
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

        if (user.getAuthority() == Authority.USER) {
            user.banUser(Authority.BANNED);
            return;
        }

        if (user.getAuthority() == Authority.BANNED) {
            user.activateUser(Authority.USER);
        }
    }

    // 실시간 권한 변경 처리를 위한 권한
    public Authority getUserAuthority(String email) {
        User user = userRepository.findByEmail(email);
        return user.getAuthority();
    }

}
