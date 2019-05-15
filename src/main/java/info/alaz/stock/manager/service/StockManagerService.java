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
            StockUpdateTimestampCannotBeInFutureException, StockHasBeenUpdatedBeforeException, OptimisticLockException;

    ProductResponseDto getStockOfProduct(String productId) throws ProductNotFoundException;

    StatisticsResponseDto getStatistics(TimeSpan timeSpan);
}
