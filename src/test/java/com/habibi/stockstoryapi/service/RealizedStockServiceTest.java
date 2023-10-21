package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.dto.RealizedStockDto;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.JUnitException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RealizedStockServiceTest {

    private StockSellRecordRepository stockSellRecordRepository;
    private RealizedStockService realizedStockService;
    @BeforeEach
    void setUp() {
        stockSellRecordRepository = mock(StockSellRecordRepository.class);
        realizedStockService = null;
    }

    @Test
    public void returnEmptyListWhenNoRealizedStock(){
        //given
        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        when(stockSellRecordRepository.findAll())
                .thenReturn(stockSellRecordEntities);

        // when
        List<RealizedStockDto> realizedStockDtos = realizedStockService.readRealizedStocks();

        // then
        assertThat(realizedStockDtos).isNotNull();
        assertThat(realizedStockDtos.size()).isEqualTo(0);
    }

    @Test
    public void realizedStocksResultAccuracy(){
        //given
        String stockCode1 = "035420";
        String stockCode2 = "035720";
        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .sellDt(LocalDate.of(2023,10,6))
                        .stockCode(stockCode1)
                        .sellPrice(65000)
                        .avgPurchasePrice(42000)
                        .build()
        );
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .sellDt(LocalDate.of(2023,10,13))
                        .stockCode(stockCode1)
                        .sellPrice(80000)
                        .avgPurchasePrice(42000)
                        .build()
        );
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .sellDt(LocalDate.of(2023,10,20))
                        .stockCode(stockCode1)
                        .sellPrice(35000)
                        .avgPurchasePrice(82300)
                        .build()
        );
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .sellDt(LocalDate.of(2023,10,20))
                        .stockCode(stockCode2)
                        .sellPrice(39050)
                        .avgPurchasePrice(112000)
                        .build()
        );
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .sellDt(LocalDate.of(2023,10,23))
                        .stockCode(stockCode2)
                        .sellPrice(38750)
                        .avgPurchasePrice(112000)
                        .build()
        );
        when(stockSellRecordRepository.findAll())
                .thenReturn(stockSellRecordEntities);

        // when
        List<RealizedStockDto> realizedStockDtos = realizedStockService.readRealizedStocks();

        // then
        assertThat(realizedStockDtos.size()).isEqualTo(2);
        for(RealizedStockDto realizedStockDto : realizedStockDtos){
            if(realizedStockDto.getStockCode().equals(stockCode1)){
                assertThat(realizedStockDto.getStockCount()).isEqualTo(3);
                assertThat(realizedStockDto.getAverageSellPrice()).isEqualTo((65000+80000+35000)/3);
                assertThat(realizedStockDto.getAveragePurchasePrice()).isEqualTo((42000+42000+82300)/3);
                continue;
            }
            if(realizedStockDto.getStockCode().equals(stockCode2)){
                assertThat(realizedStockDto.getStockCount()).isEqualTo(2);
                assertThat(realizedStockDto.getAverageSellPrice()).isEqualTo((39050+38750)/2);
                assertThat(realizedStockDto.getAveragePurchasePrice()).isEqualTo((112000+112000)/2);
                continue;
            }
            throw new JUnitException("unexpected stock code had returned");
        }
    }
}
