package com.habibi.stockstoryapi.controller;

import com.habibi.stockstoryapi.dto.OwnStockDto;
import com.habibi.stockstoryapi.dto.UserDetailsDto;
import com.habibi.stockstoryapi.service.OwnStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value="/api/own-stock", produces = MediaTypes.HAL_JSON_VALUE)
@PreAuthorize("hasAuthority('USER')")
public class OwnStockController {

    private final OwnStockService ownStockService;

    @Autowired
    public OwnStockController(OwnStockService ownStockService) {
        this.ownStockService = ownStockService;
    }

    @GetMapping
    public ResponseEntity<List<OwnStockDto>> readOwnStocks(@AuthenticationPrincipal UserDetailsDto userDetails){
        return ResponseEntity
            .ok()
            .body(ownStockService.readOwnStocks(userDetails.getUserId()));
    }

    @GetMapping(params = { "at" })
    public ResponseEntity readOwnStocksAtSomePoint(
            @RequestParam("at") String date,
            @AuthenticationPrincipal UserDetailsDto userDetails
    ){
        int[] tokens = Arrays.stream(date.split("-")).mapToInt(Integer::parseInt).toArray();
        return ResponseEntity.ok()
                .body(ownStockService
                        .readOwnStocksAtSomePoint(
                                userDetails.getUserId(),
                                LocalDate.of(tokens[0], tokens[1], tokens[2])
                        )
                );
    }
}
