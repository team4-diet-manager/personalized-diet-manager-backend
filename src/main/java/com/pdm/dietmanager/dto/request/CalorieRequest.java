package com.pdm.dietmanager.dto.request;

import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.entity.UserProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
@Schema(description = "권장 칼로리 계산 요청")
public class CalorieRequest {
    @Schema(description = "성별", example = "FEMALE")
    @NotNull(message = "성별은 필수 입력값입니다.")
    private Gender gender;

    @Schema(description = "나이", example = "23")
    @Min(value = 1, message = "나이는 1 이상이어야 합니다.")
    private int age;

    @Schema(description = "키(cm)", example = "162.0")
    @Positive(message = "키는 0보다 커야 합니다.")
    private double height;

    @Schema(description = "몸무게(kg)", example = "55.0")
    @Positive(message = "몸무게는 0보다 커야 합니다.")
    private double weight;

    @Schema(description = "활동량", example = "NORMAL")
    @NotNull(message = "활동량은 필수 입력값입니다.")
    private ActivityLevel activityLevel;

    @Schema(description = "식단 관리 목표", example = "WEIGHT_LOSS")
    @NotNull(message = "목표는 필수 입력값입니다.")
    private GoalType goalType;

    public static CalorieRequest of(Gender gender, int age, double height, double weight,
                                    ActivityLevel activityLevel, GoalType goalType) {
        CalorieRequest request = new CalorieRequest();
        request.gender = gender;
        request.age = age;
        request.height = height;
        request.weight = weight;
        request.activityLevel = activityLevel;
        request.goalType = goalType;
        return request;
    }

    public static CalorieRequest from(UserProfile userProfile) {
        return of(
                userProfile.getGender(),
                userProfile.getAge(),
                userProfile.getHeight(),
                userProfile.getWeight(),
                userProfile.getActivityLevel(),
                userProfile.getGoalType()
        );
    }
}
