package info.alaz.stock.manager.mapper;


import info.alaz.stock.manager.dto.ProductStockDto;
import info.alaz.stock.manager.entity.ProductEntity;
import info.alaz.stock.manager.entity.StockEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockMapper extends GenericEntityDtoMapper<ProductStockDto, StockEntity> {

    @Mapping(source = "productStockDto.id", target = "id")
    @Mapping(source = "productStockDto.timestamp", target = "dateUpdated")
    @Mapping(source = "productEntity", target = "product")
    StockEntity toEntity(ProductStockDto productStockDto, ProductEntity productEntity);
}
