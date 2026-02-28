package com.example.bankcards.service;

import com.example.bankcards.dto.UpdateRoleRequestDto;
import com.example.bankcards.dto.UserResponseDto;
import com.example.bankcards.dto.mappers.UserMapper;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AccessDeniedException;
import com.example.bankcards.exception.UserNotFoundException;
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
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = getUserOrThrow(id);
        return userMapper.toDto(user);
    }

    @Override
    public void updateUserRole(Long id, UpdateRoleRequestDto request) {

        User user = getUserOrThrow(id);

        Role newRole;
        try {
            newRole = Role.valueOf(request.getRole().toString().toUpperCase());
        } catch (Exception e) {
            throw new AccessDeniedException("Invalid role");
        }

        user.setRole(newRole);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserOrThrow(id);
        if (user.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Invalid role");
        }
        userRepository.delete(user);
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}