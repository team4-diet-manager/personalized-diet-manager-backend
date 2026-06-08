package com.pdm.dietmanager.dto.response;

import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;

public class UserProfileResponse {
    private final Long profileId;
    private final Gender gender;
    private final int age;
    private final double height;
    private final double weight;
    private final ActivityLevel activityLevel;
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
