package com.pdm.dietmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 컨트롤러 검증 동작(필수값/0 이하 값 차단, 미존재 리소스 404)을 웹 계층에서 검증한다.
 * WBS NFR-04/NFR-08의 "0 이하 값·필수값 누락 검증" 요구사항을 보장하기 위한 테스트.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ValidationApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createProfileWithNonPositiveWeightReturns400() throws Exception {
        String body = """
                {"gender":"FEMALE","age":23,"height":162,"weight":0,
                 "activityLevel":"NORMAL","goalType":"WEIGHT_LOSS"}
                """;

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProfileWithMissingGenderReturns400() throws Exception {
        String body = """
                {"age":23,"height":162,"weight":55,
                 "activityLevel":"NORMAL","goalType":"WEIGHT_LOSS"}
                """;

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMealLogWithNonPositiveQuantityReturns400() throws Exception {
        String body = """
                {"profileId":1,"mealDate":"2026-06-14","mealType":"LUNCH","foodId":1,"quantity":0}
                """;

        mockMvc.perform(post("/api/meal-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void dailyReportForMissingProfileReturns404() throws Exception {
        mockMvc.perform(get("/api/reports/daily")
                        .param("profileId", "999999")
                        .param("date", "2026-06-14"))
                .andExpect(status().isNotFound());
    }

    @Test
    void dailyReportWithoutDateParamReturns400() throws Exception {
        mockMvc.perform(get("/api/reports/daily")
                        .param("profileId", "1"))
                .andExpect(status().isBadRequest());
    }
}
