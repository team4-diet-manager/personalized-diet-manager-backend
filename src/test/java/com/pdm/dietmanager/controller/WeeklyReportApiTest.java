package com.pdm.dietmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MockMvc;

/**
 * 주간 칼로리 추이 엔드포인트 웹 계층 검증(200/400/404).
 */
@SpringBootTest
@AutoConfigureMockMvc
class WeeklyReportApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserProfileService userProfileService;

    private Long createProfile() {
        return userProfileService.createProfile(UserProfileRequest.of(
                Gender.FEMALE, 23, 162.0, 55.0, ActivityLevel.NORMAL, GoalType.WEIGHT_LOSS
        )).getProfileId();
    }

    @Test
    void weeklyReturnsSevenDays() throws Exception {
        Long profileId = createProfile();

        mockMvc.perform(get("/api/reports/weekly")
                        .param("profileId", String.valueOf(profileId))
                        .param("endDate", "2026-06-14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days.length()").value(7));
    }

    @Test
    void weeklyWithoutEndDateUsesTodayAndSucceeds() throws Exception {
        Long profileId = createProfile();

        mockMvc.perform(get("/api/reports/weekly")
                        .param("profileId", String.valueOf(profileId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days.length()").value(7));
    }

    @Test
    void weeklyWithoutProfileIdReturns400() throws Exception {
        mockMvc.perform(get("/api/reports/weekly"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void weeklyForMissingProfileReturns404() throws Exception {
        mockMvc.perform(get("/api/reports/weekly")
                        .param("profileId", "999999"))
                .andExpect(status().isNotFound());
    }
}
