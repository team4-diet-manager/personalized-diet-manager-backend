package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.dto.response.CalorieResponse;
import com.pdm.dietmanager.dto.response.DailyReportResponse;
import com.pdm.dietmanager.dto.response.MacroNutrients;
import com.pdm.dietmanager.dto.response.StatsResponse;
import com.pdm.dietmanager.dto.response.WeeklyReportDay;
import com.pdm.dietmanager.dto.response.WeeklyReportResponse;
import com.pdm.dietmanager.entity.User;
import com.pdm.dietmanager.entity.UserProfile;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private static final int WEEK_DAYS = 7;

    private final UserProfileService userProfileService;
    private final MealLogService mealLogService;
    private final CalorieService calorieService;
    private final ExerciseLogService exerciseLogService;
    private final StatsService statsService;

    public DailyReportResponse getDailyReport(User user, LocalDate date) {
        UserProfile userProfile = getCurrentUserProfile(user);
        Long profileId = userProfile.getProfileId();
        CalorieResponse recommendation = calorieService.calculateRecommendation(
                CalorieRequest.from(userProfile)
        );

        int intakeCalories = mealLogService.calculateDailyTotalCalories(profileId, date);
        MacroNutrients intakeMacros = mealLogService.calculateDailyIntakeMacros(profileId, date);
        int burnedCalories = exerciseLogService.calculateDailyBurnedCalories(profileId, date);

        return new DailyReportResponse(
                profileId,
                date,
                recommendation.getRecommendedCalories(),
                intakeCalories,
                recommendation.getMacros(),
                intakeMacros,
                burnedCalories
        );
    }

    public WeeklyReportResponse getWeeklyReport(User user, LocalDate endDate) {
        UserProfile userProfile = getCurrentUserProfile(user);
        int recommendedCalories = calorieService.calculateRecommendation(
                CalorieRequest.from(userProfile)
        ).getRecommendedCalories();
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        List<WeeklyReportDay> days = new ArrayList<>();
        for (int offset = WEEK_DAYS - 1; offset >= 0; offset--) {
            LocalDate day = end.minusDays(offset);
            int intakeCalories = mealLogService.calculateDailyTotalCalories(user, day);
            days.add(new WeeklyReportDay(day, recommendedCalories, intakeCalories));
        }

        return new WeeklyReportResponse(userProfile.getProfileId(), days);
    }

    public StatsResponse getStats(Long profileId, LocalDate endDate) {
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        return statsService.getStats(profileId, end);
    }

    public StatsResponse getStats(User user, LocalDate endDate) {
        UserProfile userProfile = getCurrentUserProfile(user);
        return getStats(userProfile.getProfileId(), endDate);
    }

    private UserProfile getCurrentUserProfile(User user) {
        return userProfileService.findProfileByUser(user);
    }
}
