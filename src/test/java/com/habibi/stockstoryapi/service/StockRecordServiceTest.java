package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.dto.StockRecordDto;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockRecordServiceTest {

    @Test
    public void testReturnEmptyListWhenNoRecord(){
        //given
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        StockPurchaseRecordRepository stockPurchaseRecordRepository = mock(StockPurchaseRecordRepository.class);
        when(stockPurchaseRecordRepository.findAll())
                .thenReturn(stockPurchaseRecordEntities);
        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        StockSellRecordRepository stockSellRecordRepository = mock(StockSellRecordRepository.class);
        when(stockSellRecordRepository.findAll())
                .thenReturn(stockSellRecordEntities);

        StockRecordService stockRecordService = null;

        // when
        List<StockRecordDto> stockPurchaseRecordDtos = stockRecordService.readStockPurchaseRecordsBetweenPeriods(
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        List<StockRecordDto> stockSellRecordDtos = stockRecordService.readStockSellRecordsBetweenPeriods(
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));

        // then
        assertThat(stockPurchaseRecordDtos).isNotNull();
        assertThat(stockPurchaseRecordDtos.size()).isEqualTo(0);
        assertThat(stockSellRecordDtos).isNotNull();
        assertThat(stockSellRecordDtos.size()).isEqualTo(0);
    }
    @Test
    public void testResultAccuracy(){
        //given
        String stockCode = "000660";
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 8))
                        .stockCode(stockCode)
                        .purchasePrice(45000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 9))
                        .stockCode(stockCode)
                        .purchasePrice(55000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 10))
                        .stockCode(stockCode)
                        .purchasePrice(58000)
                        .build()
        );
        StockPurchaseRecordRepository stockPurchaseRecordRepository = mock(StockPurchaseRecordRepository.class);
        when(stockPurchaseRecordRepository.findAll())
                .thenReturn(stockPurchaseRecordEntities);
        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .sellDt(LocalDate.of(2023,10,10))
                        .stockCode(stockCode)
                        .sellPrice(30000)
                        .build()
        );
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .sellDt(LocalDate.of(2023,10,11))
                        .stockCode(stockCode)
                        .sellPrice(45000)
                        .build()
        );
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .sellDt(LocalDate.of(2023,10,13))
                        .stockCode(stockCode)
                        .sellPrice(55000)
                        .build()
        );
        StockSellRecordRepository stockSellRecordRepository = mock(StockSellRecordRepository.class);
        when(stockSellRecordRepository.findAll())
                .thenReturn(stockSellRecordEntities);

        StockRecordService stockRecordService = null;


        // 1
        //// when
        List<StockRecordDto> stockPurchaseRecordDtos = stockRecordService.readStockPurchaseRecordsBetweenPeriods(
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        List<StockRecordDto> stockSellRecordDtos = stockRecordService.readStockSellRecordsBetweenPeriods(
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        //// then
        assertThat(stockPurchaseRecordDtos.size()).isEqualTo(3);
        assertThat(stockSellRecordDtos.size()).isEqualTo(3);

        // 2
        //// when
        List<StockRecordDto> stockPurchaseRecordDtos2 = stockRecordService.readStockPurchaseRecordsBetweenPeriods(
                LocalDate.of(2023, 10, 8), LocalDate.of(2023, 10, 9));
        List<StockRecordDto> stockSellRecordDtos2 = stockRecordService.readStockSellRecordsBetweenPeriods(
                LocalDate.of(2023, 10, 8), LocalDate.of(2023, 10, 9));
        //// then
        //// one purchase record on the day 8th and one purchase on the day 9th
        assertThat(stockPurchaseRecordDtos2.size()).isEqualTo(2);
        assertThat(stockSellRecordDtos2.size()).isEqualTo(0);

        // 3
        //// when
        List<StockRecordDto> stockPurchaseRecordDtos3 = stockRecordService.readStockPurchaseRecordsBetweenPeriods(
                LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 10));
        List<StockRecordDto> stockSellRecordDtos3 = stockRecordService.readStockSellRecordsBetweenPeriods(
                LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 10));
        //// then
        //// one purchase and one sell on the day 10th.
        assertThat(stockPurchaseRecordDtos2.size()).isEqualTo(1);
        assertThat(stockSellRecordDtos2.size()).isEqualTo(1);
    }
}
