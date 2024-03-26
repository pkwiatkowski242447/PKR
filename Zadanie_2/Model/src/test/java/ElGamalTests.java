import org.example.model.ElGamal;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.LengthException;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class ElGamalTests {
    private final ElGamal object = new ElGamal();

    public ElGamalTests() throws NoSuchAlgorithmException {
    }

    @Test
    public void RabinMillerTest() {
        assertFalse(object.RabinMillerTest(BigInteger.TWO));
        assertFalse(object.RabinMillerTest(BigInteger.ZERO));
        assertFalse(object.RabinMillerTest(BigInteger.TEN));
    }

    @Test
    public void generatorTests() throws LengthException {
        object.generatePrimeNumbers();
        String text = "KASDKAKSD";
        assertEquals(1, object.signMessage(text.getBytes())[0].compareTo(BigInteger.ZERO));
        assertEquals(1, object.signMessage(text.getBytes())[1].compareTo(BigInteger.ZERO));
    }
}
