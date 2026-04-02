package com.harsh.FinanceDashboard.Finance.Dashboard.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.harsh.FinanceDashboard.Finance.Dashboard.entities.*;
import com.harsh.FinanceDashboard.Finance.Dashboard.enums.*;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    // Fetch all non-deleted records with filters
    @Query("SELECT f FROM FinancialRecord f WHERE f.isDeleted = false " +
            "AND (:type IS NULL OR f.type = :type) " +
            "AND (:category IS NULL OR f.category = :category) " +
            "AND (:startDate IS NULL OR f.date >= :startDate) " +
            "AND (:endDate IS NULL OR f.date <= :endDate)")
    Page<FinancialRecord> findAllWithFilters(
            @Param("type") TransactionType type,
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    // For dashboard summary
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f " +
            "WHERE f.isDeleted = false AND f.type = :type")
    BigDecimal sumByType(@Param("type") TransactionType type);

    // Category wise totals
    @Query("SELECT f.category, SUM(f.amount) FROM FinancialRecord f " +
            "WHERE f.isDeleted = false GROUP BY f.category")
    List<Object[]> sumByCategory();

    // Monthly trends
    @Query("SELECT MONTH(f.date), YEAR(f.date), f.type, SUM(f.amount) " +
            "FROM FinancialRecord f WHERE f.isDeleted = false " +
            "GROUP BY YEAR(f.date), MONTH(f.date), f.type " +
            "ORDER BY YEAR(f.date), MONTH(f.date)")
    List<Object[]> monthlyTrends();

    // Recent records
    List<FinancialRecord> findTop10ByIsDeletedFalseOrderByCreatedAtDesc();
}
