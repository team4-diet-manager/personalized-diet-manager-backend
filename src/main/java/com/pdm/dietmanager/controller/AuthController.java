package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.request.LoginRequest;
import com.pdm.dietmanager.dto.request.SignupRequest;
import com.pdm.dietmanager.dto.response.ErrorResponse;
import com.pdm.dietmanager.dto.response.TokenResponse;
import com.pdm.dietmanager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "회원 가입 및 로그인을 담당하는 인증 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원 가입", description = "이메일, 비밀번호, 닉네임을 입력받아 새로운 회원 계정을 생성한다.")
    @ApiResponse(responseCode = "201", description = "회원 가입 성공")
    @ApiResponse(
            responseCode = "400",
            description = "입력값 검증 실패 또는 이미 존재하는 이메일",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public void signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호를 입력받아 인증 후 JWT Access Token을 발급한다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공 및 토큰 반환")
    @ApiResponse(
            responseCode = "400",
            description = "이메일 또는 비밀번호 불일치",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
