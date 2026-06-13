package com.pdm.dietmanager.dto.request;

import com.pdm.dietmanager.enums.MealType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "식단 기록 등록 및 수정 요청")
public class MealLogRequest {
    @Schema(description = "사용자 프로필 ID", example = "1")
    @NotNull(message = "사용자 프로필 ID는 필수 입력값입니다.")
    private Long profileId;

    @Schema(description = "식단 기록 날짜", example = "2026-06-09")
    @NotNull(message = "날짜는 필수 입력값입니다.")
    private LocalDate mealDate;

    @Schema(description = "식사 구분", example = "LUNCH")
    @NotNull(message = "식사 구분은 필수 입력값입니다.")
    private MealType mealType;

    @Schema(description = "음식 ID", example = "1")
    @NotNull(message = "음식 ID는 필수 입력값입니다.")
    private Long foodId;

    @Schema(description = "섭취 수량", example = "2")
    @Min(value = 1, message = "섭취 수량은 1 이상이어야 합니다.")
    private int quantity;

    public static MealLogRequest of(
            Long profileId,
            LocalDate mealDate,
            MealType mealType,
            Long foodId,
            int quantity
    ) {
        MealLogRequest request = new MealLogRequest();
        request.profileId = profileId;
        request.mealDate = mealDate;
        request.mealType = mealType;
        request.foodId = foodId;
        request.quantity = quantity;
        return request;
    }

    public Long getProfileId() {
        return profileId;
    }

    public LocalDate getMealDate() {
        return mealDate;
    }

    public MealType getMealType() {
        return mealType;
    }

    public Long getFoodId() {
        return foodId;
    }

    public int getQuantity() {
        return quantity;
    }
}
