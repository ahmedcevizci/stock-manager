package info.alaz.stock.manager.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class StockManagerException extends Exception {

    public StockManagerException(String message) {
        super(message);
    }

    public StockManagerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
