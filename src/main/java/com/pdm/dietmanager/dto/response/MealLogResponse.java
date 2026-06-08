package com.pdm.dietmanager.dto.response;

import com.pdm.dietmanager.entity.MealLog;
import com.pdm.dietmanager.enums.MealType;
import java.time.LocalDate;

public class MealLogResponse {
    private final Long mealLogId;
    private final Long profileId;
    private final LocalDate mealDate;
    private final MealType mealType;
    private final Long foodId;
    private final String foodName;
    private final int calories;
    private final int quantity;
    private final int totalCalories;

    private MealLogResponse(MealLog mealLog) {
        this.mealLogId = mealLog.getMealLogId();
        this.profileId = mealLog.getUserProfile().getProfileId();
        this.mealDate = mealLog.getMealDate();
        this.mealType = mealLog.getMealType();
        this.foodId = mealLog.getFood().getFoodId();
        this.foodName = mealLog.getFood().getName();
        this.calories = mealLog.getFood().getCalories();
        this.quantity = mealLog.getQuantity();
        this.totalCalories = mealLog.getTotalCalories();
    }

    public static MealLogResponse from(MealLog mealLog) {
        return new MealLogResponse(mealLog);
    }

    public Long getMealLogId() {
        return mealLogId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public LocalDate getMealDate() {
        return mealDate;
    }

    public MealType getMealType() {
        return mealType;
    }

    public Long getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getCalories() {
        return calories;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalCalories() {
        return totalCalories;
    }
}
