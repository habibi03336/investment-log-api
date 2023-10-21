package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.domain.StoryEntity;
import com.habibi.stockstoryapi.dto.StockStoryDto;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;
import com.habibi.stockstoryapi.repository.StoryRepository;
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
    private StoryRepository storyRepository;
    private StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private StockSellRecordRepository stockSellRecordRepository;
    @BeforeEach
    void setUp() {
        storyRepository = Mockito.mock(StoryRepository.class);
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
        when(storyRepository.findById(storyId)).thenReturn(
                Optional.ofNullable(
                        StoryEntity.builder()
                                .storyId(1)
                                .content(story)
                                .creationDt(creationDt)
                                .build()
                )
        );

        // when
        List<StockStoryDto> stockStoryDtos = stockStoryService.readPurchaseStoryOfCertainStock(stockCode);

        // then
        assertThat(stockStoryDtos.size()).isEqualTo(1);
        StockStoryDto stockStoryDto = stockStoryDtos.get(0);
        assertThat(stockStoryDto.getStockCode()).isEqualTo(stockCode);
        assertThat(stockStoryDto.getStockPrices().length).isEqualTo(2);
        assertThat(stockStoryDto.getStockPrices()).contains(50000);
        assertThat(stockStoryDto.getStockPrices()).contains(50100);
        assertThat(stockStoryDto.getStory()).isEqualTo(story);
        assertThat(stockStoryDto.getDt()).isEqualTo(creationDt);
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
        when(storyRepository.findById(storyId)).thenReturn(
                Optional.ofNullable(
                        StoryEntity.builder()
                                .storyId(1)
                                .content(story)
                                .creationDt(creationDt)
                                .build()
                )
        );

        // when
        List<StockStoryDto> stockStoryDtos = stockStoryService.readSellStoryOfCertainStock(stockCode);

        // then
        assertThat(stockStoryDtos.size()).isEqualTo(1);
        StockStoryDto stockStoryDto = stockStoryDtos.get(0);
        assertThat(stockStoryDto.getStockCode()).isEqualTo(stockCode);
        assertThat(stockStoryDto.getStockPrices().length).isEqualTo(2);
        assertThat(stockStoryDto.getStockPrices()).contains(60000);
        assertThat(stockStoryDto.getStockPrices()).contains(60100);
        assertThat(stockStoryDto.getStory()).isEqualTo(story);
        assertThat(stockStoryDto.getDt()).isEqualTo(creationDt);
    }
}
