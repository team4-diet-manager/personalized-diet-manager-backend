package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.request.WeightLogRequest;
import com.pdm.dietmanager.dto.response.WeightLogResponse;
import com.pdm.dietmanager.entity.User;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.entity.WeightLog;
import com.pdm.dietmanager.repository.WeightLogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WeightLogService {
    private final WeightLogRepository weightLogRepository;
    private final UserProfileService userProfileService;

    /** 같은 날짜의 기록이 있으면 체중을 갱신하고, 없으면 새로 등록한다(날짜별 1건 유지). */
    @Transactional
    public WeightLogResponse recordWeight(WeightLogRequest request) {
        return recordWeight(userProfileService.findProfile(request.getProfileId()), request);
    }

    @Transactional
    public WeightLogResponse recordWeight(User user, WeightLogRequest request) {
        return recordWeight(userProfileService.findProfileByUser(user), request);
    }

    private WeightLogResponse recordWeight(UserProfile userProfile, WeightLogRequest request) {
        Long profileId = userProfile.getProfileId();

        WeightLog weightLog = weightLogRepository
                .findByUserProfile_ProfileIdAndLogDate(profileId, request.getLogDate())
                .map(existing -> {
                    existing.changeWeight(request.getWeight());
                    return existing;
                })
                .orElseGet(() -> weightLogRepository.save(WeightLog.builder()
                        .userProfile(userProfile)
                        .logDate(request.getLogDate())
                        .weight(request.getWeight())
                        .build()));

        return WeightLogResponse.from(weightLog);
    }

    public List<WeightLogResponse> getWeightHistory(Long profileId) {
        return weightLogRepository.findByUserProfile_ProfileIdOrderByLogDateAsc(profileId)
                .stream()
                .map(WeightLogResponse::from)
                .toList();
    }

    public List<WeightLogResponse> getWeightHistory(User user) {
        UserProfile userProfile = userProfileService.findProfileByUser(user);
        return getWeightHistory(userProfile.getProfileId());
    }
}
