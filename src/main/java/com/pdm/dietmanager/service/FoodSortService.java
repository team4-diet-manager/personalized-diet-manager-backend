package com.pdm.dietmanager.service;

import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.enums.FoodSortType;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.strategy.FoodSortStrategy;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FoodSortService {
    private final Map<FoodSortType, FoodSortStrategy> strategyMap;

    public FoodSortService(List<FoodSortStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        FoodSortStrategy::getSortType,
                        strategy -> strategy,
                        (left, right) -> left,
                        () -> new EnumMap<>(FoodSortType.class)
                ));
    }

    public List<Food> sort(FoodSortType sortType, List<Food> foods, GoalType goalType) {
        FoodSortStrategy strategy = strategyMap.get(sortType == null ? FoodSortType.RECOMMENDED : sortType);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 음식 정렬 기준입니다.");
        }
        return strategy.sort(foods, goalType);
    }
}
