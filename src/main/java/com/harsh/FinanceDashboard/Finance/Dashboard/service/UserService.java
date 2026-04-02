package com.harsh.FinanceDashboard.Finance.Dashboard.service;

import com.harsh.FinanceDashboard.Finance.Dashboard.dto.UserDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.entities.User;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.Role;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.UserStatus;
import com.harsh.FinanceDashboard.Finance.Dashboard.exception.ResourceNotFoundException;
import com.harsh.FinanceDashboard.Finance.Dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User Not found with id: "+id));
    }
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }
    public UserDTO updateRole(Long id, Role role) {
        User user = getUserById(id);

        // Check if same role
        if (user.getRole() == role) {
            throw new RuntimeException("User already has the role: " + role);
        }

        user.setRole(role);
        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserDTO.class);
    }

    public UserDTO updateStatus(Long id, UserStatus status) {
        User user = getUserById(id);


        if (user.getStatus() == status) {
            throw new RuntimeException("User already has the status: " + status);
        }


        if (user.getRole() == Role.ADMIN && status == UserStatus.INACTIVE) {
            throw new RuntimeException("Cannot deactivate an Admin user");
        }

        user.setStatus(status);
        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserDTO.class);
    }


}

