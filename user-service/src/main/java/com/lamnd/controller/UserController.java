package com.lamnd.controller;

import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.dto.request.UserCreateRequest;
import com.lamnd.dto.request.UserUpdateRequest;
import com.lamnd.dto.response.UserResponse;
import com.lamnd.entity.User;
import com.lamnd.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController extends BaseController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createUser(@RequestBody @Valid UserCreateRequest request) {
        userService.createUser(request);
        return new ResponseEntity<>(createSuccessResponse("User created successfully"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getUsers() {
        List<UserResponse> users = userService.getUsers();

        return ResponseEntity.ok(createSuccessResponse(users));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<?>> getMyInfo() {
        UserResponse user = userService.getMyInfo();

        return ResponseEntity.ok(createSuccessResponse(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable("id") Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(createSuccessResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateRequest request) {
        UserResponse user = userService.updateUser(request, id);

        return ResponseEntity.ok(createSuccessResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
