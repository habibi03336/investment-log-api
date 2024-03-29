package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.dto.StockRecordDto;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockRecordServiceTest {

    private StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private StockSellRecordRepository stockSellRecordRepository;
    private StockRecordService stockRecordService;
    private StockCodeToNameMapper stockCodeToNameMapper;

    @BeforeEach
    void setUp() {
        stockPurchaseRecordRepository = mock(StockPurchaseRecordRepository.class);
        stockSellRecordRepository = mock(StockSellRecordRepository.class);
        stockCodeToNameMapper = mock(StockCodeToNameMapper.class);
        stockRecordService = new StockRecordServiceImpl(stockPurchaseRecordRepository, stockSellRecordRepository, stockCodeToNameMapper);
    }

    @Test
    public void returnEmptyListWhenNoStockRecord(){
        //given
        int userId = 1;
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        when(stockPurchaseRecordRepository.findAllByUserIdAndPurchaseDtIsBetween(eq(userId), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(stockPurchaseRecordEntities);
        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        when(stockSellRecordRepository.findAllByUserIdAndSellDtIsBetween(eq(userId), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(stockSellRecordEntities);

        // when
        List<StockRecordDto> stockPurchaseRecordDtos = stockRecordService.readStockPurchaseRecordsBetweenPeriods(
                userId,
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)
        );
        List<StockRecordDto> stockSellRecordDtos = stockRecordService.readStockSellRecordsBetweenPeriods(
                userId,
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)
        );

        // then
        assertThat(stockPurchaseRecordDtos).isNotNull();
        assertThat(stockPurchaseRecordDtos.size()).isEqualTo(0);
        assertThat(stockSellRecordDtos).isNotNull();
        assertThat(stockSellRecordDtos.size()).isEqualTo(0);
    }
}
