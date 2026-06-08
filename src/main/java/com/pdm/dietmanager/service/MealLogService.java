package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.request.MealLogRequest;
import com.pdm.dietmanager.dto.response.MealLogResponse;
import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.entity.MealLog;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.exception.ResourceNotFoundException;
import com.pdm.dietmanager.repository.FoodRepository;
import com.pdm.dietmanager.repository.MealLogRepository;
import com.pdm.dietmanager.repository.UserProfileRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MealLogService {
    private final MealLogRepository mealLogRepository;
    private final UserProfileRepository userProfileRepository;
    private final FoodRepository foodRepository;

    public MealLogService(
            MealLogRepository mealLogRepository,
            UserProfileRepository userProfileRepository,
            FoodRepository foodRepository
    ) {
        this.mealLogRepository = mealLogRepository;
        this.userProfileRepository = userProfileRepository;
        this.foodRepository = foodRepository;
    }

    @Transactional
    public MealLogResponse createMealLog(MealLogRequest request) {
        UserProfile userProfile = findProfile(request.getProfileId());
        Food food = findFood(request.getFoodId());

        MealLog mealLog = MealLog.builder()
                .userProfile(userProfile)
                .food(food)
                .mealDate(request.getMealDate())
                .mealType(request.getMealType())
                .quantity(request.getQuantity())
                .build();

        MealLog savedMealLog = mealLogRepository.save(mealLog);
        return MealLogResponse.from(savedMealLog);
    }

    public List<MealLogResponse> getMealLogsByDate(Long profileId, LocalDate mealDate) {
        return mealLogRepository.findByUserProfile_ProfileIdAndMealDate(profileId, mealDate)
                .stream()
                .map(MealLogResponse::from)
                .toList();
    }

    public int calculateDailyTotalCalories(Long profileId, LocalDate mealDate) {
        Long totalCalories = mealLogRepository.sumTotalCaloriesByProfileIdAndMealDate(
                profileId,
                mealDate
        );
        return totalCalories.intValue();
    }

    @Transactional
    public MealLogResponse updateMealLog(Long mealLogId, MealLogRequest request) {
        MealLog mealLog = findMealLog(mealLogId);
        Food food = findFood(request.getFoodId());

        mealLog.update(food, request.getQuantity(), request.getMealType());
        return MealLogResponse.from(mealLog);
    }

    @Transactional
    public void deleteMealLog(Long mealLogId) {
        MealLog mealLog = findMealLog(mealLogId);
        mealLogRepository.delete(mealLog);
    }

    private MealLog findMealLog(Long mealLogId) {
        return mealLogRepository.findById(mealLogId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "식단 기록을 찾을 수 없습니다. mealLogId=" + mealLogId
                ));
    }

    private UserProfile findProfile(Long profileId) {
        return userProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "사용자 프로필을 찾을 수 없습니다. profileId=" + profileId
                ));
    }

    private Food findFood(Long foodId) {
        return foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "음식을 찾을 수 없습니다. foodId=" + foodId
                ));
    }
}
