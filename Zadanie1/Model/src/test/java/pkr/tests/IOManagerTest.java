package pkr.tests;

import org.junit.jupiter.api.Test;
import pkr.zadanie1.IOManager;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IOManagerTest {

    @Test
    public void convertByteArrayToHexTest() {
        byte[] inputByteArray = "Some text".getBytes(StandardCharsets.US_ASCII);
        String expectedResult = "536F6D652074657874";
        byte[] finalResult = IOManager.convertByteArrayToHex(inputByteArray);
        assertTrue(expectedResult.equals(new String(finalResult, StandardCharsets.US_ASCII)));
    }

    @Test
    public void convertHexToByteArray() {
        byte[] inputHex = "536F6D652074657874".getBytes(StandardCharsets.US_ASCII);
        byte[] expectedResult = "Some text".getBytes(StandardCharsets.US_ASCII);
        byte[] finalResult = IOManager.convertHexToByteArray(inputHex);
        assertTrue(Arrays.equals(expectedResult, finalResult));
    }

}
