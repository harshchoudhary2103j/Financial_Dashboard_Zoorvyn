package com.harsh.FinanceDashboard.Finance.Dashboard.service;

import com.harsh.FinanceDashboard.Finance.Dashboard.dto.*;
import com.harsh.FinanceDashboard.Finance.Dashboard.entities.FinancialRecord;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.*;
import com.harsh.FinanceDashboard.Finance.Dashboard.repository.FinancialRecordRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinancialRecordRepository financialRecordRepository;

    private int extractInt(Object value) {
        if (value instanceof Double) return ((Double) value).intValue();
        if (value instanceof Long) return ((Long) value).intValue();
        if (value instanceof Integer) return (Integer) value;
        return ((Number) value).intValue();
    }

    // All roles - basic summary
    public DashboardSummaryDTO getSummary() {
        BigDecimal totalIncome = financialRecordRepository.sumByType(TransactionType.INCOME);
        BigDecimal totalExpenses = financialRecordRepository.sumByType(TransactionType.EXPENSE);
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        return DashboardSummaryDTO.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(netBalance)
                .build();
    }

    // All roles - category wise totals
    public List<CategorySummaryDTO> getCategoryWiseTotals() {
        List<Object[]> results = financialRecordRepository.sumByCategory();
        return results.stream()
                .map(row -> CategorySummaryDTO.builder()
                        .category((Category) row[0])
                        .total((BigDecimal) row[1])
                        .build())
                .toList();
    }

    // Analyst + Admin - monthly trends
    public List<MonthlyTrendDTO> getMonthlyTrends() {
        List<Object[]> results = financialRecordRepository.monthlyTrends();
        return results.stream()
                .map(row -> MonthlyTrendDTO.builder()
                        .month(extractInt(row[0]))
                        .year(extractInt(row[1]))
                        .type((TransactionType) row[2])
                        .total((BigDecimal) row[3])
                        .build())
                .toList();
    }

    // All roles - recent activity
    public List<FinancialRecord> getRecentActivity() {
        return financialRecordRepository.findTop10ByIsDeletedFalseOrderByCreatedAtDesc();
    }

    public List<MonthlyCategorySummaryDTO> getCategoryWiseTotalsPerMonth() {
        List<Object[]> results = financialRecordRepository.sumByCategoryPerMonth();
        return results.stream()
                .map(row -> MonthlyCategorySummaryDTO.builder()
                        .month(extractInt(row[0]))
                        .year(extractInt(row[1]))
                        .category((Category) row[2])
                        .total((BigDecimal) row[3])
                        .build())
                .toList();
    }
}
