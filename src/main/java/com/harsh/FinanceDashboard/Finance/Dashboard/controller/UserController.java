package com.harsh.FinanceDashboard.Finance.Dashboard.controller;

import com.harsh.FinanceDashboard.Finance.Dashboard.dto.UserDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.Role;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.UserStatus;
import com.harsh.FinanceDashboard.Finance.Dashboard.service.UserService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateRole(
            @PathVariable Long id,
            @RequestParam Role role) {
        return ResponseEntity.ok(userService.updateRole(id, role));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam UserStatus status) {
        return ResponseEntity.ok(userService.updateStatus(id, status));
    }
}