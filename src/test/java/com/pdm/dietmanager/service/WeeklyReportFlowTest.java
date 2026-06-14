package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.dto.request.MealLogRequest;
import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.dto.response.UserProfileResponse;
import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.enums.MealType;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 최근 7일 칼로리 추이 집계 흐름 검증.
 */
@SpringBootTest
@Transactional
class WeeklyReportFlowTest {
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private MealLogService mealLogService;

    @Test
    void aggregatesIntakePerDayOverSevenDays() {
        UserProfileResponse profile = userProfileService.createProfile(UserProfileRequest.of(
                Gender.FEMALE, 23, 162.0, 55.0, ActivityLevel.NORMAL, GoalType.WEIGHT_LOSS
        ));
        LocalDate end = LocalDate.of(2026, 6, 15);

        // 마지막 날(오늘)에 닭가슴살(165kcal) 2개 = 330kcal, 그 외 날짜는 0.
        mealLogService.createMealLog(MealLogRequest.of(
                profile.getProfileId(), end, MealType.LUNCH, 1L, 2
        ));

        int todayIntake = mealLogService.calculateDailyTotalCalories(profile.getProfileId(), end);
        int yesterdayIntake = mealLogService.calculateDailyTotalCalories(
                profile.getProfileId(), end.minusDays(1)
        );

        assertThat(todayIntake).isEqualTo(330);
        assertThat(yesterdayIntake).isZero();
    }
}
