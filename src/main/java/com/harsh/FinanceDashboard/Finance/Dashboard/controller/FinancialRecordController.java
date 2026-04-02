package com.harsh.FinanceDashboard.Finance.Dashboard.controller;

import com.harsh.FinanceDashboard.Finance.Dashboard.dto.FinancialRecordRequestDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.dto.FinancialRecordResponseDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.entities.User;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.Category;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.TransactionType;
import com.harsh.FinanceDashboard.Finance.Dashboard.service.FinancialRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
@Tag(name = "Financial Records", description = "Manage financial records - CRUD and search")
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;

    @Operation(summary = "Create a new record", description = "Admin only")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinancialRecordResponseDTO> createRecord(
            @RequestBody @Valid FinancialRecordRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(financialRecordService.createRecord(requestDTO, currentUser.getId()));
    }

    @Operation(summary = "Get all records", description = "All roles - supports filters and pagination")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<Page<FinancialRecordResponseDTO>> getAllRecords(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return ResponseEntity.ok(
                financialRecordService.getAllRecords(type, category, startDate, endDate, pageable)
        );
    }

    @Operation(summary = "Get record by ID", description = "All roles")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<FinancialRecordResponseDTO> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(financialRecordService.getRecordById(id));
    }

    @Operation(summary = "Update a record", description = "Admin only")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinancialRecordResponseDTO> updateRecord(
            @PathVariable Long id,
            @RequestBody @Valid FinancialRecordRequestDTO requestDTO) {
        return ResponseEntity.ok(financialRecordService.updateRecord(id, requestDTO));
    }

    @Operation(summary = "Soft delete a record", description = "Admin only")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        financialRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all soft deleted records", description = "Admin only - view soft deleted records")
    @GetMapping("/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FinancialRecordResponseDTO>> getDeletedRecords() {
        return ResponseEntity.ok(financialRecordService.getDeletedRecords());
    }

    @Operation(summary = "Search records", description = "All roles - search by keyword in notes or category")
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<Page<FinancialRecordResponseDTO>> searchRecords(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return ResponseEntity.ok(financialRecordService.searchRecords(keyword, pageable));
    }
}