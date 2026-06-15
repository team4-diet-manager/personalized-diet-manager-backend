package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.dto.response.UserProfileResponse;
import com.pdm.dietmanager.entity.User;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.exception.ResourceNotFoundException;
import com.pdm.dietmanager.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public UserProfileResponse createProfile(User user, UserProfileRequest request) {
        if (userProfileRepository.existsByUser(user)) {
            throw new IllegalStateException("이미 프로필이 존재합니다.");
        }

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .name(request.getName())
                .gender(request.getGender())
                .age(request.getAge())
                .height(request.getHeight())
                .weight(request.getWeight())
                .activityLevel(request.getActivityLevel())
                .goalType(request.getGoalType())
                .build();

        UserProfile savedProfile = userProfileRepository.save(userProfile);
        return UserProfileResponse.from(savedProfile);
    }

    public UserProfileResponse getProfile(Long profileId) {
        return UserProfileResponse.from(findProfile(profileId));
    }

    public UserProfileResponse getProfileByUser(User user) {
        return UserProfileResponse.from(findProfileByUser(user));
    }

    public UserProfile findProfileByUser(User user) {
        return userProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("프로필이 등록되지 않은 사용자입니다."));
    }

    @Transactional
    public UserProfileResponse updateProfileByUser(User user, UserProfileRequest request) {
        UserProfile userProfile = findProfileByUser(user);
        userProfile.update(
                request.getName(),
                request.getGender(),
                request.getAge(),
                request.getHeight(),
                request.getWeight(),
                request.getActivityLevel(),
                request.getGoalType()
        );
        return UserProfileResponse.from(userProfile);
    }

    @Transactional
    public UserProfileResponse updateProfile(Long profileId, UserProfileRequest request) {
        UserProfile userProfile = findProfile(profileId);
        userProfile.update(
                request.getName(),
                request.getGender(),
                request.getAge(),
                request.getHeight(),
                request.getWeight(),
                request.getActivityLevel(),
                request.getGoalType()
        );
        return UserProfileResponse.from(userProfile);
    }

    public UserProfile findProfile(Long profileId) {
        return userProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "사용자 프로필을 찾을 수 없습니다. profileId=" + profileId
                ));
    }
}
