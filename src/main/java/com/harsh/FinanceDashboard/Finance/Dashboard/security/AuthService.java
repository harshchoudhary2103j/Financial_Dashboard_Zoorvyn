package com.harsh.FinanceDashboard.Finance.Dashboard.security;

import com.harsh.FinanceDashboard.Finance.Dashboard.dto.LoginDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.dto.SignUpRequestDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.dto.UserDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.entities.User;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.Role;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.UserStatus;
import com.harsh.FinanceDashboard.Finance.Dashboard.exception.ResourceNotFoundException;
import com.harsh.FinanceDashboard.Finance.Dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private  final ModelMapper modelMapper;

    public UserDTO signup(SignUpRequestDTO signUpRequestDTO) {
        boolean exists = userRepository.existsByEmail(signUpRequestDTO.getEmail());
        if (exists) {
            throw new RuntimeException("User already exists with email: " + signUpRequestDTO.getEmail());
        }

        User newUser = User.builder()
                .name(signUpRequestDTO.getName())
                .email(signUpRequestDTO.getEmail())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .role(Role.VIEWER)          // default role
                .status(UserStatus.ACTIVE)  // default status
                .build();

        userRepository.save(newUser);
        return modelMapper.map(newUser,UserDTO.class);
    }

    public String[] login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        String[] tokens = new String[2];
        tokens[0] = jwtService.generateAccessToken(user);
        tokens[1] = jwtService.generateRefreshToken(user);
        return tokens;
    }

    public String refreshToken(String refreshToken) {
        Long id = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return jwtService.generateAccessToken(user);
    }
}