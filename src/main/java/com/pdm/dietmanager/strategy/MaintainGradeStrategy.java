package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.enums.FoodGrade;
import com.pdm.dietmanager.enums.GoalType;
import org.springframework.stereotype.Component;

@Component
public class MaintainGradeStrategy implements FoodGradeStrategy {
    @Override
    public GoalType getGoalType() {
        return GoalType.MAINTAIN;
    }

    @Override
    public FoodGrade classify(Food food) {
        // 체중 유지: 지방 비중이 낮고 칼로리가 과하지 않은 균형 잡힌 음식을 권장한다.
        if (food.getCalories() >= 500 || fatShare(food) >= 0.50) {
            return FoodGrade.RED;
        }
        if (food.getCalories() <= 350 && fatShare(food) <= 0.35) {
            return FoodGrade.GREEN;
        }
        return FoodGrade.YELLOW;
    }
}
