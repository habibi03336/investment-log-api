package com.habibi.stockstoryapi.controller;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/api/stock-purchase-record", produces = MediaTypes.HAL_JSON_VALUE)
public class StockPurchaseRecordController {

    @GetMapping(params = { "start-period", "end-period"})
    public ResponseEntity readStockPurchaseRecordsBetweenPeriods(@RequestParam("start-period") String startPeriod, @RequestParam("end-period") String endPeriod){
        return ResponseEntity.ok().build();
    }

}
