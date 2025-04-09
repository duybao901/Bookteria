package com.bookteria.identity_services.services;

import com.bookteria.identity_services.dto.request.UserCreationRequest;
import com.bookteria.identity_services.dto.request.UserUpdateRequest;
import com.bookteria.identity_services.dto.response.UserResponse;
import com.bookteria.identity_services.entities.User;
import com.bookteria.identity_services.enums.Role;
import com.bookteria.identity_services.exceptions.UserException;
import com.bookteria.identity_services.mapper.IUserMapper;
import com.bookteria.identity_services.repositories.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final IUserRepository _userRepository;
    private final IUserMapper _userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(IUserRepository _userRepository, IUserMapper _userMapper) {
        this._userRepository = _userRepository;
        this._userMapper = _userMapper;
    }

    public UserResponse createUser(UserCreationRequest request) {
        if (_userRepository.existsByUsername(request.getUsername())) {
            throw new UserException.UserWithUserNameAlreadyExists(request.getUsername());
        }

        User user = _userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
//        user.setRoles(roles);

        return _userMapper.toUserResponse(_userRepository.save(user));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        List<User> users = _userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponses.add(_userMapper.toUserResponse(user));
        }
        return userResponses;
    }

    //    @PostAuthorize("")
    public UserResponse getUserById(String userId) {
        User user = _userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserException.UserNotFoundException(userId);
        }
        return _userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = _userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserException.UserNotFoundException(userId);
        }

        _userMapper.updateUser(user, request);

        return _userMapper.toUserResponse(_userRepository.save(user));
    }

    public UserResponse deleteUser(String userId) {
        User user = _userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserException.UserNotFoundException(userId);
        }

        _userRepository.delete(user);
        return _userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = _userRepository.findByUsername(name).orElse(null);
        if (user == null) {
            throw new UserException.UserWithUsernameNotFoundException(user.getUsername());
        }

        return _userMapper.toUserResponse(user);
    }
}
