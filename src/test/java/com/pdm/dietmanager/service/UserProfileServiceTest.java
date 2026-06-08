package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.dto.response.UserProfileResponse;
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

    @Test
    void createAndGetProfile() {
        UserProfileRequest request = UserProfileRequest.of(
                Gender.FEMALE,
                23,
                162.0,
                55.0,
                ActivityLevel.NORMAL,
                GoalType.WEIGHT_LOSS
        );

        UserProfileResponse createdProfile = userProfileService.createProfile(request);
        UserProfileResponse foundProfile = userProfileService.getProfile(createdProfile.getProfileId());

        assertThat(foundProfile.getProfileId()).isEqualTo(createdProfile.getProfileId());
        assertThat(foundProfile.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(foundProfile.getGoalType()).isEqualTo(GoalType.WEIGHT_LOSS);
    }
}
