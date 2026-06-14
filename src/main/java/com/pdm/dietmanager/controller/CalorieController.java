package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.dto.response.CalorieResponse;
import com.pdm.dietmanager.dto.response.ErrorResponse;
import com.pdm.dietmanager.service.CalorieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calories")
@Tag(name = "Calorie", description = "목표별 권장 칼로리 계산 API")
public class CalorieController {
    private final CalorieService calorieService;

    @PostMapping("/recommendation")
    @Operation(
            summary = "하루 권장 칼로리 계산",
            description = "사용자의 신체 정보, 활동량, 목표를 기반으로 Strategy Pattern을 적용해 권장 칼로리를 계산한다."
    )
    @ApiResponse(responseCode = "200", description = "권장 칼로리 계산 성공")
    @ApiResponse(
            responseCode = "400",
            description = "입력값 검증 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public CalorieResponse calculateRecommendation(@Valid @RequestBody CalorieRequest request) {
        return calorieService.calculateRecommendation(request);
    }
}
