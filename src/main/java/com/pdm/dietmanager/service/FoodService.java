package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.response.FoodResponse;
import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.repository.FoodRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FoodService {
    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<FoodResponse> getFoods(String keyword) {
        List<Food> foods = hasText(keyword)
                ? foodRepository.findByNameContaining(keyword)
                : foodRepository.findAll();

        return foods.stream()
                .map(FoodResponse::from)
                .toList();
    }

    public FoodResponse getFood(Long foodId) {
        return FoodResponse.from(findFood(foodId));
    }

    public Food findFood(Long foodId) {
        return foodRepository.findById(foodId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "음식을 찾을 수 없습니다. foodId=" + foodId
                ));
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
