package com.pdm.dietmanager.dto.request;

import com.pdm.dietmanager.enums.MealType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class MealLogRequest {
    @NotNull(message = "사용자 프로필 ID는 필수 입력값입니다.")
    private Long profileId;

    @NotNull(message = "날짜는 필수 입력값입니다.")
    private LocalDate mealDate;

    @NotNull(message = "식사 구분은 필수 입력값입니다.")
    private MealType mealType;

    @NotNull(message = "음식 ID는 필수 입력값입니다.")
    private Long foodId;

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
