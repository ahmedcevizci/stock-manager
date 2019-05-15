package info.alaz.stock.manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ProductStockDto", description = "Product Stock Request DTO")
public class ProductStockDto {

    @NotNull
    @ApiModelProperty(notes = "Stock identifier", example = "a6b10bde-f0c7-439d-9d10-dca9e130f84e", position = 1)
    private UUID id;

    @ApiModelProperty(notes = "Datetime in UTC", example = "2017-07-16T22:54:01.754Z", position = 2)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime timestamp;

    @ApiModelProperty(notes = "Unique id to identify the products", example = "vegetable-123", position = 3)
    @NotNull
    private String productId;

    @ApiModelProperty(notes = "Amount of available stock", example = "500", position = 4)
    @NotNull
    @Min(0)
    private Integer quantity;
}
