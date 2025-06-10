package com.akee.blog.service;

import com.akee.blog.dto.UserDTO;
import com.akee.blog.dto.RegisterRequest;
import com.akee.blog.entity.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO getUserByUsername(String username);
    UserDTO getUserByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void updateAvatar(Long id, String avatarUrl);
    void updatePassword(Long id, String newPassword);
    UserDTO registerUser(RegisterRequest request);
    void storeVerificationCode(String email, String code);
    String getVerificationCode(String email);
    boolean verifyCode(String email, String code);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
    UserDTO findByUsername(String username);
    UserDTO findByEmail(String email);
    void changePassword(Long id, String oldPassword, String newPassword);
    void setResetPasswordToken(String email);
    IPage<UserDTO> getAllUsers(Page<User> page);
} 