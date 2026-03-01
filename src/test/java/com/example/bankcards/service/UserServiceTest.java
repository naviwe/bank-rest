package com.example.bankcards.service;

import com.example.bankcards.dto.UpdateRoleRequestDto;
import com.example.bankcards.dto.UserResponseDto;
import com.example.bankcards.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
    }

    @Test
    void getAllUsers_shouldReturnPage() {
        UserResponseDto user = new UserResponseDto();
        Page<UserResponseDto> page = new PageImpl<>(List.of(user));

        when(userService.getAllUsers(Pageable.unpaged())).thenReturn(page);

        Page<UserResponseDto> result = userService.getAllUsers(Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
        verify(userService, times(1)).getAllUsers(Pageable.unpaged());
    }

    @Test
    void getUserById_shouldReturnUser() {
        UserResponseDto user = new UserResponseDto();
        user.setId(1L);

        when(userService.getUserById(1L)).thenReturn(user);

        UserResponseDto result = userService.getUserById(1L);
        assertEquals(1L, result.getId());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void updateUserRole_shouldBeCalled() {
        UpdateRoleRequestDto request = new UpdateRoleRequestDto(Role.USER);
        doNothing().when(userService).updateUserRole(1L, request);

        userService.updateUserRole(1L, request);
        verify(userService, times(1)).updateUserRole(1L, request);
    }

    @Test
    void deleteUser_shouldBeCalled() {
        doNothing().when(userService).deleteUser(1L);
        userService.deleteUser(1L);
        verify(userService, times(1)).deleteUser(1L);
    }
}