package com.pdm.dietmanager.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DailyReportResponseTest {
    private static final LocalDate DATE = LocalDate.of(2026, 6, 14);
    private static final MacroNutrients MACROS = new MacroNutrients(100, 100, 40);
    private static final MacroNutrients INTAKE = new MacroNutrients(40, 30, 10);

    @Test
    void intakeBelowRecommendationIsUnder() {
        DailyReportResponse report = new DailyReportResponse(1L, DATE, 1600, 600, MACROS, INTAKE);

        assertThat(report.getDifference()).isEqualTo(-1000);
        assertThat(report.getStatus()).isEqualTo("UNDER");
        assertThat(report.getMessage()).isEqualTo("권장 칼로리보다 1000kcal 적게 섭취했습니다.");
    }

    @Test
    void intakeAboveRecommendationIsOver() {
        DailyReportResponse report = new DailyReportResponse(1L, DATE, 1600, 2000, MACROS, INTAKE);

        assertThat(report.getDifference()).isEqualTo(400);
        assertThat(report.getStatus()).isEqualTo("OVER");
        assertThat(report.getMessage()).isEqualTo("권장 칼로리보다 400kcal 많이 섭취했습니다.");
    }

    @Test
    void intakeEqualToRecommendationIsMatch() {
        DailyReportResponse report = new DailyReportResponse(1L, DATE, 1600, 1600, MACROS, INTAKE);

        assertThat(report.getDifference()).isZero();
        assertThat(report.getStatus()).isEqualTo("MATCH");
        assertThat(report.getMessage()).isEqualTo("권장 칼로리와 동일하게 섭취했습니다.");
    }
}
