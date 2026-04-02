package com.harsh.FinanceDashboard.Finance.Dashboard.dto;

import com.harsh.FinanceDashboard.Finance.Dashboard.enums.Category;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialRecordResponseDTO {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private Category category;
    private LocalDate date;
    private String notes;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
