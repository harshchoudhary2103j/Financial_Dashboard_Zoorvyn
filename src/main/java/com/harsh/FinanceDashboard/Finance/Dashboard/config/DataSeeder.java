package com.harsh.FinanceDashboard.Finance.Dashboard.config;

import com.harsh.FinanceDashboard.Finance.Dashboard.entities.User;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.Role;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.UserStatus;
import com.harsh.FinanceDashboard.Finance.Dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByEmail("admin@finance.com")) return;

        User admin = User.builder()
                .name("Super Admin")
                .email("admin@finance.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(admin);
        System.out.println("Default admin created: admin@finance.com / admin123");
    }
}
