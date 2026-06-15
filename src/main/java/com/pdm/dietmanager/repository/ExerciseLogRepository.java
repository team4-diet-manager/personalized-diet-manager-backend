package com.pdm.dietmanager.repository;

import com.pdm.dietmanager.entity.ExerciseLog;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * ExerciseLog 엔티티의 데이터베이스 접근 인터페이스.
 */
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    List<ExerciseLog> findByUserProfile_ProfileIdAndExerciseDate(Long profileId, LocalDate exerciseDate);

    @Query("""
            SELECT COALESCE(SUM(e.burnedCalories), 0)
            FROM ExerciseLog e
            WHERE e.userProfile.profileId = :profileId
              AND e.exerciseDate = :exerciseDate
            """)
    Long sumBurnedCaloriesByProfileIdAndExerciseDate(
            @Param("profileId") Long profileId,
            @Param("exerciseDate") LocalDate exerciseDate
    );
}
