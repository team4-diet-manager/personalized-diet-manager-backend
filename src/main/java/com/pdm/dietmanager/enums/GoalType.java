package com.pdm.dietmanager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GoalType {
    WEIGHT_LOSS("다이어트"),
    MUSCLE_GAIN("벌크업"),
    MAINTAIN("체중 유지");

    private final String description;
}
