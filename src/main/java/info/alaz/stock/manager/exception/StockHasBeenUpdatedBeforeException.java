package info.alaz.stock.manager.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
public class StockHasBeenUpdatedBeforeException extends StockManagerException {

    public static final String DEFAULT_MESSAGE = "Stock stockId: %s has been updated by some other request in the meantime.";

    private final UUID stockId;

    public StockHasBeenUpdatedBeforeException(String message, UUID stockId) {
        super(message);
        this.stockId = stockId;
    }

    public StockHasBeenUpdatedBeforeException(String message, Throwable throwable, UUID stockId) {
        super(message, throwable);
        this.stockId = stockId;
    }

    public StockHasBeenUpdatedBeforeException(UUID stockId) {
        super(String.format(DEFAULT_MESSAGE, stockId.toString()));
        this.stockId = stockId;
    }

    public StockHasBeenUpdatedBeforeException(Throwable throwable, UUID stockId) {
        super(String.format(DEFAULT_MESSAGE, stockId.toString()), throwable);
        this.stockId = stockId;
    }
}
