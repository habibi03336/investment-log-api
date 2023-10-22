package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.dto.StockRecordDto;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class StockRecordServiceImpl implements  StockRecordService{
    private StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private StockSellRecordRepository stockSellRecordRepository;
    public StockRecordServiceImpl(StockPurchaseRecordRepository stockPurchaseRecordRepository, StockSellRecordRepository stockSellRecordRepository){
        this.stockPurchaseRecordRepository = stockPurchaseRecordRepository;
        this.stockSellRecordRepository = stockSellRecordRepository;
    }
    @Override
    public List<StockRecordDto> readStockPurchaseRecordsBetweenPeriods(LocalDate start, LocalDate end) {
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = stockPurchaseRecordRepository.findAllByPurchaseDtIsBetween(start, end);
        return stockPurchaseRecordEntities.stream().map((entity) ->
                StockRecordDto
                        .builder()
                        .stockCode(entity.getStockCode())
                        .stockPrice(entity.getPurchasePrice())
                        .dt(entity.getPurchaseDt())
                        .build()
        ).collect(Collectors.toList());
    }
    @Override
    public List<StockRecordDto> readStockSellRecordsBetweenPeriods(LocalDate start, LocalDate end) {
        List<StockSellRecordEntity> stockSellRecordEntities = stockSellRecordRepository.findAllBySellDtIsBetween(start, end);
        return stockSellRecordEntities.stream().map((entity) ->
                StockRecordDto
                        .builder()
                        .stockCode(entity.getStockCode())
                        .stockPrice(entity.getSellPrice())
                        .dt(entity.getSellDt())
                        .build()
        ).collect(Collectors.toList());
    }
}
