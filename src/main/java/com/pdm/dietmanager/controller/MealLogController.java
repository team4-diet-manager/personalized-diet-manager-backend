package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.request.MealLogRequest;
import com.pdm.dietmanager.dto.response.DailyMealLogResponse;
import com.pdm.dietmanager.dto.response.ErrorResponse;
import com.pdm.dietmanager.dto.response.MealLogResponse;
import com.pdm.dietmanager.service.MealLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meal-logs")
@Tag(name = "Meal Log", description = "날짜별 식단 기록 등록, 조회, 수정, 삭제 API")
public class MealLogController {
    private final MealLogService mealLogService;

    public MealLogController(MealLogService mealLogService) {
        this.mealLogService = mealLogService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "식단 기록 등록", description = "날짜, 식사 구분, 음식, 섭취 수량을 저장한다.")
    @ApiResponse(responseCode = "201", description = "식단 기록 등록 성공")
    @ApiResponse(
            responseCode = "400",
            description = "입력값 검증 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "프로필 또는 음식을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public MealLogResponse createMealLog(@Valid @RequestBody MealLogRequest request) {
        return mealLogService.createMealLog(request);
    }

    @GetMapping
    @Operation(summary = "날짜별 식단 기록 조회", description = "특정 사용자의 특정 날짜 식단 기록과 총 섭취 칼로리를 조회한다.")
    @ApiResponse(responseCode = "200", description = "날짜별 식단 기록 조회 성공")
    @ApiResponse(
            responseCode = "400",
            description = "필수 쿼리 파라미터 누락 또는 날짜 형식 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public DailyMealLogResponse getMealLogs(
            @RequestParam Long profileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<MealLogResponse> mealLogs = mealLogService.getMealLogsByDate(profileId, date);
        int dailyTotalCalories = mealLogService.calculateDailyTotalCalories(profileId, date);
        return new DailyMealLogResponse(profileId, date, mealLogs, dailyTotalCalories);
    }

    @PutMapping("/{mealLogId}")
    @Operation(summary = "식단 기록 수정", description = "기존 식단 기록의 음식, 식사 구분, 섭취 수량을 수정한다.")
    @ApiResponse(responseCode = "200", description = "식단 기록 수정 성공")
    @ApiResponse(
            responseCode = "400",
            description = "입력값 검증 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "식단 기록 또는 음식을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public MealLogResponse updateMealLog(
            @PathVariable Long mealLogId,
            @Valid @RequestBody MealLogRequest request
    ) {
        return mealLogService.updateMealLog(mealLogId, request);
    }

    @DeleteMapping("/{mealLogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "식단 기록 삭제", description = "식단 기록 ID로 기존 식단 기록을 삭제한다.")
    @ApiResponse(responseCode = "204", description = "식단 기록 삭제 성공")
    @ApiResponse(
            responseCode = "404",
            description = "식단 기록을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public void deleteMealLog(@PathVariable Long mealLogId) {
        mealLogService.deleteMealLog(mealLogId);
    }
}
