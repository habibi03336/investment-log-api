package com.habibi.stockstoryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.habibi.stockstoryapi.dto.StockStoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StockStoryControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;


    @Test
    public void readStockStoryById() throws Exception {
        mockMvc.perform(get("/api/stock-story/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void readStockStoriesOfCertainStock() throws Exception {
        mockMvc.perform(get("/api/stock-story?stock-code=000660")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void createStockStory() throws Exception {
        StockStoryDto dto = StockStoryDto.builder()
                .isLong(true)
                .stockCode("105560")
                .stockPrices(new int[]{55300, 55100, 56000})
                .story("PBR이 지나치게 낮아져서 오를 여지가 있다고 생각함.")
                .build();

        mockMvc.perform(post("/api/stock-long-position-stories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated());
    }

}
