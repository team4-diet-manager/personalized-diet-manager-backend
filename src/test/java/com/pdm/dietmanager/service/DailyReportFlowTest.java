package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.dto.request.MealLogRequest;
import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.dto.response.UserProfileResponse;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.enums.MealType;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class DailyReportFlowTest {
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private MealLogService mealLogService;

    @Autowired
    private CalorieService calorieService;

    @Test
    void calculateRecommendationAndDailyIntakeGap() {
        UserProfileResponse profile = userProfileService.createProfile(UserProfileRequest.of(
                Gender.FEMALE,
                23,
                162.0,
                55.0,
                ActivityLevel.NORMAL,
                GoalType.WEIGHT_LOSS
        ));
        LocalDate mealDate = LocalDate.of(2026, 6, 9);

        mealLogService.createMealLog(MealLogRequest.of(
                profile.getProfileId(),
                mealDate,
                MealType.LUNCH,
                1L,
                1
        ));

        UserProfile userProfile = userProfileService.findProfile(profile.getProfileId());
        int recommendedCalories = calorieService.calculateRecommendedCalories(
                CalorieRequest.from(userProfile)
        );
        int dailyTotalCalories = mealLogService.calculateDailyTotalCalories(
                profile.getProfileId(),
                mealDate
        );

        assertThat(recommendedCalories).isEqualTo(1595);
        assertThat(dailyTotalCalories).isEqualTo(165);
        assertThat(dailyTotalCalories - recommendedCalories).isEqualTo(-1430);
    }
}
