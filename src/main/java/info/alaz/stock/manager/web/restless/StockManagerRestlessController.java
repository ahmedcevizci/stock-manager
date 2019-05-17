package info.alaz.stock.manager.web.restless;

import info.alaz.stock.manager.constant.APIErrorMessages;
import info.alaz.stock.manager.constant.APIOperationMessages;
import info.alaz.stock.manager.constant.APITags;
import info.alaz.stock.manager.dto.ProductResponseDto;
import info.alaz.stock.manager.dto.ProductStockDto;
import info.alaz.stock.manager.dto.StatisticsResponseDto;
import info.alaz.stock.manager.dto.TimeSpan;
import info.alaz.stock.manager.exception.StockHasBeenUpdatedBeforeException;
import info.alaz.stock.manager.service.StockManagerService;
import info.alaz.stock.manager.web.ParentStockManagerController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.time.ZonedDateTime;

import static info.alaz.stock.manager.constant.APIOperationMessages.*;

import static info.alaz.stock.manager.web.ParentStockManagerController.BASE_API_PATH;
import static org.springframework.http.HttpStatus.OK;

@Api(tags = {APITags.RESTLESS_STOCK_MANAGER_TAG})
@RestController
@RequestMapping(value = BASE_API_PATH, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = ParentStockManagerController.STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE)
public class StockManagerRestlessController extends ParentStockManagerController {

    private Logger logger = LoggerFactory.getLogger(StockManagerRestlessController.class);

    @Autowired
    public StockManagerRestlessController(StockManagerService stockManagerService) {
        super(stockManagerService);
    }

    @ApiOperation(value = API_OPS_UPDATE_STOCK_OF_PRODUCT)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = APIOperationMessages.SUCCESS_STOCK_UPDATED),
            @ApiResponse(code = 204, message = APIErrorMessages.STOCK_UPDATED_IN_THE_MEAN_TIME),
            @ApiResponse(code = 400, message = APIErrorMessages.STOCK_UPDATE_TIME_CANNOT_BE_IN_FUTURE),
            @ApiResponse(code = 404, message = APIErrorMessages.STOCK_NOT_FOUND + "\n" + APIErrorMessages.PRODUCT_NOT_FOUND + "\n" +
                    APIErrorMessages.STOCK_NOT_BELONGS_TO_PRODUCT)
    })
    @PostMapping(path = "/restless/updateStock")
    public ResponseEntity<Void> updateStock(@Valid @RequestBody ProductStockDto productStockDto) throws Exception {

        logger.info(String.format("Updating stockId: %s for product: %s", productStockDto.getId().toString(), productStockDto.getProductId()));
        try {

            this.stockManagerService.updateStock(productStockDto.getId(), productStockDto.getProductId(), productStockDto.getQuantity(), productStockDto.getTimestamp());
            logger.info(String.format("Updated stockId: %s for product: %s", productStockDto.getId().toString(), productStockDto.getProductId()));
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (StockHasBeenUpdatedBeforeException | OptimisticLockException e) {

            logger.warn(String.format("Not updated stockId: %s for product: %s", productStockDto.getId().toString(), productStockDto.getProductId()));
            //This not restful, also indicates as if it is ok, even though something went wrong inside the API.
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            //Rethrow for controller error handling advice to handle.
            throw e;
        }
    }

    //GET /stock?productId=vegetable-123
    @ApiOperation(value = API_OP_GET_STOCK_INFORMATION_OF_PRODUCT)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = APIOperationMessages.SUCCESS_CALCULATING_STATISTICS),
            @ApiResponse(code = 404, message = APIErrorMessages.PRODUCT_NOT_FOUND)
    })
    @ResponseStatus(OK)
    @GetMapping(path = "/restless/stock")
    public ProductResponseDto getStockOfProduct(@RequestParam(value = PARAM_PRODUCT_ID) String productId) throws Exception {

        logger.info(String.format("Fetching stock of product: %s", productId));

        ProductResponseDto productResponseDto = this.stockManagerService.getStockOfProduct(productId, ZonedDateTime.now());

        logger.info(String.format("Fetched stock of product: %s", productId));

        return productResponseDto;
    }

    //GET /statistics?time=[today,lastMonth]
    @ApiOperation(value = API_OP_GET_STATISTICS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = APIOperationMessages.SUCCESS_CALCULATING_STATISTICS),
            @ApiResponse(code = 400, message = APIErrorMessages.UNDEFINED_TIME_SPAN)})
    @ResponseStatus(OK)
    @GetMapping(path = "/restless/statistics")
    public StatisticsResponseDto getStatistics(@RequestParam(value = PARAM_TIME) TimeSpan timeSpan) throws Exception {

        logger.info(String.format("Fetching statistics of %s", timeSpan.name()));

        StatisticsResponseDto statisticsResponseDto = this.stockManagerService.calculateStatistics(timeSpan, ZonedDateTime.now());

        logger.info(String.format("Fetched statistics of %s", timeSpan.name()));
        return statisticsResponseDto;
    }

}
