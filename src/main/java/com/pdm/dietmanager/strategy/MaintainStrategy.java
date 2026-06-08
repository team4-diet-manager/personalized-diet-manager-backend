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
}
