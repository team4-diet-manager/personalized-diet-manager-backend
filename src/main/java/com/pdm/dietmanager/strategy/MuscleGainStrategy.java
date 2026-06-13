package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.enums.GoalType;
import org.springframework.stereotype.Component;

@Component
public class MuscleGainStrategy implements CalorieStrategy {

    @Override
    public GoalType getGoalType() {
        return GoalType.MUSCLE_GAIN;
    }

    @Override
    public int calculate(CalorieRequest request) {
        // 근육 합성에 필요한 잉여 칼로리는 활동량에 따라 달라진다.
        // 운동 강도가 높을수록 더 많은 잉여 칼로리를 권장한다.
        double surplusRatio = switch (request.getActivityLevel()) {
            case LOW -> 1.10;
            case NORMAL -> 1.15;
            case HIGH -> 1.20;
        };

        return (int) Math.round(calculateTdee(request) * surplusRatio);
    }
}
