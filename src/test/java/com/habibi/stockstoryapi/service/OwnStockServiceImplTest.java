package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.dto.OwnStockDto;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.JUnitException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OwnStockServiceImplTest {
    private StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private StockSellRecordRepository stockSellRecordRepository;
    private OwnStockService ownStockService;
    private StockCodeToNameMapper stockCodeToNameMapper;
    @BeforeEach
    void setUp() {
        stockPurchaseRecordRepository = mock(StockPurchaseRecordRepository.class);
        stockSellRecordRepository = mock(StockSellRecordRepository.class);
        stockCodeToNameMapper = mock(StockCodeToNameMapper.class);
        ownStockService = new OwnStockServiceImpl(stockPurchaseRecordRepository, stockSellRecordRepository, stockCodeToNameMapper);
    }
    @Test
    public void returnEmptyListWhenNoOwnStock(){
        // given
        int userId = 1;
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        when(stockPurchaseRecordRepository.findAllByUserId(userId))
                .thenReturn(stockPurchaseRecordEntities);
        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        when(stockSellRecordRepository.findAllByUserId(userId))
                .thenReturn(stockSellRecordEntities);

        // when
        List<OwnStockDto> ownStockDtos = ownStockService.readOwnStocks(userId);

        // then
        assertThat(ownStockDtos).isNotNull();
        assertThat(ownStockDtos.size()).isEqualTo(0);
    }

    @Test
    public void ownStockResultAccuracy(){
        // given
        int userId = 1;
        String stockCode1 = "000660";
        String stockCode2 = "066570";
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .userId(userId)
                        .purchaseDt(LocalDate.of(2023, 10, 8))
                        .stockCode(stockCode1)
                        .purchasePrice(45000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .userId(userId)
                        .purchaseDt(LocalDate.of(2023, 10, 9))
                        .stockCode(stockCode2)
                        .purchasePrice(65000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .userId(userId)
                        .purchaseDt(LocalDate.of(2023, 10, 10))
                        .stockCode(stockCode1)
                        .purchasePrice(52000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .userId(userId)
                        .purchaseDt(LocalDate.of(2023, 10, 11))
                        .stockCode(stockCode2)
                        .purchasePrice(67000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .userId(userId)
                        .purchaseDt(LocalDate.of(2023, 10, 13))
                        .stockCode(stockCode2)
                        .purchasePrice(70000)
                        .build()
        );
        when(stockPurchaseRecordRepository.findAllByUserId(userId))
                .thenReturn(stockPurchaseRecordEntities);

        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .userId(userId)
                        .sellDt(LocalDate.of(2023,10,13))
                        .stockCode(stockCode1)
                        .avgPurchasePrice((45000+52000)/2)
                        .sellPrice(30000)
                        .build()
        );
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .userId(userId)
                        .sellDt(LocalDate.of(2023,10,14))
                        .stockCode(stockCode2)
                        .avgPurchasePrice((65000+67000+70000)/3)
                        .sellPrice(68000)
                        .build()
        );
        when(stockSellRecordRepository.findAllByUserId(userId))
                .thenReturn(stockSellRecordEntities);


        // when
        List<OwnStockDto> ownStockDtos = ownStockService.readOwnStocks(userId);

        // expected
        assertThat(ownStockDtos.size()).isEqualTo(2);
        for(OwnStockDto ownStockDto : ownStockDtos){
            if(ownStockDto.getStockCode().equals(stockCode1)){
                assertThat(ownStockDto.getStockCount()).isEqualTo(1);
                assertThat(ownStockDto.getAveragePurchasePrice()).isEqualTo((45000+52000)/2);
                continue;
            }
            if(ownStockDto.getStockCode().equals(stockCode2)){
                assertThat(ownStockDto.getStockCount()).isEqualTo(2);
                assertThat(ownStockDto.getAveragePurchasePrice()).isEqualTo((65000+67000+70000)/3);
                continue;
            }
            throw new JUnitException("unexpected stock code had returned");
        }
    }

    @Test
    public void ownStocksAtSomePointAccuracy(){
        // given
        int userId = 1;
        String stockCode1 = "000660";
        String stockCode2 = "066570";
        LocalDate date = LocalDate.of(2023, 10, 13);

        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .userId(userId)
                        .purchaseDt(LocalDate.of(2023, 10, 8))
                        .stockCode(stockCode1)
                        .purchasePrice(45000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .userId(userId)
                        .purchaseDt(LocalDate.of(2023, 10, 9))
                        .stockCode(stockCode2)
                        .purchasePrice(65000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .userId(userId)
                        .purchaseDt(LocalDate.of(2023, 10, 10))
                        .stockCode(stockCode1)
                        .purchasePrice(52000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .userId(userId)
                        .purchaseDt(LocalDate.of(2023, 10, 11))
                        .stockCode(stockCode2)
                        .purchasePrice(67000)
                        .build()
        );
        when(stockPurchaseRecordRepository.findAllByUserIdAndPurchaseDtIsBefore(eq(userId), any(LocalDate.class)))
                .thenReturn(stockPurchaseRecordEntities);

        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .userId(userId)
                        .sellDt(LocalDate.of(2023,10,9))
                        .stockCode(stockCode1)
                        .sellPrice(30000)
                        .avgPurchasePrice(45000)
                        .build()
        );
        when(stockSellRecordRepository.findAllByUserIdAndSellDtIsBefore(eq(userId), any(LocalDate.class)))
                .thenReturn(stockSellRecordEntities);

        // when
        List<OwnStockDto> ownStockDtos = ownStockService.readOwnStocksAtSomePoint(userId, date);

        // then
        assertThat(ownStockDtos.size()).isEqualTo(2);
        for(OwnStockDto ownStockDto : ownStockDtos){
            if(ownStockDto.getStockCode().equals(stockCode1)){
                assertThat(ownStockDto.getStockCount()).isEqualTo(1);
                assertThat(ownStockDto.getAveragePurchasePrice()).isEqualTo(52000);
                continue;
            }
            if(ownStockDto.getStockCode().equals(stockCode2)){
                assertThat(ownStockDto.getStockCount()).isEqualTo(2);
                assertThat(ownStockDto.getAveragePurchasePrice()).isEqualTo((65000+67000) / 2);
                continue;
            }
            throw new JUnitException("unexpected stock code had returned");
        }
    }
}
