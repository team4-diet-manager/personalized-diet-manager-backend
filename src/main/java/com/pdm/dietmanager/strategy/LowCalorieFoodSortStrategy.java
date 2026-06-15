package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.enums.FoodSortType;
import com.pdm.dietmanager.enums.GoalType;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LowCalorieFoodSortStrategy implements FoodSortStrategy {
    @Override
    public FoodSortType getSortType() {
        return FoodSortType.LOW_CALORIE;
    }

    @Override
    public List<Food> sort(List<Food> foods, GoalType goalType) {
        return foods.stream()
                .sorted(Comparator.comparingInt(Food::getCalories).thenComparing(byName()))
                .toList();
    }
}
