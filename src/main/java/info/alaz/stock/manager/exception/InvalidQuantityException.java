package info.alaz.stock.manager.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class InvalidQuantityException extends StockManagerRuntimeException {

    public static final String DEFAULT_MESSAGE = "Quantity cannot be a negative number: %d";

    private final Integer quantity;

    public InvalidQuantityException(String message, Integer quantity) {
        super(message);
        this.quantity = quantity;
    }

    public InvalidQuantityException(String message, Throwable throwable, Integer quantity) {
        super(message, throwable);
        this.quantity = quantity;
    }

    public InvalidQuantityException(Integer quantity) {
        super(String.format(DEFAULT_MESSAGE, quantity));
        this.quantity = quantity;
    }

    public InvalidQuantityException(Throwable throwable, Integer quantity) {
        super(String.format(DEFAULT_MESSAGE, quantity), throwable);
        this.quantity = quantity;
    }
}
