package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.StockStoryDto;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value="/api/stock-story", produces = MediaTypes.HAL_JSON_VALUE)
public class StockStoryController {

    @GetMapping(params = { "stock-code" })
    public ResponseEntity readStockStoriesOfCertainStock(@RequestParam("stock-code") String stockCode){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity readStockStoryById(@PathVariable String id){
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity createStocStory(@RequestBody StockStoryDto createStockSellStoryDto){
        URI createdUri = linkTo(StockStoryController.class).slash("{id}").toUri();
        return ResponseEntity.created(createdUri).build();
    }
}
