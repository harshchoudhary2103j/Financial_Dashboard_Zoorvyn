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

    @Query("SELECT f FROM FinancialRecord f WHERE f.isDeleted = false " +
            "AND (:type IS NULL OR f.type = :type) " +
            "AND (:category IS NULL OR f.category = :category) " +
            "AND (:startDate IS NULL OR f.date >= :startDate) " +
            "AND (:endDate IS NULL OR f.date <= :endDate)")
    Page<FinancialRecord> findAllWithFilters(
            @Param("type") TransactionType type,
            @Param("category") Category category,
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
    @Query("SELECT EXTRACT(MONTH FROM f.date), EXTRACT(YEAR FROM f.date), f.type, SUM(f.amount) " +
            "FROM FinancialRecord f WHERE f.isDeleted = false " +
            "GROUP BY EXTRACT(YEAR FROM f.date), EXTRACT(MONTH FROM f.date), f.type " +
            "ORDER BY EXTRACT(YEAR FROM f.date), EXTRACT(MONTH FROM f.date)")
    List<Object[]> monthlyTrends();

    // Category wise totals per month
    @Query("SELECT EXTRACT(MONTH FROM f.date), EXTRACT(YEAR FROM f.date), f.category, SUM(f.amount) " +
            "FROM FinancialRecord f WHERE f.isDeleted = false " +
            "GROUP BY EXTRACT(YEAR FROM f.date), EXTRACT(MONTH FROM f.date), f.category " +
            "ORDER BY EXTRACT(YEAR FROM f.date), EXTRACT(MONTH FROM f.date)")
    List<Object[]> sumByCategoryPerMonth();

    // Recent records
    List<FinancialRecord> findTop10ByIsDeletedFalseOrderByCreatedAtDesc();

    // Get all soft deleted records
    @Query("SELECT f FROM FinancialRecord f WHERE f.isDeleted = true")
    List<FinancialRecord> findAllDeleted();
}
