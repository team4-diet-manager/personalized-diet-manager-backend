package com.pdm.dietmanager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExerciseType {
    WALKING("걷기"),
    RUNNING("달리기"),
    CYCLING("자전거"),
    WEIGHT_TRAINING("근력운동"),
    SWIMMING("수영");

    private final String description;
}
