package com.confsync.service;

import com.confsync.dto.AuthRequest;
import com.confsync.dto.AuthResponse;
import com.confsync.dto.RegisterRequest;
import com.confsync.dto.UserResponse;
import com.confsync.entity.User;
import com.confsync.enums.Role;

import java.util.List;

public interface UserService {
    AuthResponse login(AuthRequest request);
    UserResponse register(RegisterRequest request);
    UserResponse getCurrentUser();
    User getCurrentUserEntity();
    User getUserById(Long id);
    List<UserResponse> getAllUsers();
    List<UserResponse> getUsersByRole(Role role);
    UserResponse updateUserRole(Long userId, Role newRole);
    void deleteUser(Long userId);
}

