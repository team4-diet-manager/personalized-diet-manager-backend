package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.enums.ExerciseType;
import com.pdm.dietmanager.enums.Intensity;

/**
 * 운동 종류별 소모 칼로리 계산 전략.
 * 칼로리 계산({@link CalorieStrategy})·음식 분류({@link FoodGradeStrategy})에 이어
 * 전략 패턴을 세 번째 책임(운동 소모)에 적용한 사례다.
 *
 * <p>기본 공식은 MET 기반(kcal = MET × 체중 × 시간)이지만,
 * 강도(Intensity)를 MET에 반영하는 방식이 운동마다 달라 전략으로 분리한다.</p>
 */
public interface ExerciseCalorieStrategy {
    ExerciseType getType();

    int calculateBurnedCalories(double weightKg, int minutes, Intensity intensity);

    /** MET × 체중(kg) × 시간(h) 공식으로 소모 칼로리를 구한다. */
    default int byMet(double met, double weightKg, int minutes) {
        return (int) Math.round(met * weightKg * (minutes / 60.0));
    }
}
