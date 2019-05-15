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
import java.util.Set;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "StatisticsResponseDto", description = "Statistics Response DTO")
public class StatisticsResponseDto {

    @ApiModelProperty(notes = "Datetime in UTC when requested the stock", example = "2017-07-16T22:54:01.754Z", position = 1)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime requestTimestamp;

    @NotNull
    @ApiModelProperty(notes = "Time span which statistic will be based upon, valid values 'TODAY', 'LAST_MONTH'", example = "TODAY", position = 2)
    private TimeSpan timeSpan;

    @ApiModelProperty(notes = " Top three products with the highest availability", example = "TODO", position = 3)
    private Set<ProductStockDto> topAvailableProducts;

    @ApiModelProperty(notes = "top selling products in given time span", example = "TODO", position = 4)
    private Set<TopSellingProductResponseDto> topSellingProducts;
}
