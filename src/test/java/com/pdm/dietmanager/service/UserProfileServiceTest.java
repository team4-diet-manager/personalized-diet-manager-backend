package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.dto.response.UserProfileResponse;
import com.pdm.dietmanager.entity.User;
import com.pdm.dietmanager.repository.UserRepository;
import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserProfileServiceTest {
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createAndGetProfile() {
        User user = User.builder().email("test@example.com").password("pass").nickname("nick").build();
        userRepository.save(user);

        UserProfileRequest request = UserProfileRequest.of(
                "지현",
                Gender.FEMALE,
                23,
                162.0,
                55.0,
                ActivityLevel.NORMAL,
                GoalType.WEIGHT_LOSS
        );

        UserProfileResponse createdProfile = userProfileService.createProfile(user, request);
        UserProfileResponse foundProfile = userProfileService.getProfileByUser(user);

        assertThat(foundProfile.getProfileId()).isEqualTo(createdProfile.getProfileId());
        assertThat(foundProfile.getName()).isEqualTo("지현");
        assertThat(foundProfile.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(foundProfile.getGoalType()).isEqualTo(GoalType.WEIGHT_LOSS);
    }

    @Test
    void updateProfileReflectsAllFieldsIncludingGender() {
        User user = User.builder().email("update@example.com").password("pass").nickname("nick").build();
        userRepository.save(user);

        UserProfileResponse created = userProfileService.createProfile(user, UserProfileRequest.of(
                "지현",
                Gender.FEMALE,
                23,
                162.0,
                55.0,
                ActivityLevel.NORMAL,
                GoalType.WEIGHT_LOSS
        ));

        UserProfileResponse updated = userProfileService.updateProfileByUser(
                user,
                UserProfileRequest.of(
                        "민준",
                        Gender.MALE,
                        30,
                        178.0,
                        72.0,
                        ActivityLevel.HIGH,
                        GoalType.MUSCLE_GAIN
                )
        );

        // 수정 시 이름·성별을 포함한 모든 필드가 반영되어야 한다(과거 gender 누락 버그 회귀 방지).
        assertThat(updated.getProfileId()).isEqualTo(created.getProfileId());
        assertThat(updated.getName()).isEqualTo("민준");
        assertThat(updated.getGender()).isEqualTo(Gender.MALE);
        assertThat(updated.getAge()).isEqualTo(30);
        assertThat(updated.getHeight()).isEqualTo(178.0);
        assertThat(updated.getWeight()).isEqualTo(72.0);
        assertThat(updated.getActivityLevel()).isEqualTo(ActivityLevel.HIGH);
        assertThat(updated.getGoalType()).isEqualTo(GoalType.MUSCLE_GAIN);
    }
}
