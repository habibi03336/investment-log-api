package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.dto.RealizedStockDto;

import java.util.List;

public interface RealizedStockService {
    List<RealizedStockDto> readRealizedStocks(int userId);
}
