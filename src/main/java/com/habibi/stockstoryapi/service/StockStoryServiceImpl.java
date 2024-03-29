package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockStoryEntity;
import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.dto.CreateStatusDto;
import com.habibi.stockstoryapi.dto.OwnStockDto;
import com.habibi.stockstoryapi.dto.StockStoryDto;
import com.habibi.stockstoryapi.repository.StockPositionStoryRepository;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StockStoryServiceImpl implements StockStoryService {
    private StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private StockSellRecordRepository stockSellRecordRepository;
    private StockPositionStoryRepository stockPositionStoryRepository;
    private StockCodeToNameMapper stockCodeToNameMapper;
    private OwnStockService ownStockService;
    public StockStoryServiceImpl(
            StockPurchaseRecordRepository stockPurchaseRecordRepository,
            StockSellRecordRepository stockSellRecordRepository,
            StockPositionStoryRepository stockPositionStoryRepository,
            StockCodeToNameMapper stockCodeToNameMapper,
            OwnStockService ownStockService
    ){
        this.stockPurchaseRecordRepository = stockPurchaseRecordRepository;
        this.stockSellRecordRepository = stockSellRecordRepository;
        this.stockPositionStoryRepository = stockPositionStoryRepository;
        this.stockCodeToNameMapper = stockCodeToNameMapper;
        this.ownStockService = ownStockService;
    }

    @Override
    public CreateStatusDto createStockStory(int userId, StockStoryDto stockStoryDto) {
        if(stockStoryDto.isLong()){
            return createLongPositionStory(userId, stockStoryDto);
        } else {
            return createShortPositionStory(userId, stockStoryDto);
        }
    }

    @Override
    @Transactional
    public CreateStatusDto createLongPositionStory(int userId, StockStoryDto stockStoryDto) {
        String stockCode = stockStoryDto.getStockCode();
        int[] stockPrices = stockStoryDto.getStockPrices();
        LocalDate date = stockStoryDto.getDt();
        String story = stockStoryDto.getStory();
        StockStoryEntity stockStoryEntity = StockStoryEntity
                .builder()
                .userId(userId)
                .positionType("Long")
                .story(story)
                .build();
        stockPositionStoryRepository.save(stockStoryEntity);
        Long storyId = stockStoryEntity.getStoryId();
        for(int price : stockPrices){
            StockPurchaseRecordEntity stockPurchaseRecordEntity = StockPurchaseRecordEntity
                    .builder()
                    .userId(userId)
                    .storyId(storyId)
                    .stockCode(stockCode)
                    .purchaseDt(date)
                    .purchasePrice(price)
                    .build();
            stockPurchaseRecordRepository.save(stockPurchaseRecordEntity);
        }
        return CreateStatusDto.builder()
                .status(CreateStatusDto.Status.SUCCESS)
                .id(Long.toString(storyId))
                .build();
    }

    @Override
    @Transactional
    public CreateStatusDto createShortPositionStory(int userId, StockStoryDto stockStoryDto) {
        String stockCode = stockStoryDto.getStockCode();
        int[] stockPrices = stockStoryDto.getStockPrices();
        LocalDate date = stockStoryDto.getDt();
        String story = stockStoryDto.getStory();
        StockStoryEntity stockStoryEntity = StockStoryEntity
                .builder()
                .userId(userId)
                .positionType("Short")
                .story(story)
                .build();
        stockPositionStoryRepository.save(stockStoryEntity);
        Long storyId = stockStoryEntity.getStoryId();
        List<OwnStockDto> ownStocks = ownStockService.readOwnStocks(userId);
        OwnStockDto ownStock = ownStocks.stream().filter(stock -> stock.getStockCode().equals(stockCode)).findFirst().orElseThrow();
        for(int price : stockPrices){
            StockSellRecordEntity stockSellRecordEntity = StockSellRecordEntity
                    .builder()
                    .userId(userId)
                    .storyId(storyId)
                    .stockCode(stockCode)
                    .sellDt(date)
                    .avgPurchasePrice(ownStock.getAveragePurchasePrice())
                    .sellPrice(price)
                    .build();
            stockSellRecordRepository.save(stockSellRecordEntity);
        }
        return CreateStatusDto.builder()
                .status(CreateStatusDto.Status.SUCCESS)
                .id(Long.toString(storyId))
                .build();
    }

    @Override
    public StockStoryDto readStockStoryById(long id) {
        StockStoryEntity stockStoryEntity = stockPositionStoryRepository.findByStoryId(id);
        if(stockStoryEntity.getPositionType().equals("Long")){
            List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = stockPurchaseRecordRepository.findAllByStoryId(id);
            return StockStoryDto.builder()
                    .storyId(id)
                    .stockName(stockCodeToNameMapper.getStockName(stockPurchaseRecordEntities.get(0).getStockCode()))
                    .stockCode(stockPurchaseRecordEntities.get(0).getStockCode())
                    .stockPrices(stockPurchaseRecordEntities.stream().mapToInt(StockPurchaseRecordEntity::getPurchasePrice).toArray())
                    .isLong(true)
                    .dt(stockPurchaseRecordEntities.get(0).getPurchaseDt())
                    .story(stockStoryEntity.getStory())
                    .build();
        } else {
            List<StockSellRecordEntity> stockSellRecordEntities = stockSellRecordRepository.findAllByStoryId(id);
            return StockStoryDto.builder()
                    .storyId(id)
                    .stockName(stockCodeToNameMapper.getStockName(stockSellRecordEntities.get(0).getStockCode()))
                    .stockCode(stockSellRecordEntities.get(0).getStockCode())
                    .stockPrices(stockSellRecordEntities.stream().mapToInt(StockSellRecordEntity::getSellPrice).toArray())
                    .averagePurchasePrice(
                                stockSellRecordEntities.stream().mapToInt(StockSellRecordEntity::getAvgPurchasePrice).sum()
                                        /
                                stockSellRecordEntities.size()
                            )
                    .isLong(false)
                    .dt(stockSellRecordEntities.get(0).getSellDt())
                    .story(stockStoryEntity.getStory())
                    .build();
        }
    }


    @Override
    public List<StockStoryDto> readStockLongPositionStoryOfCertainStock(int userId, String stockCode) {
        String stockName = stockCodeToNameMapper.getStockName(stockCode);
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = stockPurchaseRecordRepository.findAllByUserIdAndStockCode(userId, stockCode);
        Map<Long, List<StockPurchaseRecordEntity>> stockPurchaseRecordEntitiesByStoryId = new HashMap<>();
        for(StockPurchaseRecordEntity stockPurchaseRecordEntity : stockPurchaseRecordEntities){
            long storyId = stockPurchaseRecordEntity.getStoryId();
            if(stockPurchaseRecordEntitiesByStoryId.containsKey(storyId)){
                List<StockPurchaseRecordEntity> stockPurchaseRecordEntitiesWithCertainStoryId = stockPurchaseRecordEntitiesByStoryId.get(storyId);
                stockPurchaseRecordEntitiesWithCertainStoryId.add(stockPurchaseRecordEntity);
            } else {
                List<StockPurchaseRecordEntity> stockPurchaseRecordEntitiesWithCertainStoryId = new ArrayList<>();
                stockPurchaseRecordEntitiesWithCertainStoryId.add(stockPurchaseRecordEntity);
                stockPurchaseRecordEntitiesByStoryId.put(storyId, stockPurchaseRecordEntitiesWithCertainStoryId);
            }
        }
        List<StockStoryDto> stockStoryDtos = new ArrayList<>();
        for(long storyId: stockPurchaseRecordEntitiesByStoryId.keySet()){
            List<StockPurchaseRecordEntity> stockPurchaseRecordEntitiesWithCertainStoryId = stockPurchaseRecordEntitiesByStoryId.get(storyId);
            StockStoryEntity stockStoryEntity = stockPositionStoryRepository.findByStoryId(storyId);
            stockStoryDtos.add(
                    StockStoryDto
                            .builder()
                            .storyId(storyId)
                            .stockName(stockName)
                            .isLong(true)
                            .dt(stockPurchaseRecordEntitiesWithCertainStoryId.get(0).getPurchaseDt())
                            .stockCode(stockPurchaseRecordEntitiesWithCertainStoryId.get(0).getStockCode())
                            .stockPrices(
                                    stockPurchaseRecordEntitiesWithCertainStoryId
                                            .stream()
                                            .mapToInt(entity -> entity.getPurchasePrice())
                                            .toArray()
                            )
                            .story(stockStoryEntity.getStory())
                            .build()
            );
        }
        return stockStoryDtos.stream().sorted(Comparator.comparing(StockStoryDto::getStoryId, Comparator.reverseOrder())).collect(Collectors.toList());
    }

    @Override
    public List<StockStoryDto> readStockShortPositionStoryOfCertainStock(int userId, String stockCode) {
        String stockName = stockCodeToNameMapper.getStockName(stockCode);
        List<StockSellRecordEntity> stockSellRecordEntities = stockSellRecordRepository.findAllByUserIdAndStockCode(userId, stockCode);
        Map<Long, List<StockSellRecordEntity>> stockSellRecordEntitiesByStoryId = new HashMap<>();
        for(StockSellRecordEntity stockSellRecordEntity : stockSellRecordEntities){
            long storyId = stockSellRecordEntity.getStoryId();
            if(stockSellRecordEntitiesByStoryId.containsKey(storyId)){
                List<StockSellRecordEntity> stockSellRecordEntitiesWithCertainStoryId = stockSellRecordEntitiesByStoryId.get(storyId);
                stockSellRecordEntitiesWithCertainStoryId.add(stockSellRecordEntity);
            } else {
                List<StockSellRecordEntity> stockSellRecordEntitiesWithCertainStoryId = new ArrayList<>();
                stockSellRecordEntitiesWithCertainStoryId.add(stockSellRecordEntity);
                stockSellRecordEntitiesByStoryId.put(storyId, stockSellRecordEntitiesWithCertainStoryId);
            }
        }
        List<StockStoryDto> stockStoryDtos = new ArrayList<>();
        for(long storyId: stockSellRecordEntitiesByStoryId.keySet()){
            List<StockSellRecordEntity> stockSellRecordEntitiesWithCertainStoryId = stockSellRecordEntitiesByStoryId.get(storyId);
            StockStoryEntity stockStoryEntity = stockPositionStoryRepository.findByStoryId(storyId);
            stockStoryDtos.add(
                    StockStoryDto
                            .builder()
                            .storyId(storyId)
                            .stockName(stockName)
                            .isLong(false)
                            .dt(stockSellRecordEntitiesWithCertainStoryId.get(0).getSellDt())
                            .stockCode(stockSellRecordEntitiesWithCertainStoryId.get(0).getStockCode())
                            .stockPrices(
                                    stockSellRecordEntitiesWithCertainStoryId
                                            .stream()
                                            .mapToInt(entity -> entity.getSellPrice())
                                            .toArray()
                            )
                            .averagePurchasePrice(
                                    (
                                            stockSellRecordEntitiesWithCertainStoryId
                                            .stream()
                                            .mapToInt(entity -> entity.getAvgPurchasePrice())
                                            .sum()
                                                    /
                                            stockSellRecordEntitiesWithCertainStoryId.size()
                                    )
                            )
                            .story(stockStoryEntity.getStory())
                            .build()
            );
        }
        return stockStoryDtos.stream().sorted(Comparator.comparing(StockStoryDto::getStoryId, Comparator.reverseOrder())).collect(Collectors.toList());
    }

    @Override
    public List<StockStoryDto> readStockStoryOfCertainStock(int userId, String stockCode){
        List<StockStoryDto> stories = Stream.concat(
                readStockShortPositionStoryOfCertainStock(userId, stockCode).stream(),
                readStockLongPositionStoryOfCertainStock(userId, stockCode).stream()
        ).collect(Collectors.toList());
        stories.sort(Comparator.comparing(StockStoryDto::getStoryId, Comparator.reverseOrder()));
        return stories;
    }
}
