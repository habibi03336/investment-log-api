package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.CreateStockStoryDto;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value="/api/stock-purchase-stories", produces = MediaTypes.HAL_JSON_VALUE)
public class StockPurchaseStoryController {

    @GetMapping(params = { "stock-code" })
    public ResponseEntity readStockPurchaseStoriesOfCertainStock(@RequestParam("stock-code") String stockCode){
        return ResponseEntity.ok().build();
    }
    @PostMapping
    public ResponseEntity createStockPurchaseStory(@RequestBody CreateStockStoryDto createStockPurchaseStoryDto){
        URI createdUri = linkTo(StockPurchaseStoryController.class).slash("{id}").toUri();
        return ResponseEntity.created(createdUri).build();
    }
}
