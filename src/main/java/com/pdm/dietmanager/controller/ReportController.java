package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.dto.response.DailyReportResponse;
import com.pdm.dietmanager.dto.response.ErrorResponse;
import com.pdm.dietmanager.dto.response.MacroNutrients;
import com.pdm.dietmanager.dto.response.WeeklyReportDay;
import com.pdm.dietmanager.dto.response.WeeklyReportResponse;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.service.CalorieService;
import com.pdm.dietmanager.service.MealLogService;
import com.pdm.dietmanager.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Report", description = "권장 칼로리와 실제 섭취 칼로리 비교 API")
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
    @Operation(
            summary = "일일 칼로리 리포트 조회",
            description = "저장된 프로필 목표 기준 권장 칼로리와 특정 날짜의 실제 섭취 칼로리를 비교한다."
    )
    @ApiResponse(responseCode = "200", description = "일일 리포트 조회 성공")
    @ApiResponse(
            responseCode = "400",
            description = "필수 쿼리 파라미터 누락 또는 날짜 형식 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "프로필을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public DailyReportResponse getDailyReport(
            @RequestParam Long profileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        UserProfile userProfile = userProfileService.findProfile(profileId);
        CalorieRequest calorieRequest = CalorieRequest.from(userProfile);
        int recommendedCalories = calorieService.calculateRecommendedCalories(calorieRequest);
        MacroNutrients recommendedMacros = calorieService.calculateRecommendedMacros(calorieRequest);
        int intakeCalories = mealLogService.calculateDailyTotalCalories(profileId, date);
        MacroNutrients intakeMacros = mealLogService.calculateDailyIntakeMacros(profileId, date);

        return new DailyReportResponse(
                profileId,
                date,
                recommendedCalories,
                intakeCalories,
                recommendedMacros,
                intakeMacros
        );
    }

    @GetMapping("/weekly")
    @Operation(
            summary = "주간 칼로리 추이 조회",
            description = "endDate(기본값: 오늘)를 마지막 날로 하는 최근 7일의 권장/섭취 칼로리를 날짜순으로 반환한다."
    )
    @ApiResponse(responseCode = "200", description = "주간 추이 조회 성공")
    @ApiResponse(
            responseCode = "400",
            description = "필수 쿼리 파라미터 누락 또는 날짜 형식 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "프로필을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public WeeklyReportResponse getWeeklyReport(
            @RequestParam Long profileId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        UserProfile userProfile = userProfileService.findProfile(profileId);
        int recommendedCalories = calorieService.calculateRecommendedCalories(
                CalorieRequest.from(userProfile)
        );
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        List<WeeklyReportDay> days = new ArrayList<>();
        for (int offset = 6; offset >= 0; offset--) {
            LocalDate day = end.minusDays(offset);
            int intakeCalories = mealLogService.calculateDailyTotalCalories(profileId, day);
            days.add(new WeeklyReportDay(day, recommendedCalories, intakeCalories));
        }

        return new WeeklyReportResponse(profileId, days);
    }
}
