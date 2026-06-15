package com.pdm.dietmanager.dto.request;

import com.pdm.dietmanager.enums.ExerciseType;
import com.pdm.dietmanager.enums.Intensity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;

@Getter
@Schema(description = "운동 기록 등록 요청")
public class ExerciseLogRequest {
    @Schema(description = "사용자 프로필 ID", example = "1")
    @NotNull(message = "사용자 프로필 ID는 필수 입력값입니다.")
    private Long profileId;

    @Schema(description = "운동 날짜", example = "2026-06-15")
    @NotNull(message = "날짜는 필수 입력값입니다.")
    private LocalDate exerciseDate;

    @Schema(description = "운동 종류", example = "RUNNING")
    @NotNull(message = "운동 종류는 필수 입력값입니다.")
    private ExerciseType exerciseType;

    @Schema(description = "운동 시간(분)", example = "30")
    @Min(value = 1, message = "운동 시간은 1분 이상이어야 합니다.")
    private int durationMinutes;

    @Schema(description = "운동 강도", example = "MEDIUM")
    @NotNull(message = "운동 강도는 필수 입력값입니다.")
    private Intensity intensity;

    public static ExerciseLogRequest of(Long profileId, LocalDate exerciseDate,
                                        ExerciseType exerciseType, int durationMinutes,
                                        Intensity intensity) {
        ExerciseLogRequest request = new ExerciseLogRequest();
        request.profileId = profileId;
        request.exerciseDate = exerciseDate;
        request.exerciseType = exerciseType;
        request.durationMinutes = durationMinutes;
        request.intensity = intensity;
        return request;
    }
}
