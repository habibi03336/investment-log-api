package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.StockRecordDto;
import com.habibi.stockstoryapi.dto.UserDetailsDto;
import com.habibi.stockstoryapi.service.StockRecordService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value="/api/stock-purchase-record", produces = MediaTypes.HAL_JSON_VALUE)
@PreAuthorize("hasAuthority('USER')")
public class StockPurchaseRecordController {

    private final StockRecordService stockRecordService;

    public StockPurchaseRecordController(StockRecordService stockRecordService){
        this.stockRecordService = stockRecordService;
    }

    @GetMapping(params = { "start-period", "end-period"})
    public ResponseEntity<List<StockRecordDto>> readStockPurchaseRecordsBetweenPeriods(
            @AuthenticationPrincipal UserDetailsDto userDetails,
            @RequestParam("start-period") String startPeriod,
            @RequestParam("end-period") String endPeriod
    ){
        int[] startTokens = Arrays.stream(startPeriod.split("-")).mapToInt(Integer::parseInt).toArray();
        int[] endTokens = Arrays.stream(endPeriod.split("-")).mapToInt(Integer::parseInt).toArray();
        return ResponseEntity.ok()
                .body(
                    stockRecordService.readStockPurchaseRecordsBetweenPeriods(
                        userDetails.getUserId(),
                        LocalDate.of(startTokens[0], startTokens[1], startTokens[2]),
                        LocalDate.of(endTokens[0], endTokens[1], endTokens[2])
                    )
                );
    }

}
