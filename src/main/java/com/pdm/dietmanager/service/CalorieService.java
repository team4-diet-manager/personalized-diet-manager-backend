package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.dto.response.CalorieResponse;
import com.pdm.dietmanager.dto.response.MacroNutrients;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.strategy.CalorieStrategy;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CalorieService {

    private final Map<GoalType, CalorieStrategy> strategyMap;

    public CalorieService(List<CalorieStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        CalorieStrategy::getGoalType,
                        strategy -> strategy,
                        (left, right) -> left,
                        () -> new EnumMap<>(GoalType.class)
                ));
    }



    public CalorieResponse calculateRecommendation(CalorieRequest request) {
        CalorieStrategy strategy = getStrategy(request.getGoalType());

        int recommendedCalories = strategy.calculate(request);
        MacroNutrients macros = strategy.calculateMacros(recommendedCalories);

        return new CalorieResponse(
                request.getGoalType(),
                recommendedCalories,
                macros
        );
    }

    private CalorieStrategy getStrategy(GoalType goalType) {
        CalorieStrategy strategy = strategyMap.get(goalType);

        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 목표입니다.");
        }

        return strategy;
    }
}
