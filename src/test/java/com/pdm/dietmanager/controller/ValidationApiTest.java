package com.pdm.dietmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import com.pdm.dietmanager.entity.User;
import com.pdm.dietmanager.repository.UserRepository;
import com.pdm.dietmanager.security.JwtTokenProvider;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 컨트롤러 검증 동작(필수값/0 이하 값 차단, 미존재 리소스 404)을 웹 계층에서 검증한다.
 * WBS NFR-04/NFR-08의 "0 이하 값·필수값 누락 검증" 요구사항을 보장하기 위한 테스트.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ValidationApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String getAuthToken() {
        if (!userRepository.existsByEmail("test@example.com")) {
            User user = User.builder()
                    .email("test@example.com")
                    .password("$2a$10$E2UPv7arXNp3qRhKcJe3cO.D.bIq1uVl1X4mHUp0.v4H3c65c8lB.") // BCrypt encoded "password"
                    .nickname("testuser")
                    .build();
            userRepository.save(user);
        }
        return jwtTokenProvider.generateToken("test@example.com");
    }

    @Test
    void createProfileWithNonPositiveWeightReturns400() throws Exception {
        String token = getAuthToken();
        String body = """
                {"gender":"FEMALE","age":23,"height":162,"weight":0,
                 "activityLevel":"NORMAL","goalType":"WEIGHT_LOSS"}
                """;

        mockMvc.perform(post("/api/profiles")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProfileWithMissingGenderReturns400() throws Exception {
        String token = getAuthToken();
        String body = """
                {"age":23,"height":162,"weight":55,
                 "activityLevel":"NORMAL","goalType":"WEIGHT_LOSS"}
                """;

        mockMvc.perform(post("/api/profiles")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMealLogWithNonPositiveQuantityReturns400() throws Exception {
        String token = getAuthToken();
        String body = """
                {"profileId":1,"mealDate":"2026-06-14","mealType":"LUNCH","foodId":1,"quantity":0}
                """;

        mockMvc.perform(post("/api/meal-logs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void dailyReportForMissingProfileReturns404() throws Exception {
        String token = getAuthToken();
        mockMvc.perform(get("/api/reports/daily")
                        .header("Authorization", "Bearer " + token)
                        .param("date", "2026-06-14"))
                .andExpect(status().isNotFound());
    }

    @Test
    void dailyReportWithoutDateParamReturns400() throws Exception {
        String token = getAuthToken();
        mockMvc.perform(get("/api/reports/daily")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }
}
