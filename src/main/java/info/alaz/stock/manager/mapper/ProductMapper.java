package info.alaz.stock.manager.mapper;


import info.alaz.stock.manager.dto.ProductResponseDto;
import info.alaz.stock.manager.entity.ProductEntity;
import info.alaz.stock.manager.entity.StockEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper extends GenericEntityDtoMapper<ProductResponseDto, ProductEntity> {

    @Mapping(source = "stockEntity.id", target = "stock.id")
    @Mapping(source = "stockEntity.quantity", target = "stock.quantity")//timestamp
    @Mapping(source = "stockEntity.dateUpdated", target = "stock.timestamp")//
    @Mapping(source = "productEntity.name", target = "productId")
    @Mapping(source = "requestTimestamp", target = "requestTimestamp")
    ProductResponseDto toDto(ProductEntity productEntity, StockEntity stockEntity, ZonedDateTime requestTimestamp);
}
