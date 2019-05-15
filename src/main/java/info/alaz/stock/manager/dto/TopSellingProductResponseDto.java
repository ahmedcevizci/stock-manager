package info.alaz.stock.manager.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TopSellingProductResponseDto", description = "Top Selling Product Response DTO")
public class TopSellingProductResponseDto {

    @ApiModelProperty(notes = "Product (name) identifier", example = "vegetable-123", position = 1)
    @NotNull
    private String productId;

    @ApiModelProperty(notes = "Number of products sold within given time span", example = "500", position = 2)
    @NotNull
    @Min(0)
    private Integer itemsSold;

}
