package com.pdm.dietmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;

@Getter
@Schema(description = "일일 칼로리 리포트 응답")
public class DailyReportResponse {
    @Schema(description = "사용자 프로필 ID", example = "1")
    private final Long profileId;

    @Schema(description = "리포트 날짜", example = "2026-06-09")
    private final LocalDate date;

    @Schema(description = "프로필 목표 기준 하루 권장 칼로리", example = "1595")
    private final int recommendedCalories;

    @Schema(description = "해당 날짜의 실제 섭취 칼로리", example = "600")
    private final int intakeCalories;

    @Schema(description = "순 섭취 칼로리(섭취 - 운동 소모) - 권장 칼로리", example = "-1295")
    private final int difference;

    @Schema(description = "순 섭취 상태", example = "UNDER")
    private final String status;

    @Schema(description = "권장량 대비 순 섭취량 비교 메시지", example = "권장 칼로리보다 1295kcal 적습니다.")
    private final String message;

    @Schema(description = "목표별 권장 탄단지 그램")
    private final MacroNutrients recommendedMacros;

    @Schema(description = "해당 날짜의 실제 섭취 탄단지 그램")
    private final MacroNutrients intakeMacros;

    @Schema(description = "해당 날짜의 총 소모 칼로리(운동)", example = "300")
    private final int burnedCalories;

    @Schema(description = "순 섭취 칼로리(섭취 - 소모)", example = "300")
    private final int netCalories;

    public DailyReportResponse(
            Long profileId,
            LocalDate date,
            int recommendedCalories,
            int intakeCalories,
            MacroNutrients recommendedMacros,
            MacroNutrients intakeMacros,
            int burnedCalories
    ) {
        this.profileId = profileId;
        this.date = date;
        this.recommendedCalories = recommendedCalories;
        this.intakeCalories = intakeCalories;
        this.netCalories = intakeCalories - burnedCalories;
        this.difference = this.netCalories - recommendedCalories;
        this.status = resolveStatus(this.difference);
        this.message = createMessage(this.difference);
        this.recommendedMacros = recommendedMacros;
        this.intakeMacros = intakeMacros;
        this.burnedCalories = burnedCalories;
    }

    private String resolveStatus(int difference) {
        if (difference < 0) {
            return "UNDER";
        }
        if (difference > 0) {
            return "OVER";
        }
        return "MATCH";
    }

    private String createMessage(int difference) {
        if (difference < 0) {
            return "권장 칼로리보다 " + Math.abs(difference) + "kcal 적습니다.";
        }
        if (difference > 0) {
            return "권장 칼로리보다 " + difference + "kcal 많습니다.";
        }
        return "권장 칼로리와 동일합니다.";
    }
}
