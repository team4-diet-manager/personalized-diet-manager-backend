package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.dto.response.FoodResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FoodServiceTest {
    @Autowired
    private FoodService foodService;

    @Test
    void getFoodsReturnsSeededFoodData() {
        List<FoodResponse> foods = foodService.getFoods(null);

        assertThat(foods).hasSize(20);
        assertThat(foods)
                .extracting(FoodResponse::getName)
                .contains("닭가슴살", "고구마", "현미밥");
    }

    @Test
    void getFoodsFiltersByKeyword() {
        List<FoodResponse> foods = foodService.getFoods("닭");

        assertThat(foods)
                .extracting(FoodResponse::getName)
                .containsExactly("닭가슴살", "닭갈비");
    }
}
