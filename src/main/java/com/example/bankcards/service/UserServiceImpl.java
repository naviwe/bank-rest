package com.example.bankcards.service;

import com.example.bankcards.dto.UpdateRoleRequestDto;
import com.example.bankcards.dto.UserResponseDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = getUserOrThrow(id);
        return mapToDto(user);
    }

    @Override
    public void updateUserRole(Long id, UpdateRoleRequestDto request) {

        User user = getUserOrThrow(id);

        Role newRole;
        try {
            newRole = Role.valueOf(request.getRole().toString().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid role");
        }

        user.setRole(newRole);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserOrThrow(id);

        userRepository.delete(user);
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserResponseDto mapToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}