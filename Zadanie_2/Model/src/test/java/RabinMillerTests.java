import org.example.model.ElGamal;

import static org.junit.jupiter.api.Assertions.*;

import org.testng.annotations.Test;

import java.math.BigInteger;

public class RabinMillerTests {
    private final ElGamal object = new ElGamal();

    @Test
    public void RabinMillerTest() {
        assertTrue(object.RabinMillerTest(BigInteger.ONE));
        assertFalse(object.RabinMillerTest(BigInteger.TWO));
        assertFalse(object.RabinMillerTest(BigInteger.ZERO));
        assertFalse(object.RabinMillerTest(BigInteger.TEN));
    }
}
