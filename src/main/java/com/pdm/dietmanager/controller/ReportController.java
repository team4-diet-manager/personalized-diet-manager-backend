package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.dto.response.DailyReportResponse;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.service.CalorieService;
import com.pdm.dietmanager.service.MealLogService;
import com.pdm.dietmanager.service.UserProfileService;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final UserProfileService userProfileService;
    private final MealLogService mealLogService;
    private final CalorieService calorieService;

    public ReportController(
            UserProfileService userProfileService,
            MealLogService mealLogService,
            CalorieService calorieService
    ) {
        this.userProfileService = userProfileService;
        this.mealLogService = mealLogService;
        this.calorieService = calorieService;
    }

    @GetMapping("/daily")
    public DailyReportResponse getDailyReport(
            @RequestParam Long profileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        UserProfile userProfile = userProfileService.findProfile(profileId);
        CalorieRequest calorieRequest = CalorieRequest.from(userProfile);
        int recommendedCalories = calorieService.calculateRecommendedCalories(calorieRequest);
        int intakeCalories = mealLogService.calculateDailyTotalCalories(profileId, date);

        return new DailyReportResponse(profileId, date, recommendedCalories, intakeCalories);
    }
}
