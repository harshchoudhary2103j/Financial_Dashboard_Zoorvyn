package com.harsh.FinanceDashboard.Finance.Dashboard.dto;


import com.harsh.FinanceDashboard.Finance.Dashboard.enums.Category;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialRecordRequestDTO {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Type is required")
    private TransactionType type;

    @NotNull(message = "Category is required")
    private Category category;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private String notes;
}
