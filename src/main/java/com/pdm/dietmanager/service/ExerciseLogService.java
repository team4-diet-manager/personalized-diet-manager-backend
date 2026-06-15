package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.request.ExerciseLogRequest;
import com.pdm.dietmanager.dto.response.ExerciseLogResponse;
import com.pdm.dietmanager.entity.ExerciseLog;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.exception.ResourceNotFoundException;
import com.pdm.dietmanager.repository.ExerciseLogRepository;
import com.pdm.dietmanager.repository.UserProfileRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExerciseLogService {
    private final ExerciseLogRepository exerciseLogRepository;
    private final UserProfileRepository userProfileRepository;
    private final ExerciseCalorieService exerciseCalorieService;

    @Transactional
    public ExerciseLogResponse createExerciseLog(ExerciseLogRequest request) {
        UserProfile userProfile = findProfile(request.getProfileId());

        // 프로필 체중 기준으로 종류별 전략을 적용해 소모 칼로리를 계산한다.
        int burnedCalories = exerciseCalorieService.calculateBurnedCalories(
                request.getExerciseType(),
                userProfile.getWeight(),
                request.getDurationMinutes(),
                request.getIntensity()
        );

        ExerciseLog exerciseLog = exerciseLogRepository.save(ExerciseLog.builder()
                .userProfile(userProfile)
                .exerciseDate(request.getExerciseDate())
                .exerciseType(request.getExerciseType())
                .durationMinutes(request.getDurationMinutes())
                .intensity(request.getIntensity())
                .burnedCalories(burnedCalories)
                .build());

        return ExerciseLogResponse.from(exerciseLog);
    }

    public List<ExerciseLogResponse> getExerciseLogsByDate(Long profileId, LocalDate exerciseDate) {
        return exerciseLogRepository
                .findByUserProfile_ProfileIdAndExerciseDate(profileId, exerciseDate)
                .stream()
                .map(ExerciseLogResponse::from)
                .toList();
    }

    public int calculateDailyBurnedCalories(Long profileId, LocalDate exerciseDate) {
        return exerciseLogRepository
                .sumBurnedCaloriesByProfileIdAndExerciseDate(profileId, exerciseDate)
                .intValue();
    }

    @Transactional
    public void deleteExerciseLog(Long exerciseLogId) {
        ExerciseLog exerciseLog = exerciseLogRepository.findById(exerciseLogId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "운동 기록을 찾을 수 없습니다. exerciseLogId=" + exerciseLogId
                ));
        exerciseLogRepository.delete(exerciseLog);
    }

    private UserProfile findProfile(Long profileId) {
        return userProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "사용자 프로필을 찾을 수 없습니다. profileId=" + profileId
                ));
    }
}
