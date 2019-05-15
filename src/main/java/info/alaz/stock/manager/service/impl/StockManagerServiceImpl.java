package info.alaz.stock.manager.service.impl;

import info.alaz.stock.manager.config.StockManagerProperties;
import info.alaz.stock.manager.dto.*;
import info.alaz.stock.manager.entity.ProductEntity;
import info.alaz.stock.manager.entity.StockEntity;
import info.alaz.stock.manager.entity.StockEventEntity;
import info.alaz.stock.manager.entity.TopSellingProductEntity;
import info.alaz.stock.manager.exception.*;
import info.alaz.stock.manager.mapper.ProductMapper;
import info.alaz.stock.manager.mapper.StockMapper;
import info.alaz.stock.manager.repository.ProductRepository;
import info.alaz.stock.manager.repository.StockEventRepository;
import info.alaz.stock.manager.repository.StockRepository;
import info.alaz.stock.manager.repository.TopSellingEntityRepository;
import info.alaz.stock.manager.service.StockManagerService;
import info.alaz.stock.manager.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StockManagerServiceImpl implements StockManagerService {

    private Logger logger = LoggerFactory.getLogger(StockManagerServiceImpl.class);

    private ProductRepository productRepository;
    private StockRepository stockRepository;
    private StockEventRepository stockEventRepository;
    private StockMapper stockMapper;
    private ProductMapper productMapper;
    private StockManagerProperties stockManagerProperties;
    private TopSellingEntityRepository topSellingEntityRepository;

    @Autowired
    public StockManagerServiceImpl(ProductRepository productRepository, StockRepository stockRepository,
                                   StockEventRepository stockEventRepository, StockMapper stockMapper,
                                   ProductMapper productMapper, StockManagerProperties stockManagerProperties,
                                   TopSellingEntityRepository topSellingEntityRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.stockEventRepository = stockEventRepository;
        this.stockMapper = stockMapper;
        this.productMapper = productMapper;
        this.stockManagerProperties = stockManagerProperties;
        this.topSellingEntityRepository = topSellingEntityRepository;
    }

    @Override
    @Transactional
    public void updateStock(UUID stockUuid, String productId, Integer newQuantity, ZonedDateTime updateTimestamp)
            throws StockNotFoundException, ProductNotFoundException, StockNotBelongToProductException,
            StockUpdateTimestampCannotBeInFutureException, StockHasBeenUpdatedBeforeException, OptimisticLockException {
        logger.debug("Updating stock.");

        validateNewStock(stockUuid, productId, updateTimestamp);

        StockEntity stockToBeUpdated = this.stockRepository.findByIdAndProduct_Name(stockUuid, productId);
        int oldQuantity = stockToBeUpdated.getQuantity();
        stockToBeUpdated.setQuantity(newQuantity);
        StockEntity updatedStockEntity = this.stockRepository.save(stockToBeUpdated);

        int changeInQuantity = newQuantity - oldQuantity; //delta
        this.stockEventRepository.save(new StockEventEntity(updatedStockEntity, changeInQuantity, ZonedDateTime.now()));

        logger.debug("Updated stock.");
    }

    private void validateNewStock(UUID stockUuid, String productId, ZonedDateTime updateTimestamp) throws StockHasBeenUpdatedBeforeException {
        logger.debug("Validating new stock.");
        if (updateTimestamp.isAfter(ZonedDateTime.now())) {
            throw new StockUpdateTimestampCannotBeInFutureException(updateTimestamp);
        }
        ProductEntity productEntity = this.productRepository.findOne(productId);
        if (productEntity == null) {
            throw new ProductNotFoundException(productId);
        }
        StockEntity stockEntity = this.stockRepository.findOne(stockUuid);
        if (stockEntity == null) {
            throw new StockNotFoundException(stockUuid);
        }
        if (!stockEntity.getProduct().getName().equals(productEntity.getName())) {
            throw new StockNotBelongToProductException(stockUuid, productId);
        }
        if (stockEntity.getDateUpdated().isAfter(updateTimestamp)) {
            throw new StockHasBeenUpdatedBeforeException(stockUuid);
        }
        logger.debug("Validated new stock.");
    }

    @Override
    public ProductResponseDto getStockOfProduct(String productId) throws ProductNotFoundException {
        logger.debug("Fetching stock of product.");

        ProductEntity productEntity = this.productRepository.findOne(productId);
        if (productEntity == null) {
            throw new ProductNotFoundException(productId);
        }

        StockEntity stockEntity = productEntity.getStockEntitySet().iterator().next();
        ProductResponseDto productResponseDto = this.productMapper.toDto(productEntity, stockEntity);

        logger.debug("Fetched stock of product.");
        return productResponseDto;
    }

    @Override
    public StatisticsResponseDto getStatistics(TimeSpan timeSpan) {
        logger.debug("Fetching statistics.");

        ZonedDateTime timeSpanAgo = DateUtil.getStartDateOfTimeSpanFromNow(timeSpan);

        List<StockEntity> topAvailableStockEntityList = this.stockRepository.findAll(new PageRequest(0, stockManagerProperties.getTopAvailableProductCountToShow(), Sort.Direction.DESC, "quantity")).getContent();
        Set<TopSellingProductEntity> topSellingProductEntitySet = this.topSellingEntityRepository.findTopSellingProducts(timeSpanAgo, stockManagerProperties.getTopSellingProductCountToShow());

        StatisticsResponseDto statisticsResponseDto = this.mapToStatisticsResponseDto(timeSpan, topAvailableStockEntityList, topSellingProductEntitySet);

        logger.debug("Fetched statistics.");
        return statisticsResponseDto;
    }

    private StatisticsResponseDto mapToStatisticsResponseDto(TimeSpan timeSpan, List<StockEntity> topAvailableStockEntityList, Set<TopSellingProductEntity> topSellingProductEntitySet) {
        Set<ProductStockDto> topAvailableProductsSet = topAvailableStockEntityList.stream().map(e -> new ProductStockDto(e.getId(), e.getDateUpdated(), e.getProduct().getName(), e.getQuantity())).collect(Collectors.toSet());
        Set<TopSellingProductResponseDto> topSellingProductsSet = topSellingProductEntitySet.stream().map(e -> new TopSellingProductResponseDto(e.getProductName(), e.getItemsSold())).collect(Collectors.toSet());
        return new StatisticsResponseDto(null, timeSpan, topAvailableProductsSet, topSellingProductsSet);
    }
}
