package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.enums.FoodGrade;
import com.pdm.dietmanager.enums.GoalType;
import org.springframework.stereotype.Component;

@Component
public class MuscleGainGradeStrategy implements FoodGradeStrategy {
    @Override
    public GoalType getGoalType() {
        return GoalType.MUSCLE_GAIN;
    }

    @Override
    public FoodGrade classify(Food food) {
        // 벌크업: 근성장에 필요한 단백질이 풍부하고 에너지가 충분한 음식을 권장한다.
        // 단백질이 거의 없는 고지방 음식은 주의로 분류한다.
        if (proteinShare(food) >= 0.25 && food.getCalories() >= 150) {
            return FoodGrade.GREEN;
        }
        if (proteinShare(food) < 0.10 && fatShare(food) >= 0.40) {
            return FoodGrade.RED;
        }
        return FoodGrade.YELLOW;
    }
}
