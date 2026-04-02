package com.harsh.FinanceDashboard.Finance.Dashboard.controller;

import com.harsh.FinanceDashboard.Finance.Dashboard.dto.*;
import com.harsh.FinanceDashboard.Finance.Dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ModelMapper modelMapper;

    // All roles
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    // All roles
    @GetMapping("/by-category")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<List<CategorySummaryDTO>> getCategoryWiseTotals() {
        return ResponseEntity.ok(dashboardService.getCategoryWiseTotals());
    }

    // Analyst + Admin only
    @GetMapping("/trends")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<List<MonthlyTrendDTO>> getMonthlyTrends() {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends());
    }

    // All roles
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

    @GetMapping("/by-category/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<List<MonthlyCategorySummaryDTO>> getCategoryWiseTotalsPerMonth() {
        return ResponseEntity.ok(dashboardService.getCategoryWiseTotalsPerMonth());
    }
}
