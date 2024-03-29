package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.dto.RealizedStockDto;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealizedStockServiceImpl implements RealizedStockService {
    private StockSellRecordRepository stockSellRecordRepository;
    private StockCodeToNameMapper stockCodeToNameMapper;

    public RealizedStockServiceImpl(StockSellRecordRepository stockSellRecordRepository, StockCodeToNameMapper stockCodeToNameMapper){
        this.stockSellRecordRepository = stockSellRecordRepository;
        this.stockCodeToNameMapper = stockCodeToNameMapper;
    }
    @Override
    public List<RealizedStockDto> readRealizedStocks(int userId) {
        List<StockSellRecordEntity> stockSellRecordEntities = stockSellRecordRepository.findAllByUserId(userId);
        Map<String, long[]> totalPurchasePriceAndTotalSellPriceAndCountByStockCode = new HashMap<>();
        for(StockSellRecordEntity stockSellRecordEntity : stockSellRecordEntities){
            if(totalPurchasePriceAndTotalSellPriceAndCountByStockCode.containsKey(stockSellRecordEntity.getStockCode())){
                long[] totalPurchasePriceAndTotalSellPriceAndCount = totalPurchasePriceAndTotalSellPriceAndCountByStockCode.get(stockSellRecordEntity.getStockCode());
                totalPurchasePriceAndTotalSellPriceAndCount[0] += stockSellRecordEntity.getAvgPurchasePrice();
                totalPurchasePriceAndTotalSellPriceAndCount[1] += stockSellRecordEntity.getSellPrice();
                totalPurchasePriceAndTotalSellPriceAndCount[2] += 1;
            } else {
                long[] totalPurchasePriceAndTotalSellPriceAndCount = new long[]{
                        stockSellRecordEntity.getAvgPurchasePrice(),
                        stockSellRecordEntity.getSellPrice(),
                        1
                };
                totalPurchasePriceAndTotalSellPriceAndCountByStockCode.put(
                        stockSellRecordEntity.getStockCode(),
                        totalPurchasePriceAndTotalSellPriceAndCount
                );
            }
        }
        List<RealizedStockDto> realizedStockDtos = new ArrayList<>();
        for(String stockCode : totalPurchasePriceAndTotalSellPriceAndCountByStockCode.keySet()){
            long[] totalPurchasePriceAndTotalSellPriceAndCount = totalPurchasePriceAndTotalSellPriceAndCountByStockCode.get(stockCode);
            realizedStockDtos.add(
                    RealizedStockDto
                            .builder()
                            .stockName(stockCodeToNameMapper.getStockName(stockCode))
                            .stockCode(stockCode)
                            .averagePurchasePrice((int)(totalPurchasePriceAndTotalSellPriceAndCount[0]/totalPurchasePriceAndTotalSellPriceAndCount[2]))
                            .averageSellPrice((int)(totalPurchasePriceAndTotalSellPriceAndCount[1] / totalPurchasePriceAndTotalSellPriceAndCount[2]))
                            .stockCount((int) totalPurchasePriceAndTotalSellPriceAndCount[2])
                            .build()
            );
        }
        return realizedStockDtos;
    }
}
