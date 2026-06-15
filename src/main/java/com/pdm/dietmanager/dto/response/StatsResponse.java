package com.pdm.dietmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "기록 통계(연속 기록·주간 달성률) 응답")
@Getter
public class StatsResponse {
    @Schema(description = "연속 기록 일수(오늘 미기록 시 어제까지 기준)", example = "5")
    private final int streak;

    @Schema(description = "최근 7일 중 목표를 달성한 일수", example = "4")
    private final int weeklyAchievedDays;

    @Schema(description = "최근 7일 중 식단을 기록한 일수", example = "6")
    private final int weeklyLoggedDays;

    @Schema(description = "주간 목표 달성률(달성일/7, %)", example = "57")
    private final int achievementRate;

    public StatsResponse(int streak, int weeklyAchievedDays, int weeklyLoggedDays, int achievementRate) {
        this.streak = streak;
        this.weeklyAchievedDays = weeklyAchievedDays;
        this.weeklyLoggedDays = weeklyLoggedDays;
        this.achievementRate = achievementRate;
    }
}
