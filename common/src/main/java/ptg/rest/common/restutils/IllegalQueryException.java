package ptg.rest.common.restutils;

public class IllegalQueryException extends RuntimeException {

    public IllegalQueryException() {
    }

    public IllegalQueryException(String message) {
        super(message);
    }

    public IllegalQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalQueryException(Throwable cause) {
        super(cause);
    }

    public IllegalQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
