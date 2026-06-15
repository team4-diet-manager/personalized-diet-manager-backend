package com.pdm.dietmanager.dto.response;

import com.pdm.dietmanager.entity.WeightLog;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;

@Getter
@Schema(description = "체중 기록 응답")
public class WeightLogResponse {
    @Schema(description = "체중 기록 ID", example = "1")
    private final Long weightLogId;

    @Schema(description = "측정 날짜", example = "2026-06-15")
    private final LocalDate logDate;

    @Schema(description = "체중(kg)", example = "54.5")
    private final double weight;

    private WeightLogResponse(WeightLog weightLog) {
        this.weightLogId = weightLog.getWeightLogId();
        this.logDate = weightLog.getLogDate();
        this.weight = weightLog.getWeight();
    }

    public static WeightLogResponse from(WeightLog weightLog) {
        return new WeightLogResponse(weightLog);
    }
}
