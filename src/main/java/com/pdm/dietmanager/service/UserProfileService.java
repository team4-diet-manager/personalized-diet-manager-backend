package com.pdm.dietmanager.service;

import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.dto.response.UserProfileResponse;
import com.pdm.dietmanager.entity.UserProfile;
import com.pdm.dietmanager.exception.ResourceNotFoundException;
import com.pdm.dietmanager.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public UserProfileResponse createProfile(UserProfileRequest request) {
        UserProfile userProfile = UserProfile.builder()
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

    @Transactional
    public UserProfileResponse updateProfile(Long profileId, UserProfileRequest request) {
        UserProfile userProfile = findProfile(profileId);
        userProfile.update(
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
