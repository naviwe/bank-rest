package com.example.bankcards.service;

import com.example.bankcards.dto.UpdateRoleRequestDto;
import com.example.bankcards.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserResponseDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserById(Long id);

    void updateUserRole(Long id, UpdateRoleRequestDto request);

    void deleteUser(Long id);
}