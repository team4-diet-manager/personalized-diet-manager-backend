package com.pdm.dietmanager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Intensity {
    LOW("낮음"),
    MEDIUM("보통"),
    HIGH("높음");

    private final String description;
}
