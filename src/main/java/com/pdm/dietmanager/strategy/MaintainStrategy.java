package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.enums.GoalType;
import org.springframework.stereotype.Component;

@Component
public class MaintainStrategy implements CalorieStrategy {
    @Override
    public GoalType getGoalType() {
        return GoalType.MAINTAIN;
    }

    @Override
    public int calculate(CalorieRequest request) {
        return (int) Math.round(calculateTdee(request));
    }

    @Override
    public MacroRatio macroRatio() {
        // 체중 유지: 탄단지를 고르게 배분한 균형 구성.
        return new MacroRatio(0.30, 0.40, 0.30);
    }
}
