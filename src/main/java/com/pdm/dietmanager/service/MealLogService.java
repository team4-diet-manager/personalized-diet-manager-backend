package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.request.MealLogRequest;
import com.pdm.dietmanager.dto.response.DailyMealLogResponse;
import com.pdm.dietmanager.dto.response.MacroNutrients;
import com.pdm.dietmanager.dto.response.MealLogResponse;
import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.entity.MealLog;
import com.pdm.dietmanager.entity.User;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.exception.ResourceNotFoundException;
import com.pdm.dietmanager.repository.FoodRepository;
import com.pdm.dietmanager.repository.MealLogRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MealLogService {
    private final MealLogRepository mealLogRepository;
    private final UserProfileService userProfileService;
    private final FoodRepository foodRepository;

    @Transactional
    public MealLogResponse createMealLog(MealLogRequest request) {
        return createMealLog(userProfileService.findProfile(request.getProfileId()), request);
    }

    @Transactional
    public MealLogResponse createMealLog(User user, MealLogRequest request) {
        return createMealLog(userProfileService.findProfileByUser(user), request);
    }

    private MealLogResponse createMealLog(UserProfile userProfile, MealLogRequest request) {
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

    public List<MealLogResponse> getMealLogsByDate(User user, LocalDate mealDate) {
        UserProfile userProfile = userProfileService.findProfileByUser(user);
        return mealLogRepository.findByUserProfile_ProfileIdAndMealDate(userProfile.getProfileId(), mealDate)
                .stream()
                .map(MealLogResponse::from)
                .toList();
    }

    /** 기간 내 식단을 기록한 날짜를 최신순으로 반환한다(연속 기록 스트릭 계산용). */
    public List<LocalDate> getLoggedDates(Long profileId, LocalDate startDate, LocalDate endDate) {
        return mealLogRepository.findDistinctMealDatesBetween(profileId, startDate, endDate);
    }

    public DailyMealLogResponse getDailyMealLogs(User user, LocalDate mealDate) {
        UserProfile userProfile = userProfileService.findProfileByUser(user);
        List<MealLogResponse> mealLogs = mealLogRepository.findByUserProfile_ProfileIdAndMealDate(userProfile.getProfileId(), mealDate)
                .stream()
                .map(MealLogResponse::from)
                .toList();
        Long totalCalories = mealLogRepository.sumTotalCaloriesByProfileIdAndMealDate(
                userProfile.getProfileId(),
                mealDate
        );
        return new DailyMealLogResponse(userProfile.getProfileId(), mealDate, mealLogs, totalCalories.intValue());
    }

    public int calculateDailyTotalCalories(Long profileId, LocalDate mealDate) {
        Long totalCalories = mealLogRepository.sumTotalCaloriesByProfileIdAndMealDate(
                profileId,
                mealDate
        );
        return totalCalories.intValue();
    }

    public int calculateDailyTotalCalories(User user, LocalDate mealDate) {
        UserProfile userProfile = userProfileService.findProfileByUser(user);
        return calculateDailyTotalCalories(userProfile.getProfileId(), mealDate);
    }

    /**
     * 특정 날짜에 실제로 섭취한 탄단지(매크로) 그램을 음식별 수량만큼 합산한다.
     * 권장 매크로와 비교할 "실제 섭취 매크로"를 제공한다.
     */
    public MacroNutrients calculateDailyIntakeMacros(Long profileId, LocalDate mealDate) {
        List<MealLog> mealLogs = mealLogRepository.findByUserProfile_ProfileIdAndMealDate(
                profileId,
                mealDate
        );

        int protein = 0;
        int carb = 0;
        int fat = 0;
        for (MealLog mealLog : mealLogs) {
            Food food = mealLog.getFood();
            protein += food.getProteinGrams() * mealLog.getQuantity();
            carb += food.getCarbGrams() * mealLog.getQuantity();
            fat += food.getFatGrams() * mealLog.getQuantity();
        }

        return new MacroNutrients(protein, carb, fat);
    }

    public MacroNutrients calculateDailyIntakeMacros(User user, LocalDate mealDate) {
        UserProfile userProfile = userProfileService.findProfileByUser(user);
        return calculateDailyIntakeMacros(userProfile.getProfileId(), mealDate);
    }

    @Transactional
    public MealLogResponse updateMealLog(User user, Long mealLogId, MealLogRequest request) {
        MealLog mealLog = findMealLog(mealLogId);
        if (!mealLog.getUserProfile().getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("해당 기록을 수정할 권한이 없습니다.");
        }
        Food food = findFood(request.getFoodId());

        mealLog.update(food, request.getQuantity(), request.getMealType());
        return MealLogResponse.from(mealLog);
    }

    @Transactional
    public void deleteMealLog(User user, Long mealLogId) {
        MealLog mealLog = findMealLog(mealLogId);
        if (!mealLog.getUserProfile().getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("해당 기록을 삭제할 권한이 없습니다.");
        }
        mealLogRepository.delete(mealLog);
    }

    private MealLog findMealLog(Long mealLogId) {
        return mealLogRepository.findById(mealLogId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "식단 기록을 찾을 수 없습니다. mealLogId=" + mealLogId
                ));
    }

    private Food findFood(Long foodId) {
        return foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "음식을 찾을 수 없습니다. foodId=" + foodId
                ));
    }
}
