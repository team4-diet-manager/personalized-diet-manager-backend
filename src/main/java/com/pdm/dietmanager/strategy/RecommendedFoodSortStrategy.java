package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.enums.FoodGrade;
import com.pdm.dietmanager.enums.FoodSortType;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.service.FoodGradeService;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RecommendedFoodSortStrategy implements FoodSortStrategy {
    private final FoodGradeService foodGradeService;

    public RecommendedFoodSortStrategy(FoodGradeService foodGradeService) {
        this.foodGradeService = foodGradeService;
    }

    @Override
    public FoodSortType getSortType() {
        return FoodSortType.RECOMMENDED;
    }

    @Override
    public List<Food> sort(List<Food> foods, GoalType goalType) {
        if (goalType == null) {
            return foods.stream()
                    .sorted(Comparator.comparing(Food::getFoodId))
                    .toList();
        }
        return foods.stream()
                .sorted(Comparator
                        .comparingInt((Food food) -> gradeRank(foodGradeService.classify(goalType, food)))
                        .thenComparing(Food::getCalories)
                        .thenComparing(byName()))
                .toList();
    }

    private int gradeRank(FoodGrade grade) {
        return switch (grade) {
            case GREEN -> 0;
            case YELLOW -> 1;
            case RED -> 2;
        };
    }
}
