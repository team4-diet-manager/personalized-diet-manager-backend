package com.pdm.dietmanager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityLevel {
    LOW(1.2, "저활동 (거의 운동 안 함)"),
    NORMAL(1.55, "보통 활동 (주 3~5회 운동)"),
    HIGH(1.725, "고활동 (주 6~7회 강도 높은 운동)");

    private final double multiplier;
    private final String description;
}
