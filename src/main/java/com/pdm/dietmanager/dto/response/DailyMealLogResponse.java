package com.pdm.dietmanager.dto.response;

import java.time.LocalDate;
import java.util.List;

public class DailyMealLogResponse {
    private final Long profileId;
    private final LocalDate mealDate;
    private final List<MealLogResponse> mealLogs;
    private final int dailyTotalCalories;

    public DailyMealLogResponse(
            Long profileId,
            LocalDate mealDate,
            List<MealLogResponse> mealLogs,
            int dailyTotalCalories
    ) {
        this.profileId = profileId;
        this.mealDate = mealDate;
        this.mealLogs = mealLogs;
        this.dailyTotalCalories = dailyTotalCalories;
    }

    public Long getProfileId() {
        return profileId;
    }

    public LocalDate getMealDate() {
        return mealDate;
    }

    public List<MealLogResponse> getMealLogs() {
        return mealLogs;
    }

    public int getDailyTotalCalories() {
        return dailyTotalCalories;
    }
}
