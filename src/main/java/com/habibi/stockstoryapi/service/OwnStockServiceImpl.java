package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.dto.OwnStockDto;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OwnStockServiceImpl implements OwnStockService {

    private StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private StockSellRecordRepository stockSellRecordRepository;
    private StockCodeToNameMapper stockCodeToNameMapper;

    public OwnStockServiceImpl(
            StockPurchaseRecordRepository stockPurchaseRecordRepository,
            StockSellRecordRepository stockSellRecordRepository,
            StockCodeToNameMapper stockCodeToNameMapper
    ){
        this.stockPurchaseRecordRepository = stockPurchaseRecordRepository;
        this.stockSellRecordRepository = stockSellRecordRepository;
        this.stockCodeToNameMapper = stockCodeToNameMapper;
    }

    @Override
    public List<OwnStockDto> readOwnStocks() {
        List<StockPurchaseRecordEntity> stockPurchaseRecords = stockPurchaseRecordRepository.findAll();
        List<StockSellRecordEntity> stockSellRecords = stockSellRecordRepository.findAll();
        List<OwnStockDto> ownStocks = getOwnStocksByPurchaseAndSellRecords(stockPurchaseRecords, stockSellRecords);
        return ownStocks;
    }

    @Override
    public List<OwnStockDto> readOwnStocksAtSomePoint(LocalDate date) {
        List<StockPurchaseRecordEntity> stockPurchaseRecords = stockPurchaseRecordRepository.findAllByPurchaseDtIsBefore(date);
        List<StockSellRecordEntity> stockSellRecords = stockSellRecordRepository.findAllBySellDtIsBefore(date);
        List<OwnStockDto> ownStocks = getOwnStocksByPurchaseAndSellRecords(stockPurchaseRecords, stockSellRecords);
        return ownStocks;
    }

    private List<OwnStockDto> getOwnStocksByPurchaseAndSellRecords(List<StockPurchaseRecordEntity> stockPurchaseRecords,
                                                                          List<StockSellRecordEntity> stockSellRecords) {
        Map<String, long[]> totalPurchasePriceAndCountByStockCode = new HashMap<>();
        for(StockPurchaseRecordEntity stockPurchaseRecordEntity : stockPurchaseRecords){
            if(totalPurchasePriceAndCountByStockCode.containsKey(stockPurchaseRecordEntity.getStockCode())){
                long[] totalPricesAndCount = totalPurchasePriceAndCountByStockCode.get(stockPurchaseRecordEntity.getStockCode());
                totalPricesAndCount[0] += stockPurchaseRecordEntity.getPurchasePrice();
                totalPricesAndCount[1] += 1;
            } else {
                long[] totalPricesAndCount = new long[]{
                        stockPurchaseRecordEntity.getPurchasePrice(),
                        1l
                };
                totalPurchasePriceAndCountByStockCode.put(stockPurchaseRecordEntity.getStockCode(), totalPricesAndCount);
            }
        }
        for(StockSellRecordEntity stockSellRecordEntity: stockSellRecords){
            long[] totalPricesAndCount = totalPurchasePriceAndCountByStockCode.get(stockSellRecordEntity.getStockCode());
            totalPricesAndCount[0] -= stockSellRecordEntity.getAvgPurchasePrice();
            totalPricesAndCount[1] -= 1;
        }
        List<OwnStockDto> ownStocks = new ArrayList<>();
        for(String stockCode : totalPurchasePriceAndCountByStockCode.keySet()){
            long[] totalPricesAndCount = totalPurchasePriceAndCountByStockCode.get(stockCode);
            if(totalPricesAndCount[1] > 0){
                ownStocks.add(
                        OwnStockDto
                                .builder()
                                .stockName(stockCodeToNameMapper.getStockName(stockCode))
                                .stockCode(stockCode)
                                .stockCount((int) totalPricesAndCount[1])
                                .averagePurchasePrice((int) (totalPricesAndCount[0] / totalPricesAndCount[1]))
                                .build()
                );
            }
        }
        return ownStocks;
    }
}
