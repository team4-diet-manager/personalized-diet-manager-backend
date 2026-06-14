package com.pdm.dietmanager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 사용자의 목표 기준으로 음식의 적합도를 나타내는 신호등 등급.
 */
@Getter
@RequiredArgsConstructor
public enum FoodGrade {
    GREEN("권장"),
    YELLOW("적당히"),
    RED("주의");

    private final String description;
}
