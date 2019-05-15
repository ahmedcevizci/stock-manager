package info.alaz.stock.manager.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
public class StockNotBelongToProductException extends StockManagerRuntimeException {

    public static final String DEFAULT_MESSAGE = "Stock stockId: %s does not belong to product productName: %s";

    private final UUID stockId;
    private final String productName;

    public StockNotBelongToProductException(String message, UUID stockId, String productName) {
        super(message);
        this.stockId = stockId;
        this.productName = productName;
    }

    public StockNotBelongToProductException(String message, Throwable throwable, UUID stockId, String productName) {
        super(message, throwable);
        this.stockId = stockId;
        this.productName = productName;
    }

    public StockNotBelongToProductException(UUID stockId, String productName) {
        super(String.format(DEFAULT_MESSAGE, stockId.toString(), productName));
        this.stockId = stockId;
        this.productName = productName;
    }

    public StockNotBelongToProductException(Throwable throwable, UUID stockId, String productName) {
        super(DEFAULT_MESSAGE, throwable);
        this.stockId = stockId;
        this.productName = productName;
    }
}
