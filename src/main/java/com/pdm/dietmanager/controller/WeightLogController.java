package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.request.WeightLogRequest;
import com.pdm.dietmanager.dto.response.ErrorResponse;
import com.pdm.dietmanager.dto.response.WeightLogResponse;
import com.pdm.dietmanager.service.WeightLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weight-logs")
@RequiredArgsConstructor
@Tag(name = "Weight Log", description = "체중 기록 및 변화 추적 API")
public class WeightLogController {
    private final WeightLogService weightLogService;

    @PostMapping
    @Operation(
            summary = "체중 기록",
            description = "특정 날짜의 체중을 기록한다. 같은 날짜에 이미 기록이 있으면 해당 값을 갱신한다."
    )
    @ApiResponse(responseCode = "200", description = "체중 기록 성공")
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
    public WeightLogResponse recordWeight(@Valid @RequestBody WeightLogRequest request) {
        return weightLogService.recordWeight(request);
    }

    @GetMapping
    @Operation(summary = "체중 기록 조회", description = "특정 사용자의 전체 체중 기록을 날짜 오름차순으로 조회한다.")
    @ApiResponse(responseCode = "200", description = "체중 기록 조회 성공")
    @ApiResponse(
            responseCode = "400",
            description = "필수 쿼리 파라미터 누락",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public List<WeightLogResponse> getWeightHistory(@RequestParam Long profileId) {
        return weightLogService.getWeightHistory(profileId);
    }
}
