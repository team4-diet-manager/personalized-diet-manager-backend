package com.pdm.dietmanager.service;

import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.enums.FoodGrade;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.strategy.FoodGradeStrategy;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 목표별 음식 분류 전략을 DI로 주입받아 EnumMap으로 관리한다.
 * 새 목표가 생겨도 {@link FoodGradeStrategy} 구현체만 추가하면 되어 분기문 수정이 필요 없다(OCP).
 */
@Service
public class FoodGradeService {
    private final Map<GoalType, FoodGradeStrategy> strategyMap;

    public FoodGradeService(List<FoodGradeStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        FoodGradeStrategy::getGoalType,
                        strategy -> strategy,
                        (left, right) -> left,
                        () -> new EnumMap<>(GoalType.class)
                ));
    }

    public FoodGrade classify(GoalType goalType, Food food) {
        FoodGradeStrategy strategy = strategyMap.get(goalType);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 목표입니다.");
        }
        return strategy.classify(food);
    }
}
