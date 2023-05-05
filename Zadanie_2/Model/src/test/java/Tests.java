import org.example.model.ElGamal;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.LengthException;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class Tests {
    private final ElGamal object = new ElGamal();
    private String text = "KASDKAKSD";

    @Test
    public void RabinMillerTest() {
        assertFalse(object.RabinMillerTest(BigInteger.TWO));
        assertFalse(object.RabinMillerTest(BigInteger.ZERO));
        assertFalse(object.RabinMillerTest(BigInteger.TEN));
    }

    @Test
    public void generatorTests() throws LengthException, NoSuchAlgorithmException {
        object.generatePrimeNumbers();
        assertTrue(object.signMessage(text.getBytes())[0].compareTo(BigInteger.ZERO) == 1);
        assertTrue(object.signMessage(text.getBytes())[1].compareTo(BigInteger.ZERO) == 1);
    }
}
