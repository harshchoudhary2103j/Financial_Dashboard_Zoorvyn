package com.harsh.FinanceDashboard.Finance.Dashboard.controller;

import com.harsh.FinanceDashboard.Finance.Dashboard.dto.LoginDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.dto.LoginResponseDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.dto.SignUpRequestDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.dto.UserDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.security.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Signup, Login and Refresh token APIs")
public class AuthController {

    private final AuthService authService;
    @Operation(summary = "Register a new user", description = "Default role is VIEWER")
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        return new ResponseEntity<>(authService.signup(signUpRequestDTO), HttpStatus.CREATED);
    }
    @Operation(summary = "Login user", description = "Returns access token, sets refresh token in cookie")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
        String[] tokens = authService.login(loginDTO);
        Cookie cookie = new Cookie("refreshToken", tokens[1]);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(new LoginResponseDTO(tokens[0]));
    }
    @Operation(summary = "Refresh access token", description = "Uses refresh token from cookie")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found in cookies"));

        String accessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new LoginResponseDTO(accessToken));
    }
}