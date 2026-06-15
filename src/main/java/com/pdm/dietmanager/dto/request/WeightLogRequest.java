package com.pdm.dietmanager.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.Getter;

@Getter
@Schema(description = "체중 기록 등록 요청")
public class WeightLogRequest {
    @Schema(description = "사용자 프로필 ID", example = "1")
    @NotNull(message = "사용자 프로필 ID는 필수 입력값입니다.")
    private Long profileId;

    @Schema(description = "측정 날짜", example = "2026-06-15")
    @NotNull(message = "날짜는 필수 입력값입니다.")
    private LocalDate logDate;

    @Schema(description = "체중(kg)", example = "54.5")
    @Positive(message = "체중은 0보다 커야 합니다.")
    private double weight;

    public static WeightLogRequest of(Long profileId, LocalDate logDate, double weight) {
        WeightLogRequest request = new WeightLogRequest();
        request.profileId = profileId;
        request.logDate = logDate;
        request.weight = weight;
        return request;
    }
}
