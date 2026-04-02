package com.harsh.FinanceDashboard.Finance.Dashboard.dto;


import com.harsh.FinanceDashboard.Finance.Dashboard.enums.Role;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;
}
