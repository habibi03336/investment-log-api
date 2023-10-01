package com.habibi.stockstoryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.habibi.stockstoryapi.dto.CreateStockStoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StockSellStoryControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void readStockSellRecordsOfCertainStock() throws Exception {
        mockMvc.perform(get("/api/stock-sell-stories?stock-code=000660")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void createStockSellStory() throws Exception {
        CreateStockStoryDto dto = CreateStockStoryDto.builder()
                .stockCode("035720")
                .stockPrices(new int[]{43950, 44000, 43900})
                .dt(LocalDateTime.of(2023, 10, 1, 15, 00))
                .story("실적이 안 좋고, 개선 여지가 크게 보이지 않아 매도")
                .build();

        mockMvc.perform(post("/api/stock-sell-stories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated());
    }
}
