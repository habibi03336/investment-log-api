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
    public void testReadPurchaseStoryOfCertainStock(){
        // given
        String stockCode = "035420";
        long storyId = 1;
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        stockPurchaseRecordEntities.add(StockPurchaseRecordEntity
                .builder()
                .stockCode(stockCode)
                .purchasePrice(50000)
                .storyId(storyId)
                .build()
        );
        stockPurchaseRecordEntities.add(StockPurchaseRecordEntity
                .builder()
                .stockCode(stockCode)
                .purchasePrice(50100)
                .storyId(storyId)
                .build()
        );
        String story = "Naver is great company";
        LocalDateTime creationDt = LocalDateTime.of(2023, 10, 21, 0, 0, 0);
        when(stockPurchaseRecordRepository.findAllByStockCode(stockCode)).thenReturn(stockPurchaseRecordEntities);
        when(stockPositionStoryRepository.findById(storyId)).thenReturn(
                Optional.ofNullable(
                        StockPositionStoryEntity.builder()
                                .storyId(1)
                                .content(story)
                                .creationDt(creationDt)
                                .build()
                )
        );

        // when
        List<StockPositionStoryDto> stockPositionStoryDtos = stockStoryService.readPurchaseStoryOfCertainStock(stockCode);

        // then
        assertThat(stockPositionStoryDtos.size()).isEqualTo(1);
        StockPositionStoryDto stockPositionStoryDto = stockPositionStoryDtos.get(0);
        assertThat(stockPositionStoryDto.getStockCode()).isEqualTo(stockCode);
        assertThat(stockPositionStoryDto.getStockPrices().length).isEqualTo(2);
        assertThat(stockPositionStoryDto.getStockPrices()).contains(50000);
        assertThat(stockPositionStoryDto.getStockPrices()).contains(50100);
        assertThat(stockPositionStoryDto.getStory()).isEqualTo(story);
        assertThat(stockPositionStoryDto.getDt()).isEqualTo(creationDt);
    }
    @Test
    public void testReadSellStoryOfCertainStock(){
        // given
        String stockCode = "035420";
        long storyId = 1;
        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        stockSellRecordEntities.add(StockSellRecordEntity
                .builder()
                .stockCode(stockCode)
                .sellPrice(60000)
                .storyId(storyId)
                .build()
        );
        stockSellRecordEntities.add(StockSellRecordEntity
                .builder()
                .stockCode(stockCode)
                .sellPrice(60100)
                .storyId(storyId)
                .build()
        );
        String story = "IT market is too much hyped";
        LocalDateTime creationDt = LocalDateTime.of(2023, 10, 21, 0, 0, 0);
        when(stockSellRecordRepository.findAllByStockCode(stockCode)).thenReturn(stockSellRecordEntities);
        when(stockPositionStoryRepository.findById(storyId)).thenReturn(
                Optional.ofNullable(
                        StockPositionStoryEntity.builder()
                                .storyId(1)
                                .content(story)
                                .creationDt(creationDt)
                                .build()
                )
        );

        // when
        List<StockPositionStoryDto> stockPositionStoryDtos = stockStoryService.readSellStoryOfCertainStock(stockCode);

        // then
        assertThat(stockPositionStoryDtos.size()).isEqualTo(1);
        StockPositionStoryDto stockPositionStoryDto = stockPositionStoryDtos.get(0);
        assertThat(stockPositionStoryDto.getStockCode()).isEqualTo(stockCode);
        assertThat(stockPositionStoryDto.getStockPrices().length).isEqualTo(2);
        assertThat(stockPositionStoryDto.getStockPrices()).contains(60000);
        assertThat(stockPositionStoryDto.getStockPrices()).contains(60100);
        assertThat(stockPositionStoryDto.getStory()).isEqualTo(story);
        assertThat(stockPositionStoryDto.getDt()).isEqualTo(creationDt);
    }
}
