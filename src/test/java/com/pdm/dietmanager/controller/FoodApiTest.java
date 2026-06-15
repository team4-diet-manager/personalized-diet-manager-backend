package com.pdm.dietmanager.controller;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 음식 조회/신호등 분류 엔드포인트 웹 계층 검증.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class FoodApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void foodsWithoutGoalHaveNoGrade() throws Exception {
        mockMvc.perform(get("/api/foods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(20))
                .andExpect(jsonPath("$[0].grade").value(nullValue()));
    }

    @Test
    void foodsWithGoalIncludeTrafficLightGrade() throws Exception {
        // 다이어트 기준: 저칼로리·고단백 닭가슴살은 GREEN.
        mockMvc.perform(get("/api/foods")
                        .param("goalType", "WEIGHT_LOSS")
                        .param("keyword", "닭가슴살"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("닭가슴살"))
                .andExpect(jsonPath("$[0].grade").value("GREEN"));
    }

    @Test
    void invalidGoalTypeReturns400() throws Exception {
        mockMvc.perform(get("/api/foods").param("goalType", "INVALID_GOAL"))
                .andExpect(status().isBadRequest());
    }
}
