package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.enums.GoalType;
import org.springframework.stereotype.Component;

@Component
public class MuscleGainStrategy implements CalorieStrategy {
    private static final double SURPLUS_RATIO = 1.15;

    @Override
    public GoalType getGoalType() {
        return GoalType.MUSCLE_GAIN;
    }

    @Override
    public int calculate(CalorieRequest request) {
        return (int) Math.round(calculateTdee(request) * SURPLUS_RATIO);
    }
}
