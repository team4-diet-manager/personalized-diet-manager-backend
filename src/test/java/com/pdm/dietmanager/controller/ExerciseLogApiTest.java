package com.pdm.dietmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 운동 기록 엔드포인트 웹 계층 검증(201/200/400/404)과 소모 칼로리 계산 반영.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ExerciseLogApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserProfileService userProfileService;

    private Long createProfile() {
        return userProfileService.createProfile(UserProfileRequest.of(
                "민준", Gender.MALE, 30, 175.0, 60.0, ActivityLevel.NORMAL, GoalType.MAINTAIN
        )).getProfileId();
    }

    private String body(Long profileId, String date, String type, int minutes, String intensity) {
        return "{\"profileId\":" + profileId + ",\"exerciseDate\":\"" + date
                + "\",\"exerciseType\":\"" + type + "\",\"durationMinutes\":" + minutes
                + ",\"intensity\":\"" + intensity + "\"}";
    }

    @Test
    void createReturns201WithBurnedCalories() throws Exception {
        Long profileId = createProfile();

        // 달리기 LOW, 60kg, 30분 → 7.0 MET × 60 × 0.5 = 210
        mockMvc.perform(post("/api/exercise-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(profileId, "2026-06-15", "RUNNING", 30, "LOW")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.burnedCalories").value(210));
    }

    @Test
    void dailyListReturnsTotalBurned() throws Exception {
        Long profileId = createProfile();
        mockMvc.perform(post("/api/exercise-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(profileId, "2026-06-15", "RUNNING", 30, "LOW")))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/exercise-logs")
                        .param("profileId", String.valueOf(profileId))
                        .param("date", "2026-06-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dailyTotalBurned").value(210));
    }

    @Test
    void nonPositiveDurationReturns400() throws Exception {
        Long profileId = createProfile();

        mockMvc.perform(post("/api/exercise-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(profileId, "2026-06-15", "RUNNING", 0, "LOW")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void missingProfileReturns404() throws Exception {
        mockMvc.perform(post("/api/exercise-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(999999L, "2026-06-15", "RUNNING", 30, "LOW")))
                .andExpect(status().isNotFound());
    }
}
