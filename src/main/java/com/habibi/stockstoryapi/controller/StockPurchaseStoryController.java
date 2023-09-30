package com.habibi.stockstoryapi.controller;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value="/api/stock-purchase-stories", produces = MediaTypes.HAL_JSON_VALUE)
public class StockPurchaseStoryController {

    @PostMapping
    public ResponseEntity createStockPurchaseStory(){
        URI createdUri = linkTo(methodOn(StockPurchaseStoryController.class)
                .createStockPurchaseStory())
                .toUri();
        return ResponseEntity.created(createdUri).build();
    }
}
