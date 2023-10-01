package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.CreateStockStoryDto;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value="/api/stock-sell-stories", produces = MediaTypes.HAL_JSON_VALUE)
public class StockSellStoryController {

    @PostMapping
    public ResponseEntity createStockSellStory(@RequestBody CreateStockStoryDto createStockSellStoryDto){
        URI createdUri = linkTo(StockSellStoryController.class).slash("{id}").toUri();
        return ResponseEntity.created(createdUri).build();
    }
}
