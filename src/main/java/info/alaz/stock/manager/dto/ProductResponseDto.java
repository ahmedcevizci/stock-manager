package info.alaz.stock.manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ProductStockDto", description = "Stock Request DTO")
public class ProductResponseDto {

    @NotNull
    @ApiModelProperty(notes = "id of the requested product", example = "vegetable-123", position = 1)
    private String productId;

    @ApiModelProperty(notes = "datetime in UTC when requested the stock", example = "2017-07-16T22:54:01.754Z", position = 2)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime requestTimestamp;

    @ApiModelProperty(notes = "Unique id to identify the products", example = "vegetable-123", position = 3)
    @NotNull
    private StockResponseDto stock;
}
