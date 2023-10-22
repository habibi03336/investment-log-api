package com.habibi.stockstoryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.habibi.stockstoryapi.dto.StockPositionStoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StockLongPositionStoryControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void readStockLongPositionStoriesOfCertainStock() throws Exception {
        mockMvc.perform(get("/api/stock-long-position-stories?stock-code=000660")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void createStockLongPositionStory() throws Exception {
        StockPositionStoryDto dto = StockPositionStoryDto.builder()
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
