package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.dto.response.StatsResponse;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.enums.GoalType;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 연속 기록 스트릭과 주간 목표 달성률을 집계한다.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {
    private static final int STREAK_LOOKBACK_DAYS = 60;
    private static final int WEEK_DAYS = 7;
    private static final double MAINTAIN_TOLERANCE = 0.1;

    private final UserProfileService userProfileService;
    private final MealLogService mealLogService;
    private final CalorieService calorieService;

    public StatsResponse getStats(Long profileId, LocalDate endDate) {
        UserProfile profile = userProfileService.findProfile(profileId);
        int recommended = calorieService
                .calculateRecommendation(CalorieRequest.from(profile))
                .getRecommendedCalories();
        GoalType goal = profile.getGoalType();

        int streak = calculateStreak(profileId, endDate);

        int achievedDays = 0;
        int loggedDays = 0;
        for (int offset = WEEK_DAYS - 1; offset >= 0; offset--) {
            LocalDate day = endDate.minusDays(offset);
            int intake = mealLogService.calculateDailyTotalCalories(profileId, day);
            if (intake > 0) {
                loggedDays++;
                if (isGoalAchieved(goal, intake, recommended)) {
                    achievedDays++;
                }
            }
        }
        int achievementRate = (int) Math.round(achievedDays * 100.0 / WEEK_DAYS);

        return new StatsResponse(streak, achievedDays, loggedDays, achievementRate);
    }

    /** 오늘까지(오늘 미기록이면 어제까지) 며칠 연속으로 기록했는지 센다. */
    private int calculateStreak(Long profileId, LocalDate endDate) {
        Set<LocalDate> loggedDates = new HashSet<>(mealLogService.getLoggedDates(
                profileId, endDate.minusDays(STREAK_LOOKBACK_DAYS), endDate));

        // 오늘이 아직 미기록이면 어제부터 센다(하루가 끝나지 않았으므로 유예).
        LocalDate cursor = loggedDates.contains(endDate) ? endDate : endDate.minusDays(1);
        int streak = 0;
        while (loggedDates.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    /** 목표별 하루 달성 기준. (다이어트=권장 이하, 벌크업=권장 이상, 유지=권장 ±10%) */
    private boolean isGoalAchieved(GoalType goal, int intake, int recommended) {
        return switch (goal) {
            case WEIGHT_LOSS -> intake <= recommended;
            case MUSCLE_GAIN -> intake >= recommended;
            case MAINTAIN -> Math.abs(intake - recommended) <= recommended * MAINTAIN_TOLERANCE;
        };
    }
}
