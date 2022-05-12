package ptg.rest.common.multithreading;

public class FailedToSendMessageException extends RuntimeException {

    public FailedToSendMessageException() {
    }

    public FailedToSendMessageException(String s) {
        super(s);
    }

    public FailedToSendMessageException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FailedToSendMessageException(Throwable throwable) {
        super(throwable);
    }
}
