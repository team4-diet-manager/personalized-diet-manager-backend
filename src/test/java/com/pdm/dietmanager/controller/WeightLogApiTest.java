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
 * 체중 기록 엔드포인트 웹 계층 검증(200/400/404, 같은 날짜 upsert).
 */
@SpringBootTest
@AutoConfigureMockMvc
class WeightLogApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserProfileService userProfileService;

    private Long createProfile() {
        return userProfileService.createProfile(UserProfileRequest.of(
                "지현", Gender.FEMALE, 23, 162.0, 55.0, ActivityLevel.NORMAL, GoalType.WEIGHT_LOSS
        )).getProfileId();
    }

    private String body(Long profileId, String date, double weight) {
        return "{\"profileId\":" + profileId + ",\"logDate\":\"" + date + "\",\"weight\":" + weight + "}";
    }

    @Test
    void recordWeightReturns200WithSavedValue() throws Exception {
        Long profileId = createProfile();

        mockMvc.perform(post("/api/weight-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(profileId, "2026-06-14", 54.5)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight").value(54.5));
    }

    @Test
    void sameDateUpdatesAndKeepsSingleEntry() throws Exception {
        Long profileId = createProfile();

        mockMvc.perform(post("/api/weight-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(profileId, "2026-06-15", 55.0)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/weight-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(profileId, "2026-06-15", 53.5)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/weight-logs").param("profileId", String.valueOf(profileId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].weight").value(53.5));
    }

    @Test
    void nonPositiveWeightReturns400() throws Exception {
        Long profileId = createProfile();

        mockMvc.perform(post("/api/weight-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(profileId, "2026-06-14", 0)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recordForMissingProfileReturns404() throws Exception {
        mockMvc.perform(post("/api/weight-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(999999L, "2026-06-14", 54.0)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getWithoutProfileIdReturns400() throws Exception {
        mockMvc.perform(get("/api/weight-logs"))
                .andExpect(status().isBadRequest());
    }
}
