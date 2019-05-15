package info.alaz.stock.manager.dto.restful;

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
@ApiModel(value = "StockUpdateRequestDto", description = "Stock Update Request DTO")
public class StockUpdateRequestDto {

    @ApiModelProperty(notes = "Datetime in UTC", example = "2017-07-16T22:54:01.754Z", position = 1)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime timestamp;

    @ApiModelProperty(notes = "Amount of available stock", example = "500", position = 2)
    @NotNull
    @Min(0)
    private Integer quantity;
}
