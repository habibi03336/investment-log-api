package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.CreateStatusDto;
import com.habibi.stockstoryapi.dto.StockStoryDto;
import com.habibi.stockstoryapi.service.StockStoryService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value="/api/stock-story", produces = MediaTypes.HAL_JSON_VALUE)
@PreAuthorize("hasAuthority('USER')")
public class StockStoryController {

    private final StockStoryService stockStoryService;

    public StockStoryController(StockStoryService stockStoryService){
        this.stockStoryService = stockStoryService;
    }

    @GetMapping(params = { "stock-code" })
    public ResponseEntity<List<StockStoryDto>> readStockStoriesOfCertainStock(@RequestParam("stock-code") String stockCode){
        return ResponseEntity.ok()
                .body(stockStoryService.readStockStoryOfCertainStock(stockCode));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockStoryDto> readStockStoryById(@PathVariable long id){
        return ResponseEntity.ok()
                .body(stockStoryService.readStockStoryById(id));
    }

    @PostMapping
    public ResponseEntity<CreateStatusDto> createStockStory(@RequestBody StockStoryDto createStockStoryDto){
        CreateStatusDto createStatusDto = stockStoryService.createStockStory(createStockStoryDto);
        URI createdUri = linkTo(StockStoryController.class).slash("{id}").toUri();
        return ResponseEntity.created(createdUri).body(createStatusDto);
    }
}
