package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.request.WeightLogRequest;
import com.pdm.dietmanager.dto.response.WeightLogResponse;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.entity.WeightLog;
import com.pdm.dietmanager.exception.ResourceNotFoundException;
import com.pdm.dietmanager.repository.UserProfileRepository;
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
    private final UserProfileRepository userProfileRepository;

    /** 같은 날짜의 기록이 있으면 체중을 갱신하고, 없으면 새로 등록한다(날짜별 1건 유지). */
    @Transactional
    public WeightLogResponse recordWeight(WeightLogRequest request) {
        UserProfile userProfile = findProfile(request.getProfileId());

        WeightLog weightLog = weightLogRepository
                .findByUserProfile_ProfileIdAndLogDate(request.getProfileId(), request.getLogDate())
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

    private UserProfile findProfile(Long profileId) {
        return userProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "사용자 프로필을 찾을 수 없습니다. profileId=" + profileId
                ));
    }
}
