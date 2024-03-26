package pkr.exceptions;

import java.io.IOException;

public class IOOperationException extends IOException {
    public IOOperationException(String message) {
        super(message);
    }

    public IOOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
