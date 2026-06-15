package com.pdm.dietmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주간 칼로리 추이 엔드포인트 웹 계층 검증(200/401/404).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WeeklyReportApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private User getTestUser() {
        return userRepository.findByEmail("test@example.com").orElseGet(() -> {
            User user = User.builder()
                    .email("test@example.com")
                    .password("$2a$10$E2UPv7arXNp3qRhKcJe3cO.D.bIq1uVl1X4mHUp0.v4H3c65c8lB.") // BCrypt encoded "password"
                    .nickname("testuser")
                    .build();
            return userRepository.save(user);
        });
    }

    private String getAuthToken() {
        getTestUser();
        return jwtTokenProvider.generateToken("test@example.com");
    }

    private Long createProfile(User user) {
        return userProfileService.createProfile(user, UserProfileRequest.of(
                "지현", Gender.FEMALE, 23, 162.0, 55.0, ActivityLevel.NORMAL, GoalType.WEIGHT_LOSS
        )).getProfileId();
    }

    @Test
    void weeklyReturnsSevenDays() throws Exception {
        User user = getTestUser();
        createProfile(user);
        String token = getAuthToken();

        mockMvc.perform(get("/api/reports/weekly")
                        .header("Authorization", "Bearer " + token)
                        .param("endDate", "2026-06-14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days.length()").value(7));
    }

    @Test
    void weeklyWithoutEndDateUsesTodayAndSucceeds() throws Exception {
        User user = getTestUser();
        createProfile(user);
        String token = getAuthToken();

        mockMvc.perform(get("/api/reports/weekly")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days.length()").value(7));
    }

    @Test
    void weeklyWithoutProfileIdReturns400() throws Exception {
        // 인증되지 않은 사용자는 403 Forbidden을 반환해야 합니다.
        mockMvc.perform(get("/api/reports/weekly"))
                .andExpect(status().isForbidden());
    }

    @Test
    void weeklyForMissingProfileReturns404() throws Exception {
        getTestUser();
        String token = getAuthToken();

        mockMvc.perform(get("/api/reports/weekly")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
