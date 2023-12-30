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
    private StockCodeToNameMapper stockCodeToNameMapper;

    public StockRecordServiceImpl(
            StockPurchaseRecordRepository stockPurchaseRecordRepository,
            StockSellRecordRepository stockSellRecordRepository,
            StockCodeToNameMapper stockCodeToNameMapper
    ){
        this.stockPurchaseRecordRepository = stockPurchaseRecordRepository;
        this.stockSellRecordRepository = stockSellRecordRepository;
        this.stockCodeToNameMapper = stockCodeToNameMapper;
    }
    @Override
    public List<StockRecordDto> readStockPurchaseRecordsBetweenPeriods(LocalDate start, LocalDate end) {
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = stockPurchaseRecordRepository.findAllByPurchaseDtIsBetween(start, end);
        return stockPurchaseRecordEntities.stream().map((entity) ->
                StockRecordDto
                        .builder()
                        .stockName(stockCodeToNameMapper.getStockName(entity.getStockCode()))
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
                        .stockName(stockCodeToNameMapper.getStockName(entity.getStockCode()))
                        .stockCode(entity.getStockCode())
                        .stockPrice(entity.getSellPrice())
                        .dt(entity.getSellDt())
                        .build()
        ).collect(Collectors.toList());
    }
}
