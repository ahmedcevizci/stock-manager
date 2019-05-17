package info.alaz.stock.manager.web;


import info.alaz.stock.manager.service.StockManagerService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import static info.alaz.stock.manager.web.ParentStockManagerController.BASE_API_PATH;

@Getter
@RequestMapping(value = BASE_API_PATH)
public abstract class ParentStockManagerController {

    public static final String BASE_API_PATH = "/api/stock-manager";

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
}
