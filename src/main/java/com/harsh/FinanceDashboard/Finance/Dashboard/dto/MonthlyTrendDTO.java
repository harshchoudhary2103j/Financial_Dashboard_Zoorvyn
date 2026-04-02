package com.harsh.FinanceDashboard.Finance.Dashboard.dto;

import com.harsh.FinanceDashboard.Finance.Dashboard.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyTrendDTO {
    private int month;
    private int year;
    private TransactionType type;
    private BigDecimal total;
}