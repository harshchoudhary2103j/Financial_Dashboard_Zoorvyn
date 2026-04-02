package com.harsh.FinanceDashboard.Finance.Dashboard.service;
// Service
import com.harsh.FinanceDashboard.Finance.Dashboard.dto.FinancialRecordRequestDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.dto.FinancialRecordResponseDTO;
import com.harsh.FinanceDashboard.Finance.Dashboard.repository.FinancialRecordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.*;
import com.harsh.FinanceDashboard.Finance.Dashboard.entities.*;
import com.harsh.FinanceDashboard.Finance.Dashboard.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinancialRecordService {

    private final FinancialRecordRepository financialRecordRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    // Admin only
    public FinancialRecordResponseDTO createRecord(FinancialRecordRequestDTO requestDTO, Long userId) {
        User createdBy = userService.getUserById(userId);
        FinancialRecord record = modelMapper.map(requestDTO, FinancialRecord.class);
        record.setCreatedBy(createdBy);
        record.setIsDeleted(false);
        FinancialRecord saved = financialRecordRepository.save(record);
        return mapToResponseDTO(saved);
    }

    // All roles - with filters + pagination
    public Page<FinancialRecordResponseDTO> getAllRecords(TransactionType type, Category category,
                                                          LocalDate startDate, LocalDate endDate,
                                                          Pageable pageable) {
        return financialRecordRepository
                .findAllWithFilters(type, category, startDate, endDate, pageable)
                .map(this::mapToResponseDTO);
    }

    // All roles
    public FinancialRecordResponseDTO getRecordById(Long id) {
        FinancialRecord record = findActiveRecord(id);
        return mapToResponseDTO(record);
    }

    // Admin only
    public FinancialRecordResponseDTO updateRecord(Long id, FinancialRecordRequestDTO requestDTO) {
        FinancialRecord record = findActiveRecord(id);
        modelMapper.map(requestDTO, record);
        FinancialRecord saved = financialRecordRepository.save(record);
        return mapToResponseDTO(saved);
    }

    // Admin only - soft delete
    public void deleteRecord(Long id) {
        FinancialRecord record = findActiveRecord(id);
        record.setIsDeleted(true);
        financialRecordRepository.save(record);
    }

    // Helper - find non deleted record
    private FinancialRecord findActiveRecord(Long id) {
        FinancialRecord record = financialRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        if (record.getIsDeleted()) {
            throw new ResourceNotFoundException("Record not found with id: " + id);
        }
        return record;
    }

    // Helper - map to response DTO
    private FinancialRecordResponseDTO mapToResponseDTO(FinancialRecord record) {
        FinancialRecordResponseDTO dto = modelMapper.map(record, FinancialRecordResponseDTO.class);
        dto.setCreatedByName(record.getCreatedBy().getName());
        return dto;
    }
}
