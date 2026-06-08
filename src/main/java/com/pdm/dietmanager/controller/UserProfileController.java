package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.dto.response.UserProfileResponse;
import com.pdm.dietmanager.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse createProfile(@Valid @RequestBody UserProfileRequest request) {
        return userProfileService.createProfile(request);
    }

    @GetMapping("/{profileId}")
    public UserProfileResponse getProfile(@PathVariable Long profileId) {
        return userProfileService.getProfile(profileId);
    }

    @PutMapping("/{profileId}")
    public UserProfileResponse updateProfile(
            @PathVariable Long profileId,
            @Valid @RequestBody UserProfileRequest request
    ) {
        return userProfileService.updateProfile(profileId, request);
    }
}
