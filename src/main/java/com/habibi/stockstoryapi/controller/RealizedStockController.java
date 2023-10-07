package com.habibi.stockstoryapi.controller;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/api/realized-stocks", produces = MediaTypes.HAL_JSON_VALUE)
public class RealizedStockController {
    @GetMapping
    public ResponseEntity readStockSellRecords(){
        return ResponseEntity.ok().build();
    }

}
