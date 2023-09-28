package com.labs.ad_board.service;

import com.labs.ad_board.db.entity.Role;
import com.labs.ad_board.db.entity.User;
import com.labs.ad_board.db.repository.UserRepository;
import com.labs.ad_board.dto.UserCreateEditDto;
import com.labs.ad_board.dto.UserReadDto;
import com.labs.ad_board.exception.EmailException;
import com.labs.ad_board.exception.UsernameException;
import com.labs.ad_board.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserReadDto registerNewUserAccount(UserCreateEditDto userCreateEditDto) {
        if (emailExist(userCreateEditDto.getEmail())) {
            throw new EmailException("Email already in use!");
        }
        if (usernameExist(userCreateEditDto.getUsername())) {
            throw new UsernameException("Username already in use");
        }

        //by default all users are USER
        //can be set to ADMIN though 'admin page'
        userCreateEditDto.setRole(Role.USER);

        return userMapper.map(
                userRepository.save(
                        userMapper.map(userCreateEditDto)
                )
        );
    }

    public boolean emailExist(String email) {
        return userRepository.countUserByEmail(email) == 1;
    }
    public boolean usernameExist(String username) {
        return userRepository.countUserByUsername(username) == 1;
    }
}
