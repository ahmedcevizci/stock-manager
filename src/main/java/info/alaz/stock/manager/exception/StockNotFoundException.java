package info.alaz.stock.manager.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
public class StockNotFoundException extends StockManagerRuntimeException {

    public static final String DEFAULT_MESSAGE = "No stock found for given stock id: %s";

    private final UUID stockId;

    public StockNotFoundException(String message, UUID stockId) {
        super(message);
        this.stockId = stockId;
    }

    public StockNotFoundException(String message, Throwable throwable, UUID stockId) {
        super(message, throwable);
        this.stockId = stockId;
    }

    public StockNotFoundException(UUID stockId) {
        super(String.format(DEFAULT_MESSAGE, stockId.toString()));
        this.stockId = stockId;
    }

    public StockNotFoundException(Throwable throwable, UUID stockId) {
        super(String.format(DEFAULT_MESSAGE, stockId.toString()), throwable);
        this.stockId = stockId;
    }
}
