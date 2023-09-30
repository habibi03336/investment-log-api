package com.habibi.stockstoryapi.controller;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/api/own-stocks", produces = MediaTypes.HAL_JSON_VALUE)
public class OwnStockController {

    @GetMapping
    public ResponseEntity readOwnStocks(){
        return ResponseEntity.ok().build();
    }

    @GetMapping(params = { "at" })
    public ResponseEntity readOwnStocksAtSomePoint(@RequestParam("at") String date){
        return ResponseEntity.ok().build();
    }
}
