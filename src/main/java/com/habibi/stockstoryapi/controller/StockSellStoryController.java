package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.StockPositionStoryDto;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value="/api/stock-sell-stories", produces = MediaTypes.HAL_JSON_VALUE)
public class StockSellStoryController {

    @GetMapping(params = { "stock-code" })
    public ResponseEntity readStockSellStoriesOfCertainStock(@RequestParam("stock-code") String stockCode){
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity createStockSellStory(@RequestBody StockPositionStoryDto createStockSellStoryDto){
        URI createdUri = linkTo(StockSellStoryController.class).slash("{id}").toUri();
        return ResponseEntity.created(createdUri).build();
    }
}
