package info.alaz.stock.manager.web.errorhandling;


import info.alaz.stock.manager.constant.APIErrorHeaders;
import info.alaz.stock.manager.exception.*;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.OptimisticLockException;
import javax.validation.ConstraintViolationException;

@NoArgsConstructor
@RestControllerAdvice
public class StockManagerExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(StockManagerExceptionHandler.class);

    protected ResponseEntity<ErrorResponse> createResponse(HttpStatus status, HttpHeaders headers, String reason) {
        ErrorResponse error = new ErrorResponse(status.value(), reason);
        return new ResponseEntity(error, headers, status);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundRequest(final ProductNotFoundException ex) {
        logger.warn("Stock Manager Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-StockManagerAPIError", "error." + APIErrorHeaders.PRODUCT_NOT_FOUND);
        return createResponse(HttpStatus.NOT_FOUND, headers, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundRequest(final StockNotFoundException ex) {
        logger.warn("Stock Manager Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-StockManagerAPIError", "error." + APIErrorHeaders.STOCK_NOT_FOUND);
        return createResponse(HttpStatus.NOT_FOUND, headers, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundRequest(final StockNotBelongToProductException ex) {
        logger.warn("Stock Manager Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-StockManagerAPIError", "error." + APIErrorHeaders.STOCK_NOT_BELONGS_TO_PRODUCT);
        return createResponse(HttpStatus.NOT_FOUND, headers, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleConflictRequest(final StockHasBeenUpdatedBeforeException ex) {
        logger.warn("Stock Manager Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-StockManagerAPIError", "error." + APIErrorHeaders.STOCK_UPDATED_BY_ANOTHER_REQUEST);
        return createResponse(HttpStatus.CONFLICT, headers, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleNotFoundRequest(final InvalidQuantityException ex) {
        logger.warn("Stock Manager Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-StockManagerAPIError", "error." + APIErrorHeaders.STOCK_QUANTITY_CANNOT_BE_NEGATIVE);
        return createResponse(HttpStatus.BAD_REQUEST, headers, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleConflictRequest(final OptimisticLockException ex) {
        logger.warn("Stock Manager Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-StockManagerAPIError", "error." + APIErrorHeaders.STOCK_UPDATED_BY_ANOTHER_REQUEST);
        return createResponse(HttpStatus.CONFLICT, headers, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleConflictRequest(final StockUpdateTimestampCannotBeInFutureException ex) {
        logger.warn("Stock Manager Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-StockManagerAPIError", "error." + APIErrorHeaders.STOCK_UPDATE_TIMESTAMP_CANNOT_BE_IN_FUTURE);
        return createResponse(HttpStatus.CONFLICT, headers, ex.getMessage());
    }

    //Other general HTTP exceptions
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequest(MethodArgumentNotValidException ex) {
        logger.warn("Stock Manager Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        return this.createResponse(HttpStatus.BAD_REQUEST, headers, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequest(ConstraintViolationException ex) {
        logger.warn("Stock Manager Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        return this.createResponse(HttpStatus.BAD_REQUEST, headers, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<ErrorResponse> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        logger.warn("Stock Manager Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        return this.createResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, headers, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<ErrorResponse> handleUnsupportedMediaType(HttpMediaTypeNotAcceptableException ex) {
        logger.warn("Stock Manager Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        return this.createResponse(HttpStatus.NOT_ACCEPTABLE, headers, ex.getMessage());
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleThrowable(Exception ex) {
        logger.warn("Stock Manager Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        return this.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, headers, ex.getMessage());
    }

}
