package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.StockRecordDto;
import com.habibi.stockstoryapi.service.StockRecordService;
import com.habibi.stockstoryapi.service.StockRecordServiceImpl;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value="/api/stock-purchase-record", produces = MediaTypes.HAL_JSON_VALUE)
public class StockPurchaseRecordController {

    private final StockRecordService stockRecordService;

    public StockPurchaseRecordController(StockRecordService stockRecordService){
        this.stockRecordService = stockRecordService;
    }

    @GetMapping(params = { "start-period", "end-period"})
    public ResponseEntity<List<StockRecordDto>> readStockPurchaseRecordsBetweenPeriods(@RequestParam("start-period") String startPeriod, @RequestParam("end-period") String endPeriod){
        int[] startTokens = Arrays.stream(startPeriod.split("-")).mapToInt(Integer::parseInt).toArray();
        int[] endTokens = Arrays.stream(endPeriod.split("-")).mapToInt(Integer::parseInt).toArray();
        return ResponseEntity.ok()
                .body(
                    stockRecordService.readStockPurchaseRecordsBetweenPeriods(
                        LocalDate.of(startTokens[0], startTokens[1], startTokens[2]),
                        LocalDate.of(endTokens[0], endTokens[1], endTokens[2])
                    )
                );
    }

}
