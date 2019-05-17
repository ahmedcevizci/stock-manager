package info.alaz.stock.manager.constant;

public class APIErrorMessages {

    public static final String UNDEFINED_TIME_SPAN = "Undefined time span specified";
    public static final String PRODUCT_NOT_FOUND = "No product found by given name.";
    public static final String STOCK_NOT_FOUND = "No stock found by given id.";
    public static final String STOCK_NOT_BELONGS_TO_PRODUCT = "Indicated stock does not belong to given product.";
    public static final String STOCK_UPDATE_TIME_CANNOT_BE_IN_FUTURE = "Stock update time cannot be in future.";
    public static final String STOCK_UPDATED_IN_THE_MEAN_TIME = "Stock has been updated by another request in the mean time.";
    public static final String QUANTITY_CANNOT_BE_NEGATIVE = "Stock quantity cannot be a negative number.";
}
