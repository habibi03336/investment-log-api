package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.StockPositionStoryDto;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value="/api/stock-short-position-stories", produces = MediaTypes.HAL_JSON_VALUE)
public class StockShortPositionStoryController {

    @GetMapping(params = { "stock-code" })
    public ResponseEntity readStockShortPositionStoriesOfCertainStock(@RequestParam("stock-code") String stockCode){
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity createStockShortPositionStory(@RequestBody StockPositionStoryDto createStockSellStoryDto){
        URI createdUri = linkTo(StockShortPositionStoryController.class).slash("{id}").toUri();
        return ResponseEntity.created(createdUri).build();
    }
}
