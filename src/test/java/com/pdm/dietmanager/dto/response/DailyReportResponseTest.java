package com.pdm.dietmanager.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DailyReportResponseTest {
    private static final LocalDate DATE = LocalDate.of(2026, 6, 14);
    private static final MacroNutrients MACROS = new MacroNutrients(100, 100, 40);
    private static final MacroNutrients INTAKE = new MacroNutrients(40, 30, 10);

    @Test
    void netIntakeBelowRecommendationIsUnder() {
        DailyReportResponse report = new DailyReportResponse(1L, DATE, 1600, 2000, MACROS, INTAKE, 500);

        assertThat(report.getDifference()).isEqualTo(-100);
        assertThat(report.getStatus()).isEqualTo("UNDER");
        assertThat(report.getMessage()).isEqualTo("권장 칼로리보다 100kcal 적습니다.");
        assertThat(report.getBurnedCalories()).isEqualTo(500);
        assertThat(report.getNetCalories()).isEqualTo(1500);
    }

    @Test
    void netIntakeAboveRecommendationIsOver() {
        DailyReportResponse report = new DailyReportResponse(1L, DATE, 1600, 2200, MACROS, INTAKE, 300);

        assertThat(report.getDifference()).isEqualTo(300);
        assertThat(report.getStatus()).isEqualTo("OVER");
        assertThat(report.getMessage()).isEqualTo("권장 칼로리보다 300kcal 많습니다.");
        assertThat(report.getBurnedCalories()).isEqualTo(300);
        assertThat(report.getNetCalories()).isEqualTo(1900);
    }

    @Test
    void netIntakeEqualToRecommendationIsMatch() {
        DailyReportResponse report = new DailyReportResponse(1L, DATE, 1600, 1800, MACROS, INTAKE, 200);

        assertThat(report.getDifference()).isZero();
        assertThat(report.getStatus()).isEqualTo("MATCH");
        assertThat(report.getMessage()).isEqualTo("권장 칼로리와 동일합니다.");
        assertThat(report.getBurnedCalories()).isEqualTo(200);
        assertThat(report.getNetCalories()).isEqualTo(1600);
    }
}
