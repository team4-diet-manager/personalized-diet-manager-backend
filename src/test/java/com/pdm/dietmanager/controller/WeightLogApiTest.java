package com.pdm.dietmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pdm.dietmanager.dto.request.UserProfileRequest;
import com.pdm.dietmanager.enums.ActivityLevel;
import com.pdm.dietmanager.enums.Gender;
import com.pdm.dietmanager.enums.GoalType;
import com.pdm.dietmanager.entity.User;
import com.pdm.dietmanager.repository.UserRepository;
import com.pdm.dietmanager.security.JwtTokenProvider;
import com.pdm.dietmanager.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * 체중 기록 엔드포인트 웹 계층 검증(200/400/404, 같은 날짜 upsert).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WeightLogApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private User getTestUser() {
        return userRepository.findByEmail("weight@example.com").orElseGet(() -> userRepository.save(
                User.builder()
                        .email("weight@example.com")
                        .password("password")
                        .nickname("weight")
                        .build()
        ));
    }

    private String getAuthToken() {
        getTestUser();
        return jwtTokenProvider.generateToken("weight@example.com");
    }

    private Long createProfile(User user) {
        return userProfileService.createProfile(user, UserProfileRequest.of(
                "지현", Gender.FEMALE, 23, 162.0, 55.0, ActivityLevel.NORMAL, GoalType.WEIGHT_LOSS
        )).getProfileId();
    }

    private String body(Long profileId, String date, double weight) {
        return "{\"profileId\":" + profileId + ",\"logDate\":\"" + date + "\",\"weight\":" + weight + "}";
    }

    @Test
    void recordWeightReturns200WithSavedValue() throws Exception {
        User user = getTestUser();
        Long profileId = createProfile(user);
        String token = getAuthToken();

        mockMvc.perform(post("/api/weight-logs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(profileId, "2026-06-14", 54.5)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight").value(54.5));
    }

    @Test
    void sameDateUpdatesAndKeepsSingleEntry() throws Exception {
        User user = getTestUser();
        Long profileId = createProfile(user);
        String token = getAuthToken();

        mockMvc.perform(post("/api/weight-logs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(profileId, "2026-06-15", 55.0)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/weight-logs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(profileId, "2026-06-15", 53.5)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/weight-logs")
                        .header("Authorization", "Bearer " + token)
                        .param("profileId", String.valueOf(profileId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].weight").value(53.5));
    }

    @Test
    void nonPositiveWeightReturns400() throws Exception {
        User user = getTestUser();
        Long profileId = createProfile(user);
        String token = getAuthToken();

        mockMvc.perform(post("/api/weight-logs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(profileId, "2026-06-14", 0)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recordForMissingProfileReturns404() throws Exception {
        getTestUser();
        String token = getAuthToken();

        mockMvc.perform(post("/api/weight-logs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(999999L, "2026-06-14", 54.0)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getHistoryUsesAuthenticatedUser() throws Exception {
        User user = getTestUser();
        Long profileId = createProfile(user);
        String token = getAuthToken();

        mockMvc.perform(post("/api/weight-logs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(profileId, "2026-06-14", 54.0)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/weight-logs")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
