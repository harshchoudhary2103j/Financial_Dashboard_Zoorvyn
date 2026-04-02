package com.harsh.FinanceDashboard.Finance.Dashboard.dto;

import com.harsh.FinanceDashboard.Finance.Dashboard.enums.Category;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyCategorySummaryDTO {
    private int month;
    private int year;
    private Category category;
    private BigDecimal total;
}
