package com.akee.blog.service.impl;

import com.akee.blog.dto.UserDTO;
import com.akee.blog.entity.User;
import com.akee.blog.mapper.UserMapper;
import com.akee.blog.service.UserService;
import com.akee.blog.service.EmailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.akee.blog.dto.RegisterRequest;
import org.springframework.data.redis.core.RedisTemplate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        baseMapper.insert(user);
        return convertToDTO(user);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = baseMapper.selectById(id);
        if (user == null) throw new RuntimeException("User not found");
        BeanUtils.copyProperties(userDTO, user, "password");
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        baseMapper.updateById(user);
        return convertToDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = baseMapper.selectById(id);
        return user != null ? convertToDTO(user) : null;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return baseMapper.selectList(null).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = baseMapper.selectOne(wrapper);
        return user != null ? convertToDTO(user) : null;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = baseMapper.selectOne(wrapper);
        return user != null ? convertToDTO(user) : null;
    }

    @Override
    public boolean existsByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return baseMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        return baseMapper.selectCount(wrapper) > 0;
    }

    @Override
    public void updateAvatar(Long id, String avatarUrl) {
        User user = baseMapper.selectById(id);
        if (user != null) {
            user.setAvatar(avatarUrl);
            baseMapper.updateById(user);
        }
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        User user = baseMapper.selectById(id);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            baseMapper.updateById(user);
        }
    }

    @Override
    public UserDTO registerUser(RegisterRequest request) {
        if (!verifyCodeFromCache(request.getEmail(), request.getVerificationCode())) {
            throw new RuntimeException("验证码错误");
        }
        if (existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        baseMapper.insert(user);
        return convertToDTO(user);
    }

    @Override
    public void forgotPassword(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = baseMapper.selectOne(wrapper);
        if (user == null) throw new RuntimeException("邮箱未注册");
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordExpires(LocalDateTime.now().plusHours(24));
        baseMapper.updateById(user);
        String resetUrl = "http://localhost:8081/reset-password?token=" + token;
        emailService.sendResetPasswordEmail(user.getEmail(), resetUrl);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getResetPasswordToken, token);
        User user = baseMapper.selectOne(wrapper);
        if (user == null || user.getResetPasswordExpires().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("无效或已过期的重置令牌");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpires(null);
        baseMapper.updateById(user);
    }

    private boolean verifyCodeFromCache(String email, String code) {
        String cachedCode = redisTemplate.opsForValue().get("verification_code:" + email);
        if (cachedCode == null || !cachedCode.equals(code)) {
            return false;
        }
        redisTemplate.delete("verification_code:" + email);
        return true;
    }

    @Override
    public void storeVerificationCode(String email, String code) {
        String key = "verification_code:" + email;
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
    }

    @Override
    public String getVerificationCode(String email) {
        String key = "verification_code:" + email;
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String cachedCode = getVerificationCode(email);
        return cachedCode != null && cachedCode.equals(code);
    }

    @Override
    public UserDTO findByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = baseMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToDTO(user);
    }

    @Override
    public UserDTO findByEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = baseMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToDTO(user);
    }

    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        baseMapper.updateById(user);
    }

    @Override
    public void setResetPasswordToken(String email) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>()
            .eq(User::getEmail, email));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 生成重置密码令牌
        String token = UUID.randomUUID().toString();
        LocalDateTime expires = LocalDateTime.now().plusHours(24);

        // 更新用户信息
        user.setResetPasswordToken(token);
        user.setResetPasswordExpires(expires);
        baseMapper.updateById(user);

        // 发送重置密码邮件
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;
        emailService.sendResetPasswordEmail(email, resetUrl);
    }

    @Override
    public IPage<UserDTO> getAllUsers(com.baomidou.mybatisplus.extension.plugins.pagination.Page<User> page) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getCreatedAt);
        return baseMapper.selectPage(page, wrapper).convert(this::convertToDTO);
    }


    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        // 清除敏感信息
        userDTO.setPassword(null);
        return userDTO;
    }
}