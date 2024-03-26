package pkr.exceptions;

public class KeyValidityException extends GeneralAESException {
    public KeyValidityException(String message) {
        super(message);
    }

    public KeyValidityException(String message, Throwable cause) {
        super(message, cause);
    }
}
