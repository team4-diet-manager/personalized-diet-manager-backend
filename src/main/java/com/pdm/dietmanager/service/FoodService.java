package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.response.FoodResponse;
import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.exception.ResourceNotFoundException;
import com.pdm.dietmanager.repository.FoodRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final FoodGradeService foodGradeService;


    public List<FoodResponse> getFoods(String keyword, GoalType goalType) {
        List<Food> foods = hasText(keyword)
                ? foodRepository.findByNameContaining(keyword)
                : foodRepository.findAll();

        // 목표가 주어지면 신호등 등급을 함께 계산해 내려준다.
        if (goalType == null) {
            return foods.stream().map(FoodResponse::from).toList();
        }
        return foods.stream()
                .map(food -> FoodResponse.of(food, foodGradeService.classify(goalType, food)))
                .toList();
    }

    public FoodResponse getFood(Long foodId) {
        return FoodResponse.from(findFood(foodId));
    }

    public Food findFood(Long foodId) {
        return foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "음식을 찾을 수 없습니다. foodId=" + foodId
                ));
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
