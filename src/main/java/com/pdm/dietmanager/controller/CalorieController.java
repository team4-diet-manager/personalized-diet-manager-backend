package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.dto.response.CalorieResponse;
import com.pdm.dietmanager.service.CalorieService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calories")
public class CalorieController {
    private final CalorieService calorieService;

    public CalorieController(CalorieService calorieService) {
        this.calorieService = calorieService;
    }

    @PostMapping("/recommendation")
    public CalorieResponse calculateRecommendation(@Valid @RequestBody CalorieRequest request) {
        return calorieService.calculateRecommendation(request);
    }
}
