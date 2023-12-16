package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.RealizedStockDto;
import com.habibi.stockstoryapi.service.RealizedStockService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
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
