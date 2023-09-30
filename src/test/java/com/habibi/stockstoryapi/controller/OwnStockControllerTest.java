package com.habibi.stockstoryapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class OwnStockControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void readOwnStocks() throws Exception {
        mockMvc.perform(get("/api/own-stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void readOwnStocksAtSomePoint() throws Exception {
        mockMvc.perform(get("/api/own-stocks?at=20230930")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andExpect(status().isOk());
    }
}
