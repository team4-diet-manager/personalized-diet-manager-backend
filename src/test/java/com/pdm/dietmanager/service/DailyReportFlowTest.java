package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.dto.request.MealLogRequest;
import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.dto.response.MacroNutrients;
import com.pdm.dietmanager.dto.response.UserProfileResponse;
import com.pdm.dietmanager.entity.User;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.repository.UserRepository;
import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.enums.MealType;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class DailyReportFlowTest {
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private MealLogService mealLogService;

    @Autowired
    private CalorieService calorieService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void calculateRecommendationAndDailyIntakeGap() {
        User user = User.builder().email("report@example.com").password("pass").nickname("nick").build();
        userRepository.save(user);

        UserProfileResponse profile = userProfileService.createProfile(user, UserProfileRequest.of(
                "지현",
                Gender.FEMALE,
                23,
                162.0,
                55.0,
                ActivityLevel.NORMAL,
                GoalType.WEIGHT_LOSS
        ));
        LocalDate mealDate = LocalDate.of(2026, 6, 9);

        mealLogService.createMealLog(user, MealLogRequest.of(
                profile.getProfileId(),
                mealDate,
                MealType.LUNCH,
                1L,
                1
        ));

        UserProfile userProfile = userProfileService.findProfile(profile.getProfileId());
        int recommendedCalories = calorieService.calculateRecommendation(
                CalorieRequest.from(userProfile)
        ).getRecommendedCalories();
        int dailyTotalCalories = mealLogService.calculateDailyTotalCalories(
                user,
                mealDate
        );
        MacroNutrients intakeMacros = mealLogService.calculateDailyIntakeMacros(
                user,
                mealDate
        );

        assertThat(recommendedCalories).isEqualTo(1595);
        assertThat(dailyTotalCalories).isEqualTo(165);
        assertThat(dailyTotalCalories - recommendedCalories).isEqualTo(-1430);
        // 닭가슴살(foodId=1) 1개 섭취 시 매크로(P31/C0/F4)가 합산되어야 한다.
        assertThat(intakeMacros.getProteinGrams()).isEqualTo(31);
        assertThat(intakeMacros.getCarbGrams()).isZero();
        assertThat(intakeMacros.getFatGrams()).isEqualTo(4);
    }
}
