package com.pdm.dietmanager.dto.response;

import com.pdm.dietmanager.entity.ExerciseLog;
import com.pdm.dietmanager.enums.ExerciseType;
import com.pdm.dietmanager.enums.Intensity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;

@Getter
@Schema(description = "운동 기록 응답")
public class ExerciseLogResponse {
    @Schema(description = "운동 기록 ID", example = "1")
    private final Long exerciseLogId;

    @Schema(description = "운동 날짜", example = "2026-06-15")
    private final LocalDate exerciseDate;

    @Schema(description = "운동 종류", example = "RUNNING")
    private final ExerciseType exerciseType;

    @Schema(description = "운동 시간(분)", example = "30")
    private final int durationMinutes;

    @Schema(description = "운동 강도", example = "MEDIUM")
    private final Intensity intensity;

    @Schema(description = "소모 칼로리", example = "273")
    private final int burnedCalories;

    private ExerciseLogResponse(ExerciseLog exerciseLog) {
        this.exerciseLogId = exerciseLog.getExerciseLogId();
        this.exerciseDate = exerciseLog.getExerciseDate();
        this.exerciseType = exerciseLog.getExerciseType();
        this.durationMinutes = exerciseLog.getDurationMinutes();
        this.intensity = exerciseLog.getIntensity();
        this.burnedCalories = exerciseLog.getBurnedCalories();
    }

    public static ExerciseLogResponse from(ExerciseLog exerciseLog) {
        return new ExerciseLogResponse(exerciseLog);
    }
}
