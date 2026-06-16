package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.dto.request.MealLogRequest;
import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.dto.response.StatsResponse;
import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.enums.MealType;
import com.pdm.dietmanager.entity.User;
import com.pdm.dietmanager.repository.UserRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class StatsServiceTest {
    @Autowired
    private StatsService statsService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private MealLogService mealLogService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void countsConsecutiveLoggedDaysAsStreak() {
        Long profileId = createProfile();
        LocalDate today = LocalDate.of(2026, 6, 15);

        // 오늘·어제·그제 연속 기록, 그 전날(4일 전)은 비움 → 스트릭 3
        logMeal(profileId, today);
        logMeal(profileId, today.minusDays(1));
        logMeal(profileId, today.minusDays(2));
        logMeal(profileId, today.minusDays(4));

        StatsResponse stats = statsService.getStats(profileId, today);

        assertThat(stats.getStreak()).isEqualTo(3);
        assertThat(stats.getWeeklyLoggedDays()).isEqualTo(4);
    }

    @Test
    void weightLossAchievedWhenIntakeUnderRecommended() {
        Long profileId = createProfile();
        LocalDate today = LocalDate.of(2026, 6, 15);

        // 다이어트: 닭가슴살 1개(165kcal) << 권장 → 달성으로 집계
        logMeal(profileId, today);

        StatsResponse stats = statsService.getStats(profileId, today);

        assertThat(stats.getWeeklyAchievedDays()).isEqualTo(1);
        assertThat(stats.getAchievementRate()).isEqualTo(14); // round(1/7*100)
    }

    private Long createProfile() {
        User user = userRepository.save(User.builder()
                .email("stats-" + System.nanoTime() + "@example.com")
                .password("password")
                .nickname("stats")
                .build());
        return userProfileService.createProfile(user, UserProfileRequest.of(
                "지현", Gender.FEMALE, 23, 162.0, 55.0, ActivityLevel.NORMAL, GoalType.WEIGHT_LOSS
        )).getProfileId();
    }

    private void logMeal(Long profileId, LocalDate date) {
        mealLogService.createMealLog(MealLogRequest.of(profileId, date, MealType.LUNCH, 1L, 1));
    }
}
