package pkr.zadanie2.exceptions;

import java.security.NoSuchAlgorithmException;

public class IncorrectMessageDigestAlgorithm extends NoSuchAlgorithmException {
    public IncorrectMessageDigestAlgorithm(String msg) {
        super(msg);
    }

    public IncorrectMessageDigestAlgorithm(String message, Throwable cause) {
        super(message, cause);
    }
}
