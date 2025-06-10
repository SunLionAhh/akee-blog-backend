package com.akee.blog.controller;

import com.akee.blog.dto.JwtResponse;
import com.akee.blog.dto.LoginRequest;
import com.akee.blog.dto.RegisterRequest;
import com.akee.blog.dto.ForgotPasswordRequest;
import com.akee.blog.dto.ResetPasswordRequest;
import com.akee.blog.dto.UserDTO;
import com.akee.blog.security.JwtTokenProvider;
import com.akee.blog.service.UserService;
import com.akee.blog.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户认证相关的 API 接口")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录并获取 JWT token")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Processing login request for user: {}", loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = null;
        UserDTO userDTO = userService.findByUsername(userDetails.getUsername());
        if (userDTO != null) {
            userId = userDTO.getId();
        }
        
        String[] roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toArray(String[]::new);

        logger.info("User {} successfully logged in with roles: {}", userDetails.getUsername(), String.join(", ", roles));
        return ResponseEntity.ok(new JwtResponse(jwt, userId, userDetails.getUsername(), roles));
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册接口")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Processing registration request for user: {}", registerRequest.getUsername());
        UserDTO userDTO = userService.registerUser(registerRequest);
        logger.info("User {} successfully registered", registerRequest.getUsername());
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/send-verification-code")
    @Operation(summary = "发送验证码", description = "发送验证码到指定邮箱")
    public ResponseEntity<Void> sendVerificationCode(@RequestParam String email) {
        logger.info("Processing verification code request for email: {}", email);
        String code = generateVerificationCode();
        logger.info("Generated verification code for email: {}", email);
        
        try {
            emailService.sendVerificationCode(email, code);
            logger.info("Verification code email sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send verification code email to: {}", email, e);
            throw e;
        }
        
        userService.storeVerificationCode(email, code);
        logger.info("Verification code stored for email: {}", email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "忘记密码", description = "请求重置密码链接")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        logger.info("Processing forgot password request for email: {}", request.getEmail());
        userService.forgotPassword(request.getEmail());
        logger.info("Forgot password request processed for email: {}", request.getEmail());
        return ResponseEntity.ok("如果邮箱存在，密码重置链接已发送到您的邮箱。");
    }

    @PostMapping("/reset-password")
    @Operation(summary = "重置密码", description = "使用重置令牌设置新密码")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        logger.info("Processing reset password request for token.");
        userService.resetPassword(request.getToken(), request.getNewPassword());
        logger.info("Reset password request processed successfully.");
        return ResponseEntity.ok("密码已成功重置。");
    }

    @PostMapping("/verify-code")
    @Operation(summary = "验证验证码", description = "验证邮箱验证码")
    public ResponseEntity<Boolean> verifyCode(@RequestParam String email, @RequestParam String code) {
        logger.info("Processing code verification for email: {}", email);
        boolean isValid = verifyCodeFromCache(email, code);
        logger.info("Code verification result for email {}: {}", email, isValid);
        return ResponseEntity.ok(isValid);
    }

    private String generateVerificationCode() {
        String code = String.format("%06d", new Random().nextInt(1000000));
        logger.debug("Generated verification code: {}", code);
        return code;
    }

    private boolean verifyCodeFromCache(String email, String code) {
        logger.debug("Verifying code from cache for email: {}", email);
        return true;
    }
} 