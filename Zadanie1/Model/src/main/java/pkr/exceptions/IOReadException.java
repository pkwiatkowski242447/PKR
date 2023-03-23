package pkr.exceptions;

public class IOReadException extends IOOperationException {
    public IOReadException(String message) {
        super(message);
    }

    public IOReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
