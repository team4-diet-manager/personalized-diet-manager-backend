package com.pdm.dietmanager.dto.response;

import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 프로필 응답")
public class UserProfileResponse {
    @Schema(description = "사용자 프로필 ID", example = "1")
    private final Long profileId;

    @Schema(description = "성별", example = "FEMALE")
    private final Gender gender;

    @Schema(description = "나이", example = "23")
    private final int age;

    @Schema(description = "키(cm)", example = "162.0")
    private final double height;

    @Schema(description = "몸무게(kg)", example = "55.0")
    private final double weight;

    @Schema(description = "활동량", example = "NORMAL")
    private final ActivityLevel activityLevel;

    @Schema(description = "식단 관리 목표", example = "WEIGHT_LOSS")
    private final GoalType goalType;

    private UserProfileResponse(UserProfile userProfile) {
        this.profileId = userProfile.getProfileId();
        this.gender = userProfile.getGender();
        this.age = userProfile.getAge();
        this.height = userProfile.getHeight();
        this.weight = userProfile.getWeight();
        this.activityLevel = userProfile.getActivityLevel();
        this.goalType = userProfile.getGoalType();
    }

    public static UserProfileResponse from(UserProfile userProfile) {
        return new UserProfileResponse(userProfile);
    }

    public Long getProfileId() {
        return profileId;
    }

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
