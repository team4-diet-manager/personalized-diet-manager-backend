package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.request.MealLogRequest;
import com.pdm.dietmanager.dto.response.DailyMealLogResponse;
import com.pdm.dietmanager.dto.response.MealLogResponse;
import com.pdm.dietmanager.service.MealLogService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meal-logs")
public class MealLogController {
    private final MealLogService mealLogService;

    public MealLogController(MealLogService mealLogService) {
        this.mealLogService = mealLogService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MealLogResponse createMealLog(@Valid @RequestBody MealLogRequest request) {
        return mealLogService.createMealLog(request);
    }

    @GetMapping
    public DailyMealLogResponse getMealLogs(
            @RequestParam Long profileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<MealLogResponse> mealLogs = mealLogService.getMealLogsByDate(profileId, date);
        int dailyTotalCalories = mealLogService.calculateDailyTotalCalories(profileId, date);
        return new DailyMealLogResponse(profileId, date, mealLogs, dailyTotalCalories);
    }

    @PutMapping("/{mealLogId}")
    public MealLogResponse updateMealLog(
            @PathVariable Long mealLogId,
            @Valid @RequestBody MealLogRequest request
    ) {
        return mealLogService.updateMealLog(mealLogId, request);
    }

    @DeleteMapping("/{mealLogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMealLog(@PathVariable Long mealLogId) {
        mealLogService.deleteMealLog(mealLogId);
    }
}
