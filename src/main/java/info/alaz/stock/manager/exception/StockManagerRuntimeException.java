package info.alaz.stock.manager.exception;


import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class StockManagerRuntimeException extends RuntimeException {

    public StockManagerRuntimeException(String message) {
        super(message);
    }

    public StockManagerRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
