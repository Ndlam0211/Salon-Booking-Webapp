package com.lamnd.service;

import com.lamnd.dto.request.UserCreateRequest;
import com.lamnd.dto.request.UserUpdateRequest;
import com.lamnd.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    void createUser(UserCreateRequest request);
    List<UserResponse> getUsers();
    UserResponse getUserById(Long id);
    UserResponse getUserProfile(String token);
    UserResponse updateUser(UserUpdateRequest request,  Long id);
    void deleteUser(Long id);
}
