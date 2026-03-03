package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.dto.UpdateRoleRequestDto;
import com.example.bankcards.dto.UserResponseDto;
import com.example.bankcards.service.CardAdminService;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final CardAdminService  cardAdminService;

    @PostMapping("/cards/{userId}")
    public CardResponseDto createCard(@PathVariable Long userId) {
        return cardAdminService.createCard(userId);
    }

    @PatchMapping("/cards/{cardId}/block")
    public void blockCard(@PathVariable Long cardId) {
        cardAdminService.blockCard(cardId);
    }

    @PatchMapping("/cards/{cardId}/activate")
    public void activateCard(@PathVariable Long cardId) {
        cardAdminService.activateCard(cardId);
    }

    @DeleteMapping("/cards/{cardId}")
    public void deleteCard(@PathVariable Long cardId) {
        cardAdminService.deleteCard(cardId);
    }

    @GetMapping("/cards")
    public Page<CardResponseDto> getAllCards(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return cardAdminService.getAllCards(pageable);
    }

    @GetMapping("/users")
    public Page<UserResponseDto> getAllUsers(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/users/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/users/{id}/role")
    public void updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequestDto request
    ) {
        userService.updateUserRole(id, request);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}