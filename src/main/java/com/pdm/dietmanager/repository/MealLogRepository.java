package com.pdm.dietmanager.repository;

import com.pdm.dietmanager.entity.MealLog;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * MealLog 엔티티의 데이터베이스 접근 인터페이스.
 */
public interface MealLogRepository extends JpaRepository<MealLog, Long> {
    List<MealLog> findByUserProfile_ProfileIdAndMealDate(Long profileId, LocalDate mealDate);

    @Query("""
            SELECT COALESCE(SUM(m.totalCalories), 0)
            FROM MealLog m
            WHERE m.userProfile.profileId = :profileId
              AND m.mealDate = :mealDate
            """)
    Long sumTotalCaloriesByProfileIdAndMealDate(
            @Param("profileId") Long profileId,
            @Param("mealDate") LocalDate mealDate
    );
}
