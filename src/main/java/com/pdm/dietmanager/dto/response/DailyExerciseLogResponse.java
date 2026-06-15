package com.pdm.dietmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
@Schema(description = "날짜별 운동 기록 조회 응답")
public class DailyExerciseLogResponse {
    @Schema(description = "사용자 프로필 ID", example = "1")
    private final Long profileId;

    @Schema(description = "운동 날짜", example = "2026-06-15")
    private final LocalDate exerciseDate;

    @Schema(description = "해당 날짜의 운동 기록 목록")
    private final List<ExerciseLogResponse> exerciseLogs;

    @Schema(description = "해당 날짜의 총 소모 칼로리", example = "420")
    private final int dailyTotalBurned;

    public DailyExerciseLogResponse(Long profileId, LocalDate exerciseDate,
                                    List<ExerciseLogResponse> exerciseLogs, int dailyTotalBurned) {
        this.profileId = profileId;
        this.exerciseDate = exerciseDate;
        this.exerciseLogs = exerciseLogs;
        this.dailyTotalBurned = dailyTotalBurned;
    }
}
