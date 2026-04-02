package com.harsh.FinanceDashboard.Finance.Dashboard.dto;


import com.harsh.FinanceDashboard.Finance.Dashboard.enums.Category;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorySummaryDTO {
    private Category category;
    private BigDecimal total;
}