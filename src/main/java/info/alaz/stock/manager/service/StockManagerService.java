package info.alaz.stock.manager.service;

import info.alaz.stock.manager.dto.ProductResponseDto;
import info.alaz.stock.manager.dto.StatisticsResponseDto;
import info.alaz.stock.manager.dto.TimeSpan;
import info.alaz.stock.manager.exception.*;

import javax.persistence.OptimisticLockException;
import java.time.ZonedDateTime;
import java.util.UUID;

public interface StockManagerService {

    void updateStock(UUID stockUuid, String productId, Integer newQuantity, ZonedDateTime updateTimestamp)
            throws StockNotFoundException, ProductNotFoundException, StockNotBelongToProductException,
            StockUpdateTimestampCannotBeInFutureException, InvalidQuantityException,
            StockHasBeenUpdatedBeforeException, OptimisticLockException;

    ProductResponseDto getStockOfProduct(String productId, ZonedDateTime requestTimestamp) throws ProductNotFoundException;

    StatisticsResponseDto calculateStatistics(TimeSpan timeSpan, ZonedDateTime requestTimestamp);
}
