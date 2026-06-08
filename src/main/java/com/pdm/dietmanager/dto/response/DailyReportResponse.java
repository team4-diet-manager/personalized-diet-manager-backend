package com.pdm.dietmanager.dto.response;

import java.time.LocalDate;

public class DailyReportResponse {
    private final Long profileId;
    private final LocalDate date;
    private final int recommendedCalories;
    private final int intakeCalories;
    private final int difference;
    private final String status;
    private final String message;

    public DailyReportResponse(
            Long profileId,
            LocalDate date,
            int recommendedCalories,
            int intakeCalories
    ) {
        this.profileId = profileId;
        this.date = date;
        this.recommendedCalories = recommendedCalories;
        this.intakeCalories = intakeCalories;
        this.difference = intakeCalories - recommendedCalories;
        this.status = resolveStatus(this.difference);
        this.message = createMessage(this.difference);
    }

    public Long getProfileId() {
        return profileId;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getRecommendedCalories() {
        return recommendedCalories;
    }

    public int getIntakeCalories() {
        return intakeCalories;
    }

    public int getDifference() {
        return difference;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
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
            return "권장 칼로리보다 " + Math.abs(difference) + "kcal 적게 섭취했습니다.";
        }
        if (difference > 0) {
            return "권장 칼로리보다 " + difference + "kcal 많이 섭취했습니다.";
        }
        return "권장 칼로리와 동일하게 섭취했습니다.";
    }
}
