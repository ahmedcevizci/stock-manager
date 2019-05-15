package info.alaz.stock.manager.web.restful;

import info.alaz.stock.manager.constant.APIErrorMessages;
import info.alaz.stock.manager.constant.APIOperationMessages;
import info.alaz.stock.manager.constant.APITags;
import info.alaz.stock.manager.dto.ProductResponseDto;
import info.alaz.stock.manager.dto.StatisticsResponseDto;
import info.alaz.stock.manager.dto.TimeSpan;
import info.alaz.stock.manager.dto.restful.StockUpdateRequestDto;
import info.alaz.stock.manager.service.StockManagerService;
import info.alaz.stock.manager.web.ParentStockManagerController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.UUID;

import static info.alaz.stock.manager.constant.APIOperationMessages.*;
import static info.alaz.stock.manager.web.restful.StockManagerRestfulController.BASE_RESTFUL_API_PATH;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Api(tags = {APITags.RESTFUL_STOCK_MANAGER_TAG}, hidden = true)
@RestController
@RequestMapping(value = BASE_RESTFUL_API_PATH, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = ParentStockManagerController.STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE)
public class StockManagerRestfulController extends ParentStockManagerController {

    public static final String BASE_RESTFUL_API_PATH = "/api/stock-manager/restful";

    private Logger logger = LoggerFactory.getLogger(StockManagerRestfulController.class);

    @Autowired
    public StockManagerRestfulController(StockManagerService stockManagerService) {
        super(stockManagerService);
    }

    @ApiOperation(value = API_OPS_UPDATE_STOCK_OF_PRODUCT)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = APIOperationMessages.SUCCESS_STOCK_UPDATED),
            @ApiResponse(code = 400, message = APIErrorMessages.STOCK_UPDATE_TIME_CANNOT_BE_IN_FUTURE),
            @ApiResponse(code = 404, message = APIErrorMessages.STOCK_NOT_FOUND + "\n" + APIErrorMessages.PRODUCT_NOT_FOUND + "\n" +
                    APIErrorMessages.STOCK_NOT_BELONGS_TO_PRODUCT),
            @ApiResponse(code = 409, message = APIErrorMessages.STOCK_UPDATED_IN_THE_MEAN_TIME),
    })
    @ResponseStatus(NO_CONTENT)
    @PutMapping(path = "/products/{" + PARAM_PRODUCT_ID + "}/stocks/{" + PARAM_STOCK_ID + "}")
    public void updateStock(@PathVariable(PARAM_PRODUCT_ID) String productId, @PathVariable(PARAM_STOCK_ID) UUID stockId,
                            @Valid @RequestBody StockUpdateRequestDto stockUpdateRequestDto) throws Exception {

        logger.info(String.format("Updating stockId: %s for product: %s", stockId.toString(), productId));

        this.stockManagerService.updateStock(stockId, productId, stockUpdateRequestDto.getQuantity(), stockUpdateRequestDto.getTimestamp());

        logger.info(String.format("Updated stockId: %s for product: %s", stockId.toString(), productId));
    }

    @ApiOperation(value = API_OP_GET_STOCK_INFORMATION_OF_PRODUCT)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = APIOperationMessages.SUCCESS_CALCULATING_STATISTICS),
            @ApiResponse(code = 404, message = APIErrorMessages.PRODUCT_NOT_FOUND)
    })
    @ResponseStatus(OK)
    @GetMapping(path = "/products/{" + PARAM_PRODUCT_ID + "}")
    public ProductResponseDto getStockOfProduct(@PathVariable(value = PARAM_PRODUCT_ID) String productId) throws Exception {

        logger.info(String.format("Fetching stock of product: %s", productId));

        ZonedDateTime requestTimestamp = ZonedDateTime.now();
        ProductResponseDto productResponseDto = this.stockManagerService.getStockOfProduct(productId);
        productResponseDto.setRequestTimestamp(requestTimestamp);

        logger.info(String.format("Fetched stock of product: %s", productId));

        return productResponseDto;
    }

    //GET /statistics?time=[today,lastMonth]
    @ApiOperation(value = API_OP_GET_STATISTICS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = APIOperationMessages.SUCCESS_CALCULATING_STATISTICS),
            @ApiResponse(code = 400, message = APIErrorMessages.UNDEFINED_TIME_SPAN)})
    @ResponseStatus(OK)
    @GetMapping(path = "/statistics")
    public StatisticsResponseDto getStatistics(@RequestParam(value = PARAM_TIME) TimeSpan timeSpan) throws Exception {

        logger.info(String.format("Fetching statistics of %s", timeSpan.name()));

        StatisticsResponseDto statisticsResponseDto = this.stockManagerService.getStatistics(timeSpan);
        statisticsResponseDto.setRequestTimestamp(ZonedDateTime.now());

        logger.info(String.format("Fetched statistics of %s", timeSpan.name()));
        return statisticsResponseDto;
    }

}
