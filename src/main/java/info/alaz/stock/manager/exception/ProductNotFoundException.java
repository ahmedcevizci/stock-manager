package info.alaz.stock.manager.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ProductNotFoundException extends StockManagerRuntimeException {

    public static final String DEFAULT_MESSAGE = "No product found for given product name: %s";

    private final String productName;

    public ProductNotFoundException(String message, String productName) {
        super(message);
        this.productName = productName;
    }

    public ProductNotFoundException(String message, Throwable throwable, String productName) {
        super(message, throwable);
        this.productName = productName;
    }

    public ProductNotFoundException(String productName) {
        super(String.format(DEFAULT_MESSAGE, productName));
        this.productName = productName;
    }

    public ProductNotFoundException(Throwable throwable, String productName) {
        super(String.format(DEFAULT_MESSAGE, productName), throwable);
        this.productName = productName;
    }
}
