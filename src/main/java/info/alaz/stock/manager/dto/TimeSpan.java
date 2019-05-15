package info.alaz.stock.manager.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Time Span", description = "Time Span Enum")
public enum TimeSpan {
    @ApiModelProperty(notes = "From midnight till now", example = "TODAY")
    TODAY,
    @ApiModelProperty(notes = "Same day last month", example = "LAST_MONTH")
    LAST_MONTH
}
