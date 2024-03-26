package pkr.exceptions;

public class IOWriteException extends IOOperationException {
    public IOWriteException(String message) {
        super(message);
    }

    public IOWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
