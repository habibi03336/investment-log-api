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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class StockPositionStoryServiceTest {
    private StockPositionStoryService stockPositionStoryService;
    private StockPositionStoryRepository stockPositionStoryRepository;
    private StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private StockSellRecordRepository stockSellRecordRepository;
    @BeforeEach
    void setUp() {
        stockPositionStoryRepository = Mockito.mock(StockPositionStoryRepository.class);
        stockPurchaseRecordRepository = Mockito.mock(StockPurchaseRecordRepository.class);
        stockSellRecordRepository = Mockito.mock(StockSellRecordRepository.class);
        stockPositionStoryService = new StockPositionStoryServiceImpl(stockPurchaseRecordRepository, stockSellRecordRepository, stockPositionStoryRepository);
    }
    @Test
    public void readStockLongPositionStoryOfCertainStock(){
        // given
        String stockCode = "035420";
        // same storyId -> same stock purchase/sell date
        long storyId1 = 1;
        LocalDate stockPurchaseDate1 = LocalDate.of(2023, 10, 10);
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        stockPurchaseRecordEntities.add(StockPurchaseRecordEntity
                .builder()
                .stockCode(stockCode)
                .purchasePrice(50000)
                .storyId(storyId1)
                .purchaseDt(stockPurchaseDate1)
                .build()
        );
        stockPurchaseRecordEntities.add(StockPurchaseRecordEntity
                .builder()
                .stockCode(stockCode)
                .purchasePrice(50100)
                .storyId(storyId1)
                .purchaseDt(stockPurchaseDate1)
                .build()
        );
        long storyId2 = 2;
        LocalDate stockPurchaseDate2 = LocalDate.of(2023, 10, 23);
        stockPurchaseRecordEntities.add(StockPurchaseRecordEntity
                .builder()
                .stockCode(stockCode)
                .purchasePrice(60000)
                .storyId(storyId2)
                .purchaseDt(stockPurchaseDate2)
                .build()
        );
        when(stockPurchaseRecordRepository.findAllByStockCode(stockCode)).thenReturn(stockPurchaseRecordEntities);
        String story1 = "Naver is great company";
        when(stockPositionStoryRepository.findByStoryId(storyId1)).thenReturn(
            StockPositionStoryEntity.builder()
                    .storyId(storyId1)
                    .content(story1)
                    .build()
        );
        String story2 = "Naver has great potential";
        when(stockPositionStoryRepository.findByStoryId(storyId2)).thenReturn(
            StockPositionStoryEntity.builder()
                    .storyId(storyId2)
                    .content(story2)
                    .build()
        );

        // when
        List<StockPositionStoryDto> stockPositionStoryDtos = stockPositionStoryService.readStockLongPositionStoryOfCertainStock(stockCode);

        // then
        assertThat(stockPositionStoryDtos.size()).isEqualTo(2);

        // latest one comes first
        StockPositionStoryDto stockPositionStoryDtoOfStoryId2 = stockPositionStoryDtos.get(0);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockCode()).isEqualTo(stockCode);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockPrices().length).isEqualTo(1);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockPrices()[0]).isEqualTo(60000);
        assertThat(stockPositionStoryDtoOfStoryId2.getStory()).isEqualTo(story2);
        assertThat(stockPositionStoryDtoOfStoryId2.getDt()).isEqualTo(stockPurchaseDate2);

        StockPositionStoryDto stockPositionStoryDtoOfStoryId1 = stockPositionStoryDtos.get(1);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockCode()).isEqualTo(stockCode);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockPrices().length).isEqualTo(2);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockPrices()).contains(50000);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockPrices()).contains(50100);
        assertThat(stockPositionStoryDtoOfStoryId1.getStory()).isEqualTo(story1);
        assertThat(stockPositionStoryDtoOfStoryId1.getDt()).isEqualTo(stockPurchaseDate1);
    }
    @Test
    public void readStockShortPositionStoryOfCertainStock(){
        // given
        String stockCode = "035420";
        long storyId1 = 1;
        LocalDate stockSellDate1 = LocalDate.of(2023, 10, 21);
        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        stockSellRecordEntities.add(StockSellRecordEntity
                .builder()
                .stockCode(stockCode)
                .sellPrice(60000)
                .storyId(storyId1)
                .sellDt(stockSellDate1)
                .build()
        );
        long storyId2 = 2;
        LocalDate stockSellDate2 = LocalDate.of(2023, 10, 22);
        stockSellRecordEntities.add(StockSellRecordEntity
                .builder()
                .stockCode(stockCode)
                .sellPrice(78000)
                .storyId(storyId2)
                .sellDt(stockSellDate2)
                .build()
        );
        stockSellRecordEntities.add(StockSellRecordEntity
                .builder()
                .stockCode(stockCode)
                .sellPrice(81000)
                .storyId(storyId2)
                .sellDt(stockSellDate2)
                .build()
        );
        when(stockSellRecordRepository.findAllByStockCode(stockCode)).thenReturn(stockSellRecordEntities);
        String story1 = "IT market is too much hyped";
        when(stockPositionStoryRepository.findById(storyId1)).thenReturn(
                Optional.ofNullable(
                        StockPositionStoryEntity.builder()
                                .storyId(storyId1)
                                .content(story1)
                                .build()
                )
        );
        String story2 = "IT market is way too much hyped";
        when(stockPositionStoryRepository.findById(storyId2)).thenReturn(
                Optional.ofNullable(
                        StockPositionStoryEntity.builder()
                                .storyId(storyId2)
                                .content(story2)
                                .build()
                )
        );

        // when
        List<StockPositionStoryDto> stockPositionStoryDtos = stockPositionStoryService.readStockShortPositionStoryOfCertainStock(stockCode);

        // then
        // latest one comes first
        assertThat(stockPositionStoryDtos.size()).isEqualTo(2);
        StockPositionStoryDto stockPositionStoryDtoOfStoryId2 = stockPositionStoryDtos.get(0);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockCode()).isEqualTo(stockCode);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockPrices().length).isEqualTo(2);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockPrices()).contains(78000);
        assertThat(stockPositionStoryDtoOfStoryId2.getStockPrices()).contains(81000);
        assertThat(stockPositionStoryDtoOfStoryId2.getStory()).isEqualTo(story2);
        assertThat(stockPositionStoryDtoOfStoryId2.getDt()).isEqualTo(stockSellDate2);
        
        StockPositionStoryDto stockPositionStoryDtoOfStoryId1 = stockPositionStoryDtos.get(1);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockCode()).isEqualTo(stockCode);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockPrices().length).isEqualTo(1);
        assertThat(stockPositionStoryDtoOfStoryId1.getStockPrices()[0]).isEqualTo(60000);
        assertThat(stockPositionStoryDtoOfStoryId1.getStory()).isEqualTo(story1);
        assertThat(stockPositionStoryDtoOfStoryId1.getDt()).isEqualTo(stockSellDate1);
    }
}
