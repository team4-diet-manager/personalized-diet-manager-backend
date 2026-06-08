package com.pdm.dietmanager.dto.request;

import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CalorieRequest {
    @NotNull(message = "성별은 필수 입력값입니다.")
    private Gender gender;

    @Min(value = 1, message = "나이는 1 이상이어야 합니다.")
    private int age;

    @Positive(message = "키는 0보다 커야 합니다.")
    private double height;

    @Positive(message = "몸무게는 0보다 커야 합니다.")
    private double weight;

    @NotNull(message = "활동량은 필수 입력값입니다.")
    private ActivityLevel activityLevel;

    @NotNull(message = "목표는 필수 입력값입니다.")
    private GoalType goalType;

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public GoalType getGoalType() {
        return goalType;
    }
}
