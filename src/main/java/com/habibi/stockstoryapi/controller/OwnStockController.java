package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.OwnStockDto;
import com.habibi.stockstoryapi.service.OwnStockService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value="/api/own-stock", produces = MediaTypes.HAL_JSON_VALUE)
public class OwnStockController {

    private final OwnStockService ownStockService;

    @Autowired
    public OwnStockController(OwnStockService ownStockService) {
        this.ownStockService = ownStockService;
    }

    @GetMapping
    public ResponseEntity<List<OwnStockDto>> readOwnStocks(){
        return ResponseEntity
            .ok()
            .body(ownStockService.readOwnStocks());
    }

    @GetMapping(params = { "at" })
    public ResponseEntity readOwnStocksAtSomePoint(@RequestParam("at") String date){
        int[] tokens = Arrays.stream(date.split("-")).mapToInt(Integer::parseInt).toArray();
        return ResponseEntity.ok()
                .body(ownStockService.readOwnStocksAtSomePoint(LocalDate.of(tokens[0], tokens[1], tokens[2])));
    }
}
