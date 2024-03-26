package pkr.exceptions;

public class NoMessageException extends GeneralAESException {
    public NoMessageException(String message) {
        super(message);
    }

    public NoMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
