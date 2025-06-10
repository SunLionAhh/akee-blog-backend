package com.akee.blog.service;

import com.akee.blog.dto.UserDTO;
import com.akee.blog.dto.RegisterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO getUserById(Long id);
    Page<UserDTO> getAllUsers(Pageable pageable);
    UserDTO findByUsername(String username);
    UserDTO registerUser(RegisterRequest request);
    void storeVerificationCode(String email, String code);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
} 