package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.enums.FoodGrade;
import com.pdm.dietmanager.enums.GoalType;
import org.springframework.stereotype.Component;

@Component
public class WeightLossGradeStrategy implements FoodGradeStrategy {
    @Override
    public GoalType getGoalType() {
        return GoalType.WEIGHT_LOSS;
    }

    @Override
    public FoodGrade classify(Food food) {
        // 다이어트: 저칼로리·고단백을 권장하고, 고칼로리·고지방은 주의로 분류한다.
        if (food.getCalories() <= 200 && proteinShare(food) >= 0.25) {
            return FoodGrade.GREEN;
        }
        if (food.getCalories() >= 400 || fatShare(food) >= 0.45) {
            return FoodGrade.RED;
        }
        return FoodGrade.YELLOW;
    }
}
