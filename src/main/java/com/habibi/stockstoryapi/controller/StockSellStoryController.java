package com.habibi.stockstoryapi.controller;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value="/api/stock-sell-stories", produces = MediaTypes.HAL_JSON_VALUE)
public class StockSellStoryController {

    @PostMapping
    public ResponseEntity createStockSellStory(){
        URI createdUri = linkTo(methodOn(StockSellStoryController.class)
                .createStockSellStory())
                .toUri();
        return ResponseEntity.created(createdUri).build();
    }

}
