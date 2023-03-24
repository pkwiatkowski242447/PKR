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
        String finalResult = IOManager.convertByteArrayToHex(inputByteArray);
        assertTrue(expectedResult.equals(finalResult));
    }

    @Test
    public void convertHexToByteArray() {
        String inputHex = "536F6D652074657874";
        byte[] expectedResult = "Some text".getBytes(StandardCharsets.UTF_8);
        byte[] finalResult = IOManager.convertHexToByteArray(inputHex);
        assertTrue(Arrays.equals(expectedResult, finalResult));
    }

}
