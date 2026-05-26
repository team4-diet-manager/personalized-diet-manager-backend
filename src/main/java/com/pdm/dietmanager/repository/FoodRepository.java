package com.pdm.dietmanager.repository;

import com.pdm.dietmanager.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Food 엔티티의 데이터베이스 접근 인터페이스
 */
public interface FoodRepository extends JpaRepository<Food, Long> {
}
