package com.pdm.dietmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "최근 7일 칼로리 추이 리포트 응답")
public class WeeklyReportResponse {
    @Schema(description = "사용자 프로필 ID", example = "1")
    private final Long profileId;

    @Schema(description = "오래된 날짜 → 최신 날짜 순의 7일 항목")
    private final List<WeeklyReportDay> days;

    public WeeklyReportResponse(Long profileId, List<WeeklyReportDay> days) {
        this.profileId = profileId;
        this.days = days;
    }

    public Long getProfileId() {
        return profileId;
    }

    public List<WeeklyReportDay> getDays() {
        return days;
    }
}
