package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.StockStoryDto;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value="/api/stock-long-position-stories", produces = MediaTypes.HAL_JSON_VALUE)
public class StockLongPositionStoryController {

    @GetMapping(params = { "stock-code" })
    public ResponseEntity readStockLongPositionStoriesOfCertainStock(@RequestParam("stock-code") String stockCode){
        return ResponseEntity.ok().build();
    }
    @PostMapping
    public ResponseEntity createStockLongPositionStory(@RequestBody StockStoryDto createStockPurchaseStoryDto){
        URI createdUri = linkTo(StockLongPositionStoryController.class).slash("{id}").toUri();
        return ResponseEntity.created(createdUri).build();
    }
}
