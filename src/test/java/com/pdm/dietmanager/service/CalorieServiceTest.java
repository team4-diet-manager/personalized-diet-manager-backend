package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.dto.response.MacroNutrients;
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

    @Test
    void weightLossStrategyNeverRecommendsBelowBasalMetabolicRate() {
        // 저활동(1.2) 사용자는 TDEE에 0.8을 곱하면 BMR(1287) 아래로 떨어지므로,
        // 하한선 로직이 작동해 BMR 수준으로 보정되어야 한다.
        CalorieRequest request = createRequest(GoalType.WEIGHT_LOSS, ActivityLevel.LOW);

        int calories = calorieService.calculateRecommendedCalories(request);

        assertThat(calories).isEqualTo(1287);
    }

    @Test
    void muscleGainStrategyAppliesHigherSurplusForHighActivity() {
        // 고활동 사용자는 잉여 칼로리 배율이 1.20으로 높아진다.
        CalorieRequest request = createRequest(GoalType.MUSCLE_GAIN, ActivityLevel.HIGH);

        int calories = calorieService.calculateRecommendedCalories(request);

        assertThat(calories).isEqualTo(2663);
    }

    @Test
    void weightLossStrategyProducesHighProteinMacros() {
        // 다이어트 권장 1595kcal을 40/35/25 비율로 나누면
        // 단백질 160g(4kcal/g), 탄수 140g(4kcal/g), 지방 44g(9kcal/g)이 된다.
        CalorieRequest request = createRequest(GoalType.WEIGHT_LOSS);

        MacroNutrients macros = calorieService.calculateRecommendedMacros(request);

        assertThat(macros.getProteinGrams()).isEqualTo(160);
        assertThat(macros.getCarbGrams()).isEqualTo(140);
        assertThat(macros.getFatGrams()).isEqualTo(44);
    }

    @Test
    void macroRatiosDifferByGoal() {
        // 같은 신체 정보라도 목표 전략에 따라 매크로 구성이 달라진다.
        MacroNutrients weightLoss =
                calorieService.calculateRecommendedMacros(createRequest(GoalType.WEIGHT_LOSS));
        MacroNutrients muscleGain =
                calorieService.calculateRecommendedMacros(createRequest(GoalType.MUSCLE_GAIN));

        // 벌크업은 다이어트보다 탄수화물 그램이 더 많다.
        assertThat(muscleGain.getCarbGrams()).isGreaterThan(weightLoss.getCarbGrams());
    }

    private CalorieRequest createRequest(GoalType goalType) {
        return createRequest(goalType, ActivityLevel.NORMAL);
    }

    private CalorieRequest createRequest(GoalType goalType, ActivityLevel activityLevel) {
        return CalorieRequest.of(
                Gender.FEMALE,
                23,
                162.0,
                55.0,
                activityLevel,
                goalType
        );
    }
}
