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

    @Override
    public MacroRatio macroRatio() {
        // 벌크업: 운동 수행과 회복에 필요한 탄수화물 비중을 높인 고탄수 구성.
        return new MacroRatio(0.30, 0.50, 0.20);
    }
}
