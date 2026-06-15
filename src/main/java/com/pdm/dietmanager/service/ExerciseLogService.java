package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.request.ExerciseLogRequest;
import com.pdm.dietmanager.dto.response.DailyExerciseLogResponse;
import com.pdm.dietmanager.dto.response.ExerciseLogResponse;
import com.pdm.dietmanager.entity.ExerciseLog;
import com.pdm.dietmanager.entity.User;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.exception.ResourceNotFoundException;
import com.pdm.dietmanager.repository.ExerciseLogRepository;
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
    private final UserProfileService userProfileService;
    private final ExerciseCalorieService exerciseCalorieService;

    @Transactional
    public ExerciseLogResponse createExerciseLog(ExerciseLogRequest request) {
        return createExerciseLog(userProfileService.findProfile(request.getProfileId()), request);
    }

    @Transactional
    public ExerciseLogResponse createExerciseLog(User user, ExerciseLogRequest request) {
        return createExerciseLog(userProfileService.findProfileByUser(user), request);
    }

    private ExerciseLogResponse createExerciseLog(UserProfile userProfile, ExerciseLogRequest request) {

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

    public List<ExerciseLogResponse> getExerciseLogsByDate(User user, LocalDate exerciseDate) {
        UserProfile userProfile = userProfileService.findProfileByUser(user);
        return getExerciseLogsByDate(userProfile.getProfileId(), exerciseDate);
    }

    public DailyExerciseLogResponse getDailyExerciseLogs(User user, LocalDate exerciseDate) {
        UserProfile userProfile = userProfileService.findProfileByUser(user);
        Long profileId = userProfile.getProfileId();
        List<ExerciseLogResponse> logs = getExerciseLogsByDate(profileId, exerciseDate);
        int dailyTotalBurned = calculateDailyBurnedCalories(profileId, exerciseDate);
        return new DailyExerciseLogResponse(profileId, exerciseDate, logs, dailyTotalBurned);
    }

    public int calculateDailyBurnedCalories(Long profileId, LocalDate exerciseDate) {
        return exerciseLogRepository
                .sumBurnedCaloriesByProfileIdAndExerciseDate(profileId, exerciseDate)
                .intValue();
    }

    public int calculateDailyBurnedCalories(User user, LocalDate exerciseDate) {
        UserProfile userProfile = userProfileService.findProfileByUser(user);
        return calculateDailyBurnedCalories(userProfile.getProfileId(), exerciseDate);
    }

    @Transactional
    public void deleteExerciseLog(Long exerciseLogId) {
        ExerciseLog exerciseLog = exerciseLogRepository.findById(exerciseLogId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "운동 기록을 찾을 수 없습니다. exerciseLogId=" + exerciseLogId
                ));
        exerciseLogRepository.delete(exerciseLog);
    }

    @Transactional
    public void deleteExerciseLog(User user, Long exerciseLogId) {
        ExerciseLog exerciseLog = exerciseLogRepository.findById(exerciseLogId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "운동 기록을 찾을 수 없습니다. exerciseLogId=" + exerciseLogId
                ));
        if (!exerciseLog.getUserProfile().getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("해당 운동 기록을 삭제할 권한이 없습니다.");
        }
        exerciseLogRepository.delete(exerciseLog);
    }
}
