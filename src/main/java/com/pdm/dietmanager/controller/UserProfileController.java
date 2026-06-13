package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.dto.response.ErrorResponse;
import com.pdm.dietmanager.dto.response.UserProfileResponse;
import com.pdm.dietmanager.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Profile", description = "사용자 신체 정보와 목표를 관리하는 API")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "사용자 프로필 등록", description = "성별, 나이, 키, 몸무게, 활동량, 목표를 저장한다.")
    @ApiResponse(responseCode = "201", description = "프로필 등록 성공")
    @ApiResponse(
            responseCode = "400",
            description = "입력값 검증 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public UserProfileResponse createProfile(@Valid @RequestBody UserProfileRequest request) {
        return userProfileService.createProfile(request);
    }

    @GetMapping("/{profileId}")
    @Operation(summary = "사용자 프로필 조회", description = "프로필 ID로 저장된 신체 정보와 목표를 조회한다.")
    @ApiResponse(responseCode = "200", description = "프로필 조회 성공")
    @ApiResponse(
            responseCode = "404",
            description = "프로필을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public UserProfileResponse getProfile(@PathVariable Long profileId) {
        return userProfileService.getProfile(profileId);
    }

    @PutMapping("/{profileId}")
    @Operation(summary = "사용자 프로필 수정", description = "기존 프로필의 신체 정보, 활동량, 목표를 수정한다.")
    @ApiResponse(responseCode = "200", description = "프로필 수정 성공")
    @ApiResponse(
            responseCode = "400",
            description = "입력값 검증 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "프로필을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public UserProfileResponse updateProfile(
            @PathVariable Long profileId,
            @Valid @RequestBody UserProfileRequest request
    ) {
        return userProfileService.updateProfile(profileId, request);
    }
}
