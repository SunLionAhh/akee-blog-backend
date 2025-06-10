package com.akee.blog.controller;

import com.akee.blog.dto.UserDTO;
import com.akee.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户相关的 API 接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "创建用户", description = "创建一个新的用户")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "根据用户 ID 更新用户信息")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "用户 ID") @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户 ID 删除用户")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "用户 ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户", description = "根据用户 ID 获取用户信息")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "用户 ID") @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    @Operation(summary = "获取所有用户", description = "分页获取所有用户信息")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名获取用户", description = "根据用户名获取用户信息")
    public ResponseEntity<UserDTO> getUserByUsername(
            @Parameter(description = "用户名") @PathVariable String username) {
        UserDTO userDTO = userService.findByUsername(username);
        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDTO);
    }
} 