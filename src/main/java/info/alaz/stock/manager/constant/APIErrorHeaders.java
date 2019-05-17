package info.alaz.stock.manager.constant;

public class APIErrorHeaders {

    public static final String PRODUCT_NOT_FOUND = "ProductNotFound";
    public static final String STOCK_NOT_FOUND = "StockNotFound";
    public static final String STOCK_NOT_BELONGS_TO_PRODUCT = "StockNotBelongsToProduct";
    public static final String STOCK_UPDATED_BY_ANOTHER_REQUEST = "StockUpdatedByAnotherRequest";
    public static final String STOCK_UPDATE_TIMESTAMP_CANNOT_BE_IN_FUTURE = "StockUpdateCannotBeInFuture";
    public static final String STOCK_QUANTITY_CANNOT_BE_NEGATIVE = "StockQuantityCannotBeNegative";
}
