package com.pdm.dietmanager.repository;

import com.pdm.dietmanager.entity.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MealLog 엔티티의 데이터베이스 접근 인터페이스.
 */
public interface MealLogRepository extends JpaRepository<MealLog, Long> {
}
