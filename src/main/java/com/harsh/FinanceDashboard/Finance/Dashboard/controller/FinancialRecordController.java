package com.harsh.FinanceDashboard.Finance.Dashboard.controller;

import com.harsh.FinanceDashboard.Finance.Dashboard.dto.FinancialRecordRequestDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.dto.FinancialRecordResponseDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.entities.User;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.Category;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.TransactionType;
import com.harsh.FinanceDashboard.Finance.Dashboard.service.FinancialRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinancialRecordResponseDTO> createRecord(
            @RequestBody @Valid FinancialRecordRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(financialRecordService.createRecord(requestDTO, currentUser.getId()));
    }

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<FinancialRecordResponseDTO> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(financialRecordService.getRecordById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinancialRecordResponseDTO> updateRecord(
            @PathVariable Long id,
            @RequestBody @Valid FinancialRecordRequestDTO requestDTO) {
        return ResponseEntity.ok(financialRecordService.updateRecord(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        financialRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FinancialRecordResponseDTO>> getDeletedRecords() {
        return ResponseEntity.ok(financialRecordService.getDeletedRecords());
    }
}