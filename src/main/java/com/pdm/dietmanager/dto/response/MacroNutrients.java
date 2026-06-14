package com.pdm.dietmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "목표별 권장 탄수화물·단백질·지방(매크로) 그램")
public class MacroNutrients {
    private static final double KCAL_PER_GRAM_PROTEIN = 4.0;
    private static final double KCAL_PER_GRAM_CARB = 4.0;
    private static final double KCAL_PER_GRAM_FAT = 9.0;

    @Schema(description = "권장 단백질(g)", example = "160")
    private final int proteinGrams;

    @Schema(description = "권장 탄수화물(g)", example = "140")
    private final int carbGrams;

    @Schema(description = "권장 지방(g)", example = "44")
    private final int fatGrams;

    public MacroNutrients(int proteinGrams, int carbGrams, int fatGrams) {
        this.proteinGrams = proteinGrams;
        this.carbGrams = carbGrams;
        this.fatGrams = fatGrams;
    }

    /**
     * 총 칼로리와 목표별 매크로 비율로부터 그램 수를 계산한다.
     * 단백질·탄수화물은 1g당 4kcal, 지방은 1g당 9kcal 기준이다.
     */
    public static MacroNutrients of(int calories, double proteinRatio, double carbRatio, double fatRatio) {
        int protein = (int) Math.round(calories * proteinRatio / KCAL_PER_GRAM_PROTEIN);
        int carb = (int) Math.round(calories * carbRatio / KCAL_PER_GRAM_CARB);
        int fat = (int) Math.round(calories * fatRatio / KCAL_PER_GRAM_FAT);
        return new MacroNutrients(protein, carb, fat);
    }
}
