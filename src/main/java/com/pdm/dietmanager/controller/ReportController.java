package com.pdm.dietmanager.controller;

import com.pdm.dietmanager.dto.response.DailyReportResponse;
import com.pdm.dietmanager.dto.response.ErrorResponse;
import com.pdm.dietmanager.dto.response.StatsResponse;
import com.pdm.dietmanager.dto.response.WeeklyReportResponse;
import com.pdm.dietmanager.security.CustomUserDetails;
import com.pdm.dietmanager.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Report", description = "권장 칼로리와 실제 섭취 칼로리 비교 API")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/daily")
    @Operation(
            summary = "일일 칼로리 리포트 조회",
            description = "저장된 프로필 목표 기준 권장 칼로리와 특정 날짜의 실제 섭취 칼로리를 비교한다."
    )
    @ApiResponse(responseCode = "200", description = "일일 리포트 조회 성공")
    @ApiResponse(
            responseCode = "400",
            description = "필수 쿼리 파라미터 누락 또는 날짜 형식 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "프로필을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public DailyReportResponse getDailyReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return reportService.getDailyReport(userDetails.getUser(), date);
    }

    @GetMapping("/weekly")
    @Operation(
            summary = "주간 칼로리 추이 조회",
            description = "endDate(기본값: 오늘)를 마지막 날로 하는 최근 7일의 권장/섭취 칼로리를 날짜순으로 반환한다."
    )
    @ApiResponse(responseCode = "200", description = "주간 추이 조회 성공")
    @ApiResponse(
            responseCode = "400",
            description = "필수 쿼리 파라미터 누락 또는 날짜 형식 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "프로필을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public WeeklyReportResponse getWeeklyReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return reportService.getWeeklyReport(userDetails.getUser(), endDate);
    }

    @GetMapping("/stats")
    @Operation(
            summary = "기록 통계 조회",
            description = "연속 기록 일수(스트릭)와 최근 7일 목표 달성률을 반환한다."
    )
    @ApiResponse(responseCode = "200", description = "통계 조회 성공")
    @ApiResponse(
            responseCode = "400",
            description = "필수 쿼리 파라미터 누락 또는 날짜 형식 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "프로필을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public StatsResponse getStats(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long profileId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return reportService.getStats(userDetails.getUser(), endDate);
    }
}
