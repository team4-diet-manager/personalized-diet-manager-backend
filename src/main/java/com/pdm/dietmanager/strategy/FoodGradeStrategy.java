package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.enums.FoodGrade;
import com.pdm.dietmanager.enums.GoalType;

/**
 * 목표(다이어트·벌크업·유지)별로 음식의 적합도를 신호등 등급으로 분류하는 전략.
 * 칼로리 계산({@link CalorieStrategy})에 이어 전략 패턴을 음식 분류에도 적용한 두 번째 사례다.
 */
public interface FoodGradeStrategy {
    GoalType getGoalType();

    FoodGrade classify(Food food);

    /** 음식 칼로리 중 단백질이 차지하는 비율(1g=4kcal). */
    default double proteinShare(Food food) {
        if (food.getCalories() <= 0) {
            return 0;
        }
        return (food.getProteinGrams() * 4.0) / food.getCalories();
    }

    /** 음식 칼로리 중 지방이 차지하는 비율(1g=9kcal). */
    default double fatShare(Food food) {
        if (food.getCalories() <= 0) {
            return 0;
        }
        return (food.getFatGrams() * 9.0) / food.getCalories();
    }
}
