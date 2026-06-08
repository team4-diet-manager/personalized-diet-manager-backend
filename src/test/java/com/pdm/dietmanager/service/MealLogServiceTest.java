package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.dto.request.MealLogRequest;
import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.dto.response.MealLogResponse;
import com.pdm.dietmanager.dto.response.UserProfileResponse;
import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.enums.MealType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MealLogServiceTest {
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private MealLogService mealLogService;

    @Test
    void createMealLogAndCalculateDailyTotalCalories() {
        UserProfileResponse profile = createProfile();
        LocalDate mealDate = LocalDate.of(2026, 6, 9);
        MealLogRequest request = MealLogRequest.of(
                profile.getProfileId(),
                mealDate,
                MealType.LUNCH,
                1L,
                2
        );

        MealLogResponse createdMealLog = mealLogService.createMealLog(request);
        List<MealLogResponse> mealLogs = mealLogService.getMealLogsByDate(
                profile.getProfileId(),
                mealDate
        );
        int dailyTotalCalories = mealLogService.calculateDailyTotalCalories(
                profile.getProfileId(),
                mealDate
        );

        assertThat(createdMealLog.getTotalCalories()).isEqualTo(330);
        assertThat(mealLogs).hasSize(1);
        assertThat(dailyTotalCalories).isEqualTo(330);
    }

    private UserProfileResponse createProfile() {
        return userProfileService.createProfile(UserProfileRequest.of(
                Gender.FEMALE,
                23,
                162.0,
                55.0,
                ActivityLevel.NORMAL,
                GoalType.WEIGHT_LOSS
        ));
    }
}
