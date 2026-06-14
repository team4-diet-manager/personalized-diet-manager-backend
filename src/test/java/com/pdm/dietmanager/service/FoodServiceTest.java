package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.dto.response.FoodResponse;
import com.pdm.dietmanager.enums.FoodGrade;
import com.pdm.dietmanager.enums.GoalType;
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
        List<FoodResponse> foods = foodService.getFoods(null, null);

        assertThat(foods).hasSize(20);
        assertThat(foods)
                .extracting(FoodResponse::getName)
                .contains("닭가슴살", "고구마", "현미밥");
        // 목표 미지정 시 신호등 등급은 비어 있다.
        assertThat(foods).allSatisfy(food -> assertThat(food.getGrade()).isNull());
    }

    @Test
    void getFoodsFiltersByKeyword() {
        List<FoodResponse> foods = foodService.getFoods("닭", null);

        assertThat(foods)
                .extracting(FoodResponse::getName)
                .containsExactly("닭가슴살", "닭갈비");
    }

    @Test
    void getFoodsWithGoalAttachesTrafficLightGrade() {
        List<FoodResponse> foods = foodService.getFoods(null, GoalType.WEIGHT_LOSS);

        // 다이어트 기준: 저칼로리·고단백 닭가슴살은 GREEN, 고칼로리 햄버거는 RED.
        assertThat(gradeOf(foods, "닭가슴살")).isEqualTo(FoodGrade.GREEN);
        assertThat(gradeOf(foods, "햄버거")).isEqualTo(FoodGrade.RED);
        assertThat(foods).allSatisfy(food -> assertThat(food.getGrade()).isNotNull());
    }

    private FoodGrade gradeOf(List<FoodResponse> foods, String name) {
        return foods.stream()
                .filter(food -> food.getName().equals(name))
                .findFirst()
                .orElseThrow()
                .getGrade();
    }
}
