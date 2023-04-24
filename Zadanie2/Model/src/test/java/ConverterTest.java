import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import pkr.zadanie2.utils.Converter;

import java.nio.charset.StandardCharsets;

public class ConverterTest {

    @Test
    public void ConversionOfByteValuesToBase64StringTest() {
        byte[] inputByteValuesArray = "Some text to get encoded with Base64.".getBytes(StandardCharsets.UTF_8);
        byte[] expectedBase64String = "U29tZSB0ZXh0IHRvIGdldCBlbmNvZGVkIHdpdGggQmFzZTY0Lg==".getBytes(StandardCharsets.UTF_8);
        byte[] actualBase64String = Converter.byteValueToBase64Converter(inputByteValuesArray);
        assertArrayEquals(actualBase64String, expectedBase64String);
    }

    @Test
    public void ConversionOfBase64StringBackToByteValuesTest() {
        byte[] inputBase64String = "U29tZSB0ZXh0IHRvIGdldCBlbmNvZGVkIHdpdGggQmFzZTY0Lg==".getBytes(StandardCharsets.UTF_8);
        byte[] expectedByteValues = "Some text to get encoded with Base64.".getBytes(StandardCharsets.UTF_8);
        byte[] actualByteValues = Converter.base64ToByteValueConverter(inputBase64String);
        assertArrayEquals(actualByteValues, expectedByteValues);
    }
}
