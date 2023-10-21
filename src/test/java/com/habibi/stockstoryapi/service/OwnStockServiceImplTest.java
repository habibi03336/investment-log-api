package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.dto.OwnStockDto;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.JUnitException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OwnStockServiceImplTest {
    @Test
    public void testReturnSize0ListWhenNoOwnStock(){
        // given
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        StockPurchaseRecordRepository stockPurchaseRecordRepository = mock(StockPurchaseRecordRepository.class);
        when(stockPurchaseRecordRepository.findAll())
                .thenReturn(stockPurchaseRecordEntities);
        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        StockSellRecordRepository stockSellRecordRepository = mock(StockSellRecordRepository.class);
        when(stockSellRecordRepository.findAll())
                .thenReturn(stockSellRecordEntities);

        OwnStockService ownStockService = null;

        // when
        List<OwnStockDto> ownStockDtos = ownStockService.readOwnStocks();

        // then
        assertThat(ownStockDtos).isNotNull();
        assertThat(ownStockDtos.size()).isEqualTo(0);
    }

    @Test
    public void testOnlyPurchaseRecordExist(){
        // given
        String stockCode = "000660";
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 8))
                        .stockCode(stockCode)
                        .purchasePrice(45000)
                        .build()
        );
        StockPurchaseRecordRepository stockPurchaseRecordRepository = mock(StockPurchaseRecordRepository.class);
        when(stockPurchaseRecordRepository.findAll())
                .thenReturn(stockPurchaseRecordEntities);
        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        StockSellRecordRepository stockSellRecordRepository = mock(StockSellRecordRepository.class);
        when(stockSellRecordRepository.findAll())
                .thenReturn(stockSellRecordEntities);

        OwnStockService ownStockService = null;

        // when
        List<OwnStockDto> ownStockDtos = ownStockService.readOwnStocks();

        // then
        assertThat(ownStockDtos.size()).isEqualTo(1);
        OwnStockDto ownStockDto = ownStockDtos.get(0);
        assertThat(ownStockDto.getStockCode()).isEqualTo(stockCode);
        assertThat(ownStockDto.getStockCount()).isEqualTo(1);
        assertThat(ownStockDto.getAveragePurchasePrice()).isEqualTo(45000);
    }

    @Test
    public void testPurchaseSellRecordBothExist(){
        // given
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
                        .purchaseDt(LocalDate.of(2023, 10, 9))
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
        StockSellRecordRepository stockSellRecordRepository = mock(StockSellRecordRepository.class);
        when(stockSellRecordRepository.findAll())
                .thenReturn(stockSellRecordEntities);

        OwnStockService ownStockService = null;

        // when
        List<OwnStockDto> ownStockDtos = ownStockService.readOwnStocks();

        // then
        assertThat(ownStockDtos.size()).isEqualTo(1);
        OwnStockDto ownStockDto = ownStockDtos.get(0);
        assertThat(ownStockDto.getStockCode()).isEqualTo(stockCode);
        assertThat(ownStockDto.getStockCount()).isEqualTo(2);
        assertThat(ownStockDto.getAveragePurchasePrice()).isEqualTo((45000+55000+58000)/3);
    }

    @Test
    public void testOwnStockAtSomePoint(){
        // given
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
                        .purchaseDt(LocalDate.of(2023, 10, 9))
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
                        .sellDt(LocalDate.of(2023,10,11))
                        .stockCode(stockCode)
                        .sellPrice(55000)
                        .build()
        );
        StockSellRecordRepository stockSellRecordRepository = mock(StockSellRecordRepository.class);
        when(stockSellRecordRepository.findAll())
                .thenReturn(stockSellRecordEntities);

        OwnStockService ownStockService = null;

        // when & expected 1
        // should be no own stocks, because it returns own stocks at the start of the day.
        List<OwnStockDto> ownStockDtos = ownStockService.readOwnStocksAtSomePoint(LocalDate.of(2023, 10, 8));
        assertThat(ownStockDtos.size()).isEqualTo(0);

        // when & expected 2
        ownStockDtos = ownStockService.readOwnStocksAtSomePoint(LocalDate.of(2023, 10, 10));
        assertThat(ownStockDtos.size()).isEqualTo(1);
        OwnStockDto ownStockDto = ownStockDtos.get(0);
        assertThat(ownStockDto.getStockCode()).isEqualTo(stockCode);
        assertThat(ownStockDto.getStockCount()).isEqualTo(3);
        assertThat(ownStockDto.getAveragePurchasePrice()).isEqualTo((45000 + 55000 + 58000)/3);

        // when & expected 3
        ownStockDtos = ownStockService.readOwnStocksAtSomePoint(LocalDate.of(2023, 10, 11));
        assertThat(ownStockDtos.size()).isEqualTo(1);
        ownStockDto = ownStockDtos.get(0);
        assertThat(ownStockDto.getStockCode()).isEqualTo(stockCode);
        assertThat(ownStockDto.getStockCount()).isEqualTo(2);
        assertThat(ownStockDto.getAveragePurchasePrice()).isEqualTo((45000+55000+58000)/3);

        // when & expected 4
        ownStockDtos = ownStockService.readOwnStocksAtSomePoint(LocalDate.of(2023, 10, 12));
        assertThat(ownStockDtos.size()).isEqualTo(0);
    }


    @Test
    public void testManyKindsOfOwnStocks(){
        // given
        String stockCode1 = "000660";
        String stockCode2 = "066570";
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 8))
                        .stockCode(stockCode1)
                        .purchasePrice(45000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 9))
                        .stockCode(stockCode2)
                        .purchasePrice(65000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 10))
                        .stockCode(stockCode1)
                        .purchasePrice(52000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 11))
                        .stockCode(stockCode2)
                        .purchasePrice(67000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 13))
                        .stockCode(stockCode2)
                        .purchasePrice(70000)
                        .build()
        );
        StockPurchaseRecordRepository stockPurchaseRecordRepository = mock(StockPurchaseRecordRepository.class);
        when(stockPurchaseRecordRepository.findAll())
                .thenReturn(stockPurchaseRecordEntities);

        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .sellDt(LocalDate.of(2023,10,13))
                        .stockCode(stockCode1)
                        .sellPrice(30000)
                        .build()
        );
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .sellDt(LocalDate.of(2023,10,14))
                        .stockCode(stockCode2)
                        .sellPrice(68000)
                        .build()
        );

        StockSellRecordRepository stockSellRecordRepository = mock(StockSellRecordRepository.class);
        when(stockSellRecordRepository.findAll())
                .thenReturn(stockSellRecordEntities);

        OwnStockService ownStockService = null;

        // when
        List<OwnStockDto> ownStockDtos = ownStockService.readOwnStocks();

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
    public void testManyKindsOfOwnStocksAtSomePoint(){
        // given
        String stockCode1 = "000660";
        String stockCode2 = "066570";
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 8))
                        .stockCode(stockCode1)
                        .purchasePrice(45000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 9))
                        .stockCode(stockCode2)
                        .purchasePrice(65000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 10))
                        .stockCode(stockCode1)
                        .purchasePrice(52000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 11))
                        .stockCode(stockCode2)
                        .purchasePrice(67000)
                        .build()
        );
        stockPurchaseRecordEntities.add(
                StockPurchaseRecordEntity.builder()
                        .purchaseDt(LocalDate.of(2023, 10, 13))
                        .stockCode(stockCode2)
                        .purchasePrice(70000)
                        .build()
        );
        StockPurchaseRecordRepository stockPurchaseRecordRepository = mock(StockPurchaseRecordRepository.class);
        when(stockPurchaseRecordRepository.findAll())
                .thenReturn(stockPurchaseRecordEntities);

        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        stockSellRecordEntities.add(
                StockSellRecordEntity.builder()
                        .sellDt(LocalDate.of(2023,10,9))
                        .stockCode(stockCode1)
                        .sellPrice(30000)
                        .avgPurchasePrice(45000)
                        .build()
        );

        StockSellRecordRepository stockSellRecordRepository = mock(StockSellRecordRepository.class);
        when(stockSellRecordRepository.findAll())
                .thenReturn(stockSellRecordEntities);

        OwnStockService ownStockService = null;

        // when & expected 1
        List<OwnStockDto> ownStockDtos1 = ownStockService.readOwnStocksAtSomePoint(LocalDate.of(2023, 10, 8));
        assertThat(ownStockDtos1.size()).isEqualTo(0);

        // when & expected 2
        List<OwnStockDto> ownStockDtos2 = ownStockService.readOwnStocksAtSomePoint(LocalDate.of(2023, 10, 10));
        assertThat(ownStockDtos2.size()).isEqualTo(1);
        OwnStockDto ownStockDto2 = ownStockDtos2.get(0);
        assertThat(ownStockDto2.getStockCode()).isEqualTo(stockCode2);
        assertThat(ownStockDto2.getStockCount()).isEqualTo(1);
        assertThat(ownStockDto2.getAveragePurchasePrice()).isEqualTo(65000);

        // when $ expected 3
        List<OwnStockDto> ownStockDtos3 = ownStockService.readOwnStocksAtSomePoint(LocalDate.of(2023, 10, 13));
        assertThat(ownStockDtos3.size()).isEqualTo(2);
        for(OwnStockDto ownStockDto : ownStockDtos3){
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
