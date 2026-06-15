package com.pdm.dietmanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.dto.request.WeightLogRequest;
import com.pdm.dietmanager.dto.response.UserProfileResponse;
import com.pdm.dietmanager.dto.response.WeightLogResponse;
import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class WeightLogServiceTest {
    @Autowired
    private WeightLogService weightLogService;

    @Autowired
    private UserProfileService userProfileService;

    @Test
    void recordsAndReturnsHistoryInDateOrder() {
        Long profileId = createProfile();

        weightLogService.recordWeight(WeightLogRequest.of(profileId, LocalDate.of(2026, 6, 10), 55.0));
        weightLogService.recordWeight(WeightLogRequest.of(profileId, LocalDate.of(2026, 6, 14), 54.2));

        List<WeightLogResponse> history = weightLogService.getWeightHistory(profileId);

        assertThat(history).hasSize(2);
        assertThat(history).extracting(WeightLogResponse::getWeight).containsExactly(55.0, 54.2);
    }

    @Test
    void recordingSameDateUpdatesInsteadOfDuplicating() {
        Long profileId = createProfile();
        LocalDate date = LocalDate.of(2026, 6, 15);

        weightLogService.recordWeight(WeightLogRequest.of(profileId, date, 55.0));
        weightLogService.recordWeight(WeightLogRequest.of(profileId, date, 53.5));

        List<WeightLogResponse> history = weightLogService.getWeightHistory(profileId);

        // 같은 날짜는 1건으로 유지되고 최신 값으로 갱신된다.
        assertThat(history).hasSize(1);
        assertThat(history.get(0).getWeight()).isEqualTo(53.5);
    }

    private Long createProfile() {
        UserProfileResponse profile = userProfileService.createProfile(UserProfileRequest.of(
                Gender.FEMALE, 23, 162.0, 55.0, ActivityLevel.NORMAL, GoalType.WEIGHT_LOSS
        ));
        return profile.getProfileId();
    }
}
