package pkr.zadanie2.exceptions;

public class NullValueException extends NullPointerException {
    public NullValueException() {
    }

    public NullValueException(String s) {
        super(s);
    }
}
