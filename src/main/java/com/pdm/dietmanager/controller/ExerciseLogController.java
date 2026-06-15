package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.request.ExerciseLogRequest;
import com.pdm.dietmanager.dto.response.DailyExerciseLogResponse;
import com.pdm.dietmanager.dto.response.ErrorResponse;
import com.pdm.dietmanager.dto.response.ExerciseLogResponse;
import com.pdm.dietmanager.service.ExerciseLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercise-logs")
@RequiredArgsConstructor
@Tag(name = "Exercise Log", description = "운동 기록 및 소모 칼로리 API (Strategy 패턴)")
public class ExerciseLogController {
    private final ExerciseLogService exerciseLogService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "운동 기록 등록",
            description = "운동 종류·시간·강도를 입력하면 종류별 전략으로 소모 칼로리를 계산해 저장한다."
    )
    @ApiResponse(responseCode = "201", description = "운동 기록 등록 성공")
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
    public ExerciseLogResponse createExerciseLog(@Valid @RequestBody ExerciseLogRequest request) {
        return exerciseLogService.createExerciseLog(request);
    }

    @GetMapping
    @Operation(summary = "날짜별 운동 기록 조회", description = "특정 사용자의 특정 날짜 운동 기록과 총 소모 칼로리를 조회한다.")
    @ApiResponse(responseCode = "200", description = "운동 기록 조회 성공")
    @ApiResponse(
            responseCode = "400",
            description = "필수 쿼리 파라미터 누락 또는 날짜 형식 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public DailyExerciseLogResponse getExerciseLogs(
            @RequestParam Long profileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<ExerciseLogResponse> logs = exerciseLogService.getExerciseLogsByDate(profileId, date);
        int dailyTotalBurned = exerciseLogService.calculateDailyBurnedCalories(profileId, date);
        return new DailyExerciseLogResponse(profileId, date, logs, dailyTotalBurned);
    }

    @DeleteMapping("/{exerciseLogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "운동 기록 삭제", description = "운동 기록 ID로 기존 기록을 삭제한다.")
    @ApiResponse(responseCode = "204", description = "운동 기록 삭제 성공")
    @ApiResponse(
            responseCode = "404",
            description = "운동 기록을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public void deleteExerciseLog(@PathVariable Long exerciseLogId) {
        exerciseLogService.deleteExerciseLog(exerciseLogId);
    }
}
