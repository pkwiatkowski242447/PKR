package pkr.exceptions;

public class GeneralAESException extends RuntimeException {
    public GeneralAESException(String message) {
        super(message);
    }

    public GeneralAESException(String message, Throwable cause) {
        super(message, cause);
    }
}
