package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.enums.FoodSortType;
import com.pdm.dietmanager.enums.GoalType;
import java.util.Comparator;
import java.util.List;

/**
 * 식단 기록의 음식 선택 목록을 정렬하는 전략.
 * 정렬 기준이 늘어나도 구현체만 추가하면 된다(OCP).
 */
public interface FoodSortStrategy {
    FoodSortType getSortType();

    List<Food> sort(List<Food> foods, GoalType goalType);

    default Comparator<Food> byName() {
        return Comparator.comparing(Food::getName);
    }
}
