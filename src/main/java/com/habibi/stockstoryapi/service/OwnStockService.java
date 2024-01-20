package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.dto.OwnStockDto;

import java.time.LocalDate;
import java.util.List;

public interface OwnStockService {
    List<OwnStockDto> readOwnStocks(int userId);
    List<OwnStockDto> readOwnStocksAtSomePoint(int userId, LocalDate date);
}
