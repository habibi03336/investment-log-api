package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.domain.StockPositionStoryEntity;
import com.habibi.stockstoryapi.dto.StockPositionStoryDto;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;
import com.habibi.stockstoryapi.repository.StockPositionStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class StockStoryServiceTest {
    private StockStoryService stockStoryService;
    private StockPositionStoryRepository stockPositionStoryRepository;
    private StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private StockSellRecordRepository stockSellRecordRepository;
    @BeforeEach
    void setUp() {
        stockPositionStoryRepository = Mockito.mock(StockPositionStoryRepository.class);
        stockPurchaseRecordRepository = Mockito.mock(StockPurchaseRecordRepository.class);
        stockSellRecordRepository = Mockito.mock(StockSellRecordRepository.class);
        stockStoryService = null;
    }
    @Test
    public void readStockLongPositionStoryOfCertainStock(){
        // given
        String stockCode = "035420";
        long storyId1 = 1;
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        stockPurchaseRecordEntities.add(StockPurchaseRecordEntity
                .builder()
                .stockCode(stockCode)
                .purchasePrice(50000)
                .storyId(storyId1)
                .build()
        );
        stockPurchaseRecordEntities.add(StockPurchaseRecordEntity
                .builder()
                .stockCode(stockCode)
                .purchasePrice(50100)
                .storyId(storyId1)
                .build()
        );
        long storyId2 = 2;
        stockPurchaseRecordEntities.add(StockPurchaseRecordEntity
                .builder()
                .stockCode(stockCode)
                .purchasePrice(60000)
                .storyId(storyId2)
                .build()
        );
        when(stockPurchaseRecordRepository.findAllByStockCode(stockCode)).thenReturn(stockPurchaseRecordEntities);
        LocalDateTime creationDt1 = LocalDateTime.of(2023, 10, 21, 0, 0, 0);
        String story1 = "Naver is great company";
        when(stockPositionStoryRepository.findById(storyId1)).thenReturn(
                Optional.ofNullable(
                        StockPositionStoryEntity.builder()
                                .storyId(storyId1)
                                .content(story1)
                                .creationDt(creationDt1)
                                .build()
                )
        );
        LocalDateTime creationDt2 = LocalDateTime.of(2023, 10, 23, 0, 0, 0);
        String story2 = "Naver has great potential";
        when(stockPositionStoryRepository.findById(storyId2)).thenReturn(
                Optional.ofNullable(
                        StockPositionStoryEntity.builder()
                                .storyId(storyId2)
                                .content(story2)
                                .creationDt(creationDt2)
                                .build()
                )
        );

        // when
        List<StockPositionStoryDto> stockPositionStoryDtos = stockStoryService.readStockLongPositionStoryOfCertainStock(stockCode);

        // then
        assertThat(stockPositionStoryDtos.size()).isEqualTo(2);

        // latest one comes first
        StockPositionStoryDto stockPositionStoryDtoOfStoryId2 = stockPositionStoryDtos.get(0);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockCode()).isEqualTo(stockCode);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockPrices().length).isEqualTo(1);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockPrices()[0]).isEqualTo(60000);
        assertThat(stockPositionStoryDtoOfStoryId2.getStory()).isEqualTo(story2);
        assertThat(stockPositionStoryDtoOfStoryId2.getDt()).isEqualTo(creationDt2);

        StockPositionStoryDto stockPositionStoryDtoOfStoryId1 = stockPositionStoryDtos.get(1);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockCode()).isEqualTo(stockCode);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockPrices().length).isEqualTo(2);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockPrices()).contains(50000);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockPrices()).contains(50100);
        assertThat(stockPositionStoryDtoOfStoryId1.getStory()).isEqualTo(story1);
        assertThat(stockPositionStoryDtoOfStoryId1.getDt()).isEqualTo(creationDt1);
    }
    @Test
    public void readStockShortPositionStoryOfCertainStock(){
        // given
        String stockCode = "035420";
        long storyId1 = 1;
        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        stockSellRecordEntities.add(StockSellRecordEntity
                .builder()
                .stockCode(stockCode)
                .sellPrice(60000)
                .storyId(storyId1)
                .build()
        );
        long storyId2 = 2;
        stockSellRecordEntities.add(StockSellRecordEntity
                .builder()
                .stockCode(stockCode)
                .sellPrice(78000)
                .storyId(storyId2)
                .build()
        );
        stockSellRecordEntities.add(StockSellRecordEntity
                .builder()
                .stockCode(stockCode)
                .sellPrice(81000)
                .storyId(storyId2)
                .build()
        );
        when(stockSellRecordRepository.findAllByStockCode(stockCode)).thenReturn(stockSellRecordEntities);

        String story1 = "IT market is too much hyped";
        LocalDateTime creationDt1 = LocalDateTime.of(2023, 10, 21, 0, 0, 0);
        when(stockPositionStoryRepository.findById(storyId1)).thenReturn(
                Optional.ofNullable(
                        StockPositionStoryEntity.builder()
                                .storyId(storyId1)
                                .content(story1)
                                .creationDt(creationDt1)
                                .build()
                )
        );
        String story2 = "IT market is way too much hyped";
        LocalDateTime creationDt2 = LocalDateTime.of(2023, 10, 23, 0, 0, 0);
        when(stockPositionStoryRepository.findById(storyId2)).thenReturn(
                Optional.ofNullable(
                        StockPositionStoryEntity.builder()
                                .storyId(storyId2)
                                .content(story2)
                                .creationDt(creationDt2)
                                .build()
                )
        );

        // when
        List<StockPositionStoryDto> stockPositionStoryDtos = stockStoryService.readStockShortPositionStoryOfCertainStock(stockCode);

        // then
        // latest one comes first
        assertThat(stockPositionStoryDtos.size()).isEqualTo(2);
        StockPositionStoryDto stockPositionStoryDtoOfStoryId2 = stockPositionStoryDtos.get(0);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockCode()).isEqualTo(stockCode);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockPrices().length).isEqualTo(2);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockPrices()).contains(78000);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockPrices()).contains(81000);
        assertThat(stockPositionStoryDtoOfStoryId2.getStory()).isEqualTo(story2);
        assertThat(stockPositionStoryDtoOfStoryId2.getDt()).isEqualTo(creationDt2);
        
        StockPositionStoryDto stockPositionStoryDtoOfStoryId1 = stockPositionStoryDtos.get(1);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockCode()).isEqualTo(stockCode);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockPrices().length).isEqualTo(1);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockPrices()[0]).isEqualTo(60000);
        assertThat(stockPositionStoryDtoOfStoryId1.getStory()).isEqualTo(story1);
        assertThat(stockPositionStoryDtoOfStoryId1.getDt()).isEqualTo(creationDt1);
    }
}
