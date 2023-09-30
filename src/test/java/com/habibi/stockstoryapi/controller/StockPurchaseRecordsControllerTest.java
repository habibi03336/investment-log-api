package com.habibi.stockstoryapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StockPurchaseRecordsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void readStockPurchaseRecordsOfCertainStock() throws Exception {
        mockMvc.perform(post("/api/stock-purchase-records?stock-code=000660")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    public void readStockPurchaseRecordsBetweenPeriods() throws Exception {
        mockMvc.perform(post("/api/stock-purchase-records?start-period=20230911&end-period=20230930")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andExpect(status().isCreated());
    }
}
