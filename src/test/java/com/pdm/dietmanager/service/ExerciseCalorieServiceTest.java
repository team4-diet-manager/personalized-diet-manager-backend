package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.enums.ExerciseType;
import com.pdm.dietmanager.enums.Intensity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExerciseCalorieServiceTest {
    @Autowired
    private ExerciseCalorieService exerciseCalorieService;

    @Test
    void runningBurnIncreasesWithIntensity() {
        // 달리기: 강도가 높을수록 소모 칼로리가 커진다(MET 배수 적용).
        int low = exerciseCalorieService.calculateBurnedCalories(
                ExerciseType.RUNNING, 60.0, 30, Intensity.LOW);
        int high = exerciseCalorieService.calculateBurnedCalories(
                ExerciseType.RUNNING, 60.0, 30, Intensity.HIGH);

        assertThat(low).isEqualTo(210);   // 7.0 MET × 60kg × 0.5h
        assertThat(high).isGreaterThan(low);
    }

    @Test
    void weightTrainingIgnoresIntensity() {
        // 근력운동: 강도와 무관하게 동일 MET를 쓰므로 결과가 같다.
        int low = exerciseCalorieService.calculateBurnedCalories(
                ExerciseType.WEIGHT_TRAINING, 70.0, 60, Intensity.LOW);
        int high = exerciseCalorieService.calculateBurnedCalories(
                ExerciseType.WEIGHT_TRAINING, 70.0, 60, Intensity.HIGH);

        assertThat(low).isEqualTo(350);   // 5.0 MET × 70kg × 1h
        assertThat(high).isEqualTo(low);
    }
}
