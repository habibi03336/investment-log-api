package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.RealizedStockDto;
import com.habibi.stockstoryapi.service.RealizedStockService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/api/realized-stock", produces = MediaTypes.HAL_JSON_VALUE)
public class RealizedStockController {

    private final RealizedStockService realizedStockService;

    public RealizedStockController(RealizedStockService realizedStockService){
        this.realizedStockService = realizedStockService;
    }
    @GetMapping
    public ResponseEntity<List<RealizedStockDto>> readStockSellRecords(){
        return ResponseEntity.ok().body(realizedStockService.readRealizedStocks());
    }

}
