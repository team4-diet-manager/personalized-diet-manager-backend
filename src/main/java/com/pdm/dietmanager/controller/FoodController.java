package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.response.FoodResponse;
import com.pdm.dietmanager.service.FoodService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/foods")
public class FoodController {
    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public List<FoodResponse> getFoods(
            @RequestParam(required = false) String keyword
    ) {
        return foodService.getFoods(keyword);
    }

    @GetMapping("/{foodId}")
    public FoodResponse getFood(@PathVariable Long foodId) {
        return foodService.getFood(foodId);
    }
}
