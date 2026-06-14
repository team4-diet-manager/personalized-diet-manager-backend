package com.pdm.dietmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
@Schema(description = "날짜별 식단 기록 조회 응답")
public class DailyMealLogResponse {
    @Schema(description = "사용자 프로필 ID", example = "1")
    private final Long profileId;

    @Schema(description = "식단 기록 날짜", example = "2026-06-09")
    private final LocalDate mealDate;

    @Schema(description = "해당 날짜의 식단 기록 목록")
    private final List<MealLogResponse> mealLogs;

    @Schema(description = "해당 날짜의 총 섭취 칼로리", example = "600")
    private final int dailyTotalCalories;

    public DailyMealLogResponse(
            Long profileId,
            LocalDate mealDate,
            List<MealLogResponse> mealLogs,
            int dailyTotalCalories
    ) {
        this.profileId = profileId;
        this.mealDate = mealDate;
        this.mealLogs = mealLogs;
        this.dailyTotalCalories = dailyTotalCalories;
    }
}
