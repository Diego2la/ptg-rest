package ptg.rest.common.multithreading;

public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException() {
    }

    public ServiceUnavailableException(String s) {
        super(s);
    }

    public ServiceUnavailableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ServiceUnavailableException(Throwable throwable) {
        super(throwable);
    }
}
