package com.habibi.stockstoryapi;

import com.habibi.stockstoryapi.repository.StockPositionStoryRepository;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;
import com.habibi.stockstoryapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

@Configuration
@PropertySources({
        @PropertySource("classpath:.env")
})
public class SpringConfig {
    private final StockPositionStoryRepository stockPositionStoryRepository;
    private final StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private final StockSellRecordRepository stockSellRecordRepository;
    private StockCodeToNameMapper stockCodeToNameMapper;

    @Autowired
    public SpringConfig(
            StockPositionStoryRepository stockPositionStoryRepository,
            StockPurchaseRecordRepository stockPurchaseRecordRepository,
            StockSellRecordRepository stockSellRecordRepository
    ){
        this.stockPositionStoryRepository = stockPositionStoryRepository;
        this.stockPurchaseRecordRepository = stockPurchaseRecordRepository;
        this.stockSellRecordRepository = stockSellRecordRepository;
    }

    @Bean
    public StockCodeToNameMapper stockCodeToNameMapper(){
        this.stockCodeToNameMapper = new StockCodeToNameMapperByFinanceAPI();
        return stockCodeToNameMapper;
    }

    @Bean
    @DependsOn({"stockCodeToNameMapper"})
    public OwnStockService ownStockService() {
        return new OwnStockServiceImpl(stockPurchaseRecordRepository, stockSellRecordRepository, stockCodeToNameMapper);
    }

    @Bean
    @DependsOn({"stockCodeToNameMapper"})
    public RealizedStockService realizedStockService() {
        return new RealizedStockServiceImpl(stockSellRecordRepository, stockCodeToNameMapper);
    }

    @Bean
    @DependsOn({"stockCodeToNameMapper"})
    public StockRecordService stockRecordService() {
        return new StockRecordServiceImpl(stockPurchaseRecordRepository, stockSellRecordRepository, stockCodeToNameMapper);
    }

    @Bean
    @DependsOn({"stockCodeToNameMapper"})
    public StockStoryService stockStoryService() {
        return new StockStoryServiceImpl(stockPurchaseRecordRepository, stockSellRecordRepository, stockPositionStoryRepository, stockCodeToNameMapper);
    }

}
