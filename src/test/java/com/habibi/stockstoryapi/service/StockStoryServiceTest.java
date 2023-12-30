package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.domain.StockStoryEntity;
import com.habibi.stockstoryapi.dto.CreateStatusDto;
import com.habibi.stockstoryapi.dto.StockStoryDto;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;
import com.habibi.stockstoryapi.repository.StockPositionStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockStoryServiceTest {
    private StockStoryService stockStoryService;
    private StockPositionStoryRepository stockPositionStoryRepository;
    private StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private StockSellRecordRepository stockSellRecordRepository;
    private StockCodeToNameMapper stockCodeToNameMapper;

    @BeforeEach
    void setUp() {
        stockPositionStoryRepository = Mockito.mock(StockPositionStoryRepository.class);
        stockPurchaseRecordRepository = Mockito.mock(StockPurchaseRecordRepository.class);
        stockSellRecordRepository = Mockito.mock(StockSellRecordRepository.class);
        stockCodeToNameMapper = mock(StockCodeToNameMapper.class);
        stockStoryService = new StockStoryServiceImpl(
                stockPurchaseRecordRepository,
                stockSellRecordRepository,
                stockPositionStoryRepository,
                stockCodeToNameMapper
        );
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
            StockStoryEntity.builder()
                    .storyId(storyId1)
                    .story(story1)
                    .build()
        );
        String story2 = "Naver has great potential";
        when(stockPositionStoryRepository.findByStoryId(storyId2)).thenReturn(
            StockStoryEntity.builder()
                    .storyId(storyId2)
                    .story(story2)
                    .build()
        );

        // when
        List<StockStoryDto> stockStoryDtos = stockStoryService.readStockLongPositionStoryOfCertainStock(stockCode);

        // then
        assertThat(stockStoryDtos.size()).isEqualTo(2);

        // latest one comes first
        StockStoryDto stockStoryDtoOfStoryId2 = stockStoryDtos.get(0);
        assertThat(stockStoryDtoOfStoryId2.getStockCode()).isEqualTo(stockCode);
        assertThat(stockStoryDtoOfStoryId2.getStockPrices().length).isEqualTo(1);
        assertThat(stockStoryDtoOfStoryId2.getStockPrices()[0]).isEqualTo(60000);
        assertThat(stockStoryDtoOfStoryId2.getStory()).isEqualTo(story2);
        assertThat(stockStoryDtoOfStoryId2.getDt()).isEqualTo(stockPurchaseDate2);

        StockStoryDto stockStoryDtoOfStoryId1 = stockStoryDtos.get(1);
        assertThat(stockStoryDtoOfStoryId1.getStockCode()).isEqualTo(stockCode);
        assertThat(stockStoryDtoOfStoryId1.getStockPrices().length).isEqualTo(2);
        assertThat(stockStoryDtoOfStoryId1.getStockPrices()).contains(50000);
        assertThat(stockStoryDtoOfStoryId1.getStockPrices()).contains(50100);
        assertThat(stockStoryDtoOfStoryId1.getStory()).isEqualTo(story1);
        assertThat(stockStoryDtoOfStoryId1.getDt()).isEqualTo(stockPurchaseDate1);
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
                .avgPurchasePrice(50000)
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
                .avgPurchasePrice(60000)
                .storyId(storyId2)
                .sellDt(stockSellDate2)
                .build()
        );
        stockSellRecordEntities.add(StockSellRecordEntity
                .builder()
                .stockCode(stockCode)
                .sellPrice(81000)
                .avgPurchasePrice(70000)
                .storyId(storyId2)
                .sellDt(stockSellDate2)
                .build()
        );
        when(stockSellRecordRepository.findAllByStockCode(stockCode)).thenReturn(stockSellRecordEntities);
        String story1 = "IT market is too much hyped";
        when(stockPositionStoryRepository.findByStoryId(storyId1)).thenReturn(
                StockStoryEntity.builder()
                        .storyId(storyId1)
                        .story(story1)
                        .build()
        );
        String story2 = "IT market is way too much hyped";
        when(stockPositionStoryRepository.findByStoryId(storyId2)).thenReturn(
                StockStoryEntity.builder()
                        .storyId(storyId2)
                        .story(story2)
                        .build()
        );

        // when
        List<StockStoryDto> stockStoryDtos = stockStoryService.readStockShortPositionStoryOfCertainStock(stockCode);

        // then
        // latest one comes first
        assertThat(stockStoryDtos.size()).isEqualTo(2);
        StockStoryDto stockStoryDtoOfStoryId2 = stockStoryDtos.get(0);
        assertThat(stockStoryDtoOfStoryId2.getStockCode()).isEqualTo(stockCode);
        assertThat(stockStoryDtoOfStoryId2.getStockPrices().length).isEqualTo(2);
        assertThat(stockStoryDtoOfStoryId2.getStockPrices()).contains(78000);
        assertThat(stockStoryDtoOfStoryId2.getStockPrices()).contains(81000);
        assertThat(stockStoryDtoOfStoryId2.getAveragePurchasePrice()).isEqualTo(65000);
        assertThat(stockStoryDtoOfStoryId2.getStory()).isEqualTo(story2);
        assertThat(stockStoryDtoOfStoryId2.getDt()).isEqualTo(stockSellDate2);
        
        StockStoryDto stockStoryDtoOfStoryId1 = stockStoryDtos.get(1);
        assertThat(stockStoryDtoOfStoryId1.getStockCode()).isEqualTo(stockCode);
        assertThat(stockStoryDtoOfStoryId1.getStockPrices().length).isEqualTo(1);
        assertThat(stockStoryDtoOfStoryId1.getStockPrices()[0]).isEqualTo(60000);
        assertThat(stockStoryDtoOfStoryId1.getAveragePurchasePrice()).isEqualTo(50000);
        assertThat(stockStoryDtoOfStoryId1.getStory()).isEqualTo(story1);
        assertThat(stockStoryDtoOfStoryId1.getDt()).isEqualTo(stockSellDate1);
    }

    @Test
    public void readStockStoryOfCertainStock(){
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
                StockStoryEntity.builder()
                        .storyId(storyId1)
                        .story(story1)
                        .build()
        );
        String story2 = "Naver has great potential";
        when(stockPositionStoryRepository.findByStoryId(storyId2)).thenReturn(
                StockStoryEntity.builder()
                        .storyId(storyId2)
                        .story(story2)
                        .build()
        );

        String stockCode2 = "035420";
        long storyId3 = 3;
        LocalDate stockSellDate1 = LocalDate.of(2023, 10, 21);
        List<StockSellRecordEntity> stockSellRecordEntities = new ArrayList<>();
        stockSellRecordEntities.add(StockSellRecordEntity
                .builder()
                .stockCode(stockCode2)
                .sellPrice(60000)
                .avgPurchasePrice(50000)
                .storyId(storyId3)
                .sellDt(stockSellDate1)
                .build()
        );
        long storyId4 = 4;
        LocalDate stockSellDate2 = LocalDate.of(2023, 10, 22);
        stockSellRecordEntities.add(StockSellRecordEntity
                .builder()
                .stockCode(stockCode2)
                .sellPrice(78000)
                .avgPurchasePrice(60000)
                .storyId(storyId4)
                .sellDt(stockSellDate2)
                .build()
        );
        stockSellRecordEntities.add(StockSellRecordEntity
                .builder()
                .stockCode(stockCode2)
                .sellPrice(81000)
                .avgPurchasePrice(70000)
                .storyId(storyId4)
                .sellDt(stockSellDate2)
                .build()
        );
        when(stockSellRecordRepository.findAllByStockCode(stockCode)).thenReturn(stockSellRecordEntities);
        String story3 = "IT market is too much hyped";
        when(stockPositionStoryRepository.findByStoryId(storyId3)).thenReturn(
                StockStoryEntity.builder()
                        .storyId(storyId3)
                        .story(story3)
                        .build()
        );
        String story4 = "IT market is way too much hyped";
        when(stockPositionStoryRepository.findByStoryId(storyId4)).thenReturn(
                StockStoryEntity.builder()
                        .storyId(storyId4)
                        .story(story4)
                        .build()
        );

        // when
        List<StockStoryDto> stockStoryDtos = stockStoryService.readStockStoryOfCertainStock(stockCode);

        // then
        assertThat(stockStoryDtos.size()).isEqualTo(4);
    }

    @Test
    public void createStockLongPositionStory(){
        // given
        String stockCode = "373220";
        int[] stockPrices = new int[] { 402000, 401000, 404000 };
        LocalDate date = LocalDate.of(2023, 11, 3);
        String story = "Battery industry will grow and LG energy solution is a leading company.";

        StockStoryDto stockStoryDto = StockStoryDto
                .builder()
                .stockCode(stockCode)
                .stockPrices(stockPrices)
                .dt(date)
                .story(story)
                .build();

        //when
        CreateStatusDto createStatusDto = stockStoryService.createLongPositionStory(stockStoryDto);

        //then
        assertThat(createStatusDto.getStatus()).isEqualTo(CreateStatusDto.Status.SUCCESS);
    }

    @Test
    public void createStockShortPositionStory(){
        // given
        String stockCode = "373220";
        long storyId1 = 1;
        LocalDate stockPurchaseDate = LocalDate.of(2023, 10, 10);
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = new ArrayList<>();
        stockPurchaseRecordEntities.add(StockPurchaseRecordEntity
                .builder()
                .stockCode(stockCode)
                .purchasePrice(402000)
                .storyId(storyId1)
                .purchaseDt(stockPurchaseDate)
                .build()
        );
        stockPurchaseRecordEntities.add(StockPurchaseRecordEntity
                .builder()
                .stockCode(stockCode)
                .purchasePrice(401000)
                .storyId(storyId1)
                .purchaseDt(stockPurchaseDate)
                .build()
        );
        long storyId2 = 2;
        LocalDate stockPurchaseDate2 = LocalDate.of(2023, 10, 23);
        stockPurchaseRecordEntities.add(StockPurchaseRecordEntity
                .builder()
                .stockCode(stockCode)
                .purchasePrice(404000)
                .storyId(storyId2)
                .purchaseDt(stockPurchaseDate2)
                .build()
        );
        when(stockPurchaseRecordRepository.findAllByStockCode(stockCode)).thenReturn(stockPurchaseRecordEntities);

        int[] shortPositionStockPrices = new int[] { 501000, 500000, 510000 };
        LocalDate shortPositionDate = LocalDate.of(2023, 11, 3);
        String shortPositionStory = "I would like to invest in semi conductor industry more";
        StockStoryDto stockShortPositionStoryDto = StockStoryDto
                .builder()
                .isLong(false)
                .stockCode(stockCode)
                .stockPrices(shortPositionStockPrices)
                .dt(shortPositionDate)
                .story(shortPositionStory)
                .build();

        //when
        CreateStatusDto createStatusDto = stockStoryService.createShortPositionStory(stockShortPositionStoryDto);

        //then
        assertThat(createStatusDto.getStatus()).isEqualTo(CreateStatusDto.Status.SUCCESS);
    }
}
