package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.strategy.MaintainStrategy;
import com.pdm.dietmanager.strategy.MuscleGainStrategy;
import com.pdm.dietmanager.strategy.WeightLossStrategy;
import java.util.List;
import org.junit.jupiter.api.Test;

class CalorieServiceTest {
    private final CalorieService calorieService = new CalorieService(List.of(
            new WeightLossStrategy(),
            new MuscleGainStrategy(),
            new MaintainStrategy()
    ));

    @Test
    void calculateRecommendedCaloriesAppliesWeightLossStrategy() {
        CalorieRequest request = createRequest(GoalType.WEIGHT_LOSS);

        int calories = calorieService.calculateRecommendedCalories(request);

        assertThat(calories).isEqualTo(1595);
    }

    @Test
    void calculateRecommendedCaloriesAppliesMuscleGainStrategy() {
        CalorieRequest request = createRequest(GoalType.MUSCLE_GAIN);

        int calories = calorieService.calculateRecommendedCalories(request);

        assertThat(calories).isEqualTo(2293);
    }

    @Test
    void calculateRecommendedCaloriesAppliesMaintainStrategy() {
        CalorieRequest request = createRequest(GoalType.MAINTAIN);

        int calories = calorieService.calculateRecommendedCalories(request);

        assertThat(calories).isEqualTo(1994);
    }

    private CalorieRequest createRequest(GoalType goalType) {
        return CalorieRequest.of(
                Gender.FEMALE,
                23,
                162.0,
                55.0,
                ActivityLevel.NORMAL,
                goalType
        );
    }
}
