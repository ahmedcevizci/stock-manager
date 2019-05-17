package info.alaz.stock.manager.mapper;

import info.alaz.stock.manager.dto.ProductStockDto;
import info.alaz.stock.manager.dto.StatisticsResponseDto;
import info.alaz.stock.manager.dto.TimeSpan;
import info.alaz.stock.manager.dto.TopSellingProductResponseDto;
import info.alaz.stock.manager.entity.StockEntity;
import info.alaz.stock.manager.entity.TopSellingProductEntity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StatisticsMapper {

    public static StatisticsResponseDto mapToStatisticsResponseDto(TimeSpan timeSpan, ZonedDateTime requestTimestamp, List<StockEntity> topAvailableStockEntityList, Set<TopSellingProductEntity> topSellingProductEntitySet) {

        Set<ProductStockDto> topAvailableProductsSet = topAvailableStockEntityList.stream().map(e -> new ProductStockDto(e.getId(), e.getDateUpdated(), e.getProduct().getName(), e.getQuantity())).collect(Collectors.toSet());

        Set<TopSellingProductResponseDto> topSellingProductsSet = topSellingProductEntitySet.stream().map(e -> new TopSellingProductResponseDto(e.getProductName(), e.getItemsSold())).collect(Collectors.toSet());

        return new StatisticsResponseDto(requestTimestamp, timeSpan, topAvailableProductsSet, topSellingProductsSet);
    }
}
