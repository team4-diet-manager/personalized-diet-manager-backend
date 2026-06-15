package com.pdm.dietmanager.repository;

import com.pdm.dietmanager.entity.WeightLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * WeightLog 엔티티의 데이터베이스 접근 인터페이스.
 */
public interface WeightLogRepository extends JpaRepository<WeightLog, Long> {
    List<WeightLog> findByUserProfile_ProfileIdOrderByLogDateAsc(Long profileId);

    Optional<WeightLog> findByUserProfile_ProfileIdAndLogDate(Long profileId, LocalDate logDate);
}
