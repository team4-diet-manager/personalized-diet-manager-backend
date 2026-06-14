package com.pdm.dietmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "주간 리포트의 하루 단위 항목")
public class WeeklyReportDay {
    @Schema(description = "날짜", example = "2026-06-15")
    private final LocalDate date;

    @Schema(description = "해당 날짜 기준 하루 권장 칼로리", example = "1595")
    private final int recommendedCalories;

    @Schema(description = "해당 날짜의 실제 섭취 칼로리", example = "1480")
    private final int intakeCalories;

    public WeeklyReportDay(LocalDate date, int recommendedCalories, int intakeCalories) {
        this.date = date;
        this.recommendedCalories = recommendedCalories;
        this.intakeCalories = intakeCalories;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getRecommendedCalories() {
        return recommendedCalories;
    }

    public int getIntakeCalories() {
        return intakeCalories;
    }
}
