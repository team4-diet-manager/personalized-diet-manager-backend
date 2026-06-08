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
        return (int) Math.round(calculateTdee(request) * DEFICIT_RATIO);
    }
}
