package info.alaz.stock.manager.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@EqualsAndHashCode(callSuper = true)
public class StockUpdateTimestampCannotBeInFutureException extends StockManagerRuntimeException {

    public static final String DEFAULT_MESSAGE = "Stock timestamp cannot be in future.";

    private final ZonedDateTime timestamp;

    public StockUpdateTimestampCannotBeInFutureException(String message, ZonedDateTime timestamp) {
        super(message);
        this.timestamp = timestamp;
    }

    public StockUpdateTimestampCannotBeInFutureException(String message, Throwable throwable, ZonedDateTime timestamp) {
        super(message, throwable);
        this.timestamp = timestamp;
    }

    public StockUpdateTimestampCannotBeInFutureException(ZonedDateTime timestamp) {
        super(DEFAULT_MESSAGE);
        this.timestamp = timestamp;
    }

    public StockUpdateTimestampCannotBeInFutureException(Throwable throwable, ZonedDateTime timestamp) {
        super(DEFAULT_MESSAGE, throwable);
        this.timestamp = timestamp;
    }
}
