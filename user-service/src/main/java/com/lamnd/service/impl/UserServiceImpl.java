package com.lamnd.service.impl;

import com.lamnd.dto.identity.KeycloakUserDTO;
import com.lamnd.dto.request.UserCreateRequest;
import com.lamnd.dto.request.UserUpdateRequest;
import com.lamnd.dto.response.UserResponse;
import com.lamnd.entity.User;
import com.lamnd.exception.ResourceExistedException;
import com.lamnd.exception.ResourceNotFoundException;
import com.lamnd.mapper.UserMapper;
import com.lamnd.repository.UserRepo;
import com.lamnd.service.KeycloakService;
import com.lamnd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepo userRepo;
    private final KeycloakService keycloakService;

    @Override
    public void createUser(UserCreateRequest request) {
        User user = userMapper.toEntity(request);

        // xử lý race condition khi có nhiều request tạo user cùng lúc với username/email/phone giống nhau
        saveUser(user);
    }

    @Override
    public List<UserResponse> getUsers() {
        List<User> users = userRepo.findAll();
        return userMapper.toList(users);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User existingUser = findUserById(id);
        return userMapper.toDTO(existingUser);
    }

    @Override
    public UserResponse getMyInfo() {
//        var context = SecurityContextHolder.getContext();
//
//        String username = context.getAuthentication().getName();
//
//        User user = findUserByUsername(username);
//
//        return userMapper.toDTO(user);
        return null;
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest request, Long id) {
        User existingUser = findUserById(id);

        // kiểm tra trùng lặp username/email/phone khi update và bỏ qua chính bản thân user đang update
        boolean isUserNameExisted = userRepo.existsByUsernameAndIdNot(request.username(), id);
        boolean isEmailExisted = userRepo.existsByEmailAndIdNot(request.email(), id);
        boolean isPhoneExisted = userRepo.existsByPhoneNumberAndIdNot(request.phoneNumber(), id);

        // Bước này chỉ check và không xử lý được race condition
        if (isUserNameExisted) {
            throw new ResourceExistedException("User", "username", request.username());
        }
        if (isEmailExisted) {
            throw new ResourceExistedException("User", "email", request.email());
        }
        if (isPhoneExisted && request.phoneNumber() != null) {
            throw new ResourceExistedException("User", "phone", request.phoneNumber());
        }

        // cập nhật thông tin user với dữ liệu mới từ request
        userMapper.updateEntityFromRequest(request, existingUser);

        // xử lý race condition khi có nhiều request update cùng lúc với username/email/phone giống nhau
        User updatedUser = saveUser(existingUser);

        return userMapper.toDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        findUserById(id);
        userRepo.deleteById(id);
    }

    @Override
    public UserResponse getUserFromToken(String token) {
        KeycloakUserDTO keycloakUserDTO = keycloakService.fetchUserProfileByToken(token);

        User existingUser = findUserByEmail(keycloakUserDTO.getEmail());

        return userMapper.toDTO(existingUser);
    }

    private User findUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    private User findUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private User saveUser(User user) {
        try {
            return userRepo.save(user);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("uq_users_email")) {
                throw new ResourceExistedException("User", "email", user.getEmail());
            }

            if (ex.getMessage().contains("uq_users_username")) {
                throw new ResourceExistedException("User", "username", user.getUsername());
            }

            if (ex.getMessage().contains("uq_users_phone")) {
                throw new ResourceExistedException("User", "phone number", user.getPhoneNumber());
            }

            throw ex;
        }
    }
}
