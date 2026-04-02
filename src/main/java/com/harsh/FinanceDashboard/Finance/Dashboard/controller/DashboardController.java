package com.harsh.FinanceDashboard.Finance.Dashboard.controller;

import com.harsh.FinanceDashboard.Finance.Dashboard.dto.*;
import com.harsh.FinanceDashboard.Finance.Dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Summary and analytics APIs for finance dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Get financial summary", description = "Analyst and Admin only - total income, expenses and net balance")
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @Operation(summary = "Get category wise totals", description = "All roles - total amount grouped by category")
    @GetMapping("/by-category")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<List<CategorySummaryDTO>> getCategoryWiseTotals() {
        return ResponseEntity.ok(dashboardService.getCategoryWiseTotals());
    }

    @Operation(summary = "Get monthly trends", description = "Analyst and Admin only - month wise income and expense breakdown")
    @GetMapping("/trends")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<List<MonthlyTrendDTO>> getMonthlyTrends() {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends());
    }

    @Operation(summary = "Get recent activity", description = "All roles - last 10 transactions")
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<List<FinancialRecordResponseDTO>> getRecentActivity() {
        return ResponseEntity.ok(
                dashboardService.getRecentActivity()
                        .stream()
                        .map(record -> {
                            FinancialRecordResponseDTO dto = modelMapper.map(record, FinancialRecordResponseDTO.class);
                            dto.setCreatedByName(record.getCreatedBy().getName());
                            return dto;
                        })
                        .toList()
        );
    }

    @Operation(summary = "Get category wise totals per month", description = "All roles - category breakdown for each month")
    @GetMapping("/by-category/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<List<MonthlyCategorySummaryDTO>> getCategoryWiseTotalsPerMonth() {
        return ResponseEntity.ok(dashboardService.getCategoryWiseTotalsPerMonth());
    }
}