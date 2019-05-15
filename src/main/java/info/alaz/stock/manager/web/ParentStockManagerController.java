package info.alaz.stock.manager.web;


import info.alaz.stock.manager.service.StockManagerService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Getter
public abstract class ParentStockManagerController {

    public static final String STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE = "application/vnd.alaz.info.sensor.manager.v1.response+json";
    public static final String APPLICATION_JSON = "application/json";
    public static final String SWAGGER_VERSIONING = "Stock-Manager-V1";

    public static final String PARAM_PRODUCT_ID = "productId";
    public static final String PARAM_TIME = "time";
    public static final String PARAM_STOCK_ID = "stockId";

    private Logger logger = LoggerFactory.getLogger(ParentStockManagerController.class);

    protected final StockManagerService stockManagerService;

    public ParentStockManagerController(StockManagerService stockManagerService) {
        this.stockManagerService = stockManagerService;
    }

    @RequestMapping("/")
    public String index() {
        logger.trace("A TRACE Message");
        logger.debug("A DEBUG Message");
        logger.info("An INFO Message");
        logger.warn("A WARN Message");
        logger.error("An ERROR Message");

        return "Howdy! Check out the Logs to see the output...";
    }
}
