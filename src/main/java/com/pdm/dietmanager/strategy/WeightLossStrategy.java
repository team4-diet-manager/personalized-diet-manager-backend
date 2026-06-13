package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.enums.GoalType;
import org.springframework.stereotype.Component;

@Component
public class WeightLossStrategy implements CalorieStrategy {
    private static final double DEFICIT_RATIO = 0.8;

    @Override
    public GoalType getGoalType() {
        return GoalType.WEIGHT_LOSS;
    }

    @Override
    public int calculate(CalorieRequest request) {
        double targetCalories = calculateTdee(request) * DEFICIT_RATIO;
        double basalFloor = calculateBmr(request);

        // 감량을 위해 칼로리를 줄이더라도 기초대사량(BMR) 미만으로는 권장하지 않는다.
        // 활동량이 낮은 사용자는 단순 배율만 적용하면 BMR 아래로 떨어질 수 있어 하한선을 둔다.
        return (int) Math.round(Math.max(targetCalories, basalFloor));
    }
}
