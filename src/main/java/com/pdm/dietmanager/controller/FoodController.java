package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.response.ErrorResponse;
import com.pdm.dietmanager.dto.response.FoodResponse;
import com.pdm.dietmanager.enums.FoodSortType;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foods")
@Tag(name = "Food", description = "기본 음식 데이터 조회 API")
public class FoodController {
    private final FoodService foodService;

    @GetMapping
    @Operation(
            summary = "음식 목록 조회",
            description = "기본 음식 데이터 20개를 조회한다. keyword가 있으면 음식명에 해당 키워드를 포함한 음식만 조회한다. "
                    + "goalType이 있으면 목표 기준 신호등 등급(grade)을 함께 내려준다. "
                    + "sortType으로 식단 기록 음식 목록 정렬 전략을 선택한다."
    )
    @ApiResponse(responseCode = "200", description = "음식 목록 조회 성공")
    public List<FoodResponse> getFoods(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) GoalType goalType,
            @RequestParam(required = false) FoodSortType sortType
    ) {
        return foodService.getFoods(keyword, goalType, sortType);
    }

    @GetMapping("/{foodId}")
    @Operation(summary = "음식 단건 조회", description = "음식 ID로 음식명, 1회 제공량, 칼로리를 조회한다.")
    @ApiResponse(responseCode = "200", description = "음식 조회 성공")
    @ApiResponse(
            responseCode = "404",
            description = "음식을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public FoodResponse getFood(@PathVariable Long foodId) {
        return foodService.getFood(foodId);
    }
}
