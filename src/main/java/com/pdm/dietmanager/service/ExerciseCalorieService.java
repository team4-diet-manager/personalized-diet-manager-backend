package com.pdm.dietmanager.service;

import com.pdm.dietmanager.enums.ExerciseType;
import com.pdm.dietmanager.enums.Intensity;
import com.pdm.dietmanager.strategy.ExerciseCalorieStrategy;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 운동 종류별 소모 칼로리 전략을 DI로 주입받아 EnumMap으로 관리한다.
 * 새 운동이 추가돼도 {@link ExerciseCalorieStrategy} 구현체만 추가하면 된다(OCP).
 */
@Service
public class ExerciseCalorieService {
    private final Map<ExerciseType, ExerciseCalorieStrategy> strategyMap;

    public ExerciseCalorieService(List<ExerciseCalorieStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        ExerciseCalorieStrategy::getType,
                        strategy -> strategy,
                        (left, right) -> left,
                        () -> new EnumMap<>(ExerciseType.class)
                ));
    }

    public int calculateBurnedCalories(
            ExerciseType type,
            double weightKg,
            int minutes,
            Intensity intensity
    ) {
        ExerciseCalorieStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 운동입니다.");
        }
        return strategy.calculateBurnedCalories(weightKg, minutes, intensity);
    }
}
