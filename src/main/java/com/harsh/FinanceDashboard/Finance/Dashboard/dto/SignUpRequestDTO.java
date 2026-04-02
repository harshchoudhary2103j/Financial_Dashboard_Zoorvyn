package com.harsh.FinanceDashboard.Finance.Dashboard.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpRequestDTO {
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 6)
    private String password;
}
