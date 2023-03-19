package pkr.tests;

import pkr.zadanie1.AdvancedEncryptionStandard;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AESTest {

    private final AdvancedEncryptionStandard AES = new AdvancedEncryptionStandard();

    @Test
    public void rotWordTest() {
        byte[] givenByte = {1, 2, 3, 4};
        byte[] predictedResult = {2, 3, 4, 1};
        byte[] resultByte = AES.rotateWord(givenByte);
        for (int i = 0; i < givenByte.length; i++) {
            assertEquals(resultByte[i], predictedResult[i]);
        }
    }

    @Test
    public void substituteWordTest() {
        byte[] someWord = {0x53, (byte) 0xED, 0x2f, (byte) 0xFF};
        byte[] predictedResult = {(byte) 0xED, 0x55, 0x15, 0x16};
        byte[] resultWord = AES.substituteWord(someWord);
        assertTrue(Arrays.equals(predictedResult, resultWord));
    }

    @Test
    public void roundConstantTest() {
        byte[] zeroRoundConstant  = AES.roundConstant(0);
        byte[] predictedRCon_0 = {0, 0, 0, 0};
        assertTrue(Arrays.equals(zeroRoundConstant, predictedRCon_0));
        byte[] firstRoundConstant = AES.roundConstant(1);
        byte[] predictedRCon_1 = {1, 0, 0, 0};
        assertTrue(Arrays.equals(firstRoundConstant, predictedRCon_1));
        byte[] secondRoundConstant = AES.roundConstant(2);
        byte[] predictedRCon_2 = {2, 0, 0, 0};
        assertTrue(Arrays.equals(secondRoundConstant, predictedRCon_2));
        byte[] wrongRoundGiven = AES.roundConstant(-1);
        byte[] predictedRCon_minus1 = {0, 0, 0, 0};
        assertTrue(Arrays.equals(wrongRoundGiven, predictedRCon_minus1));
        byte[] eightRoundConstant = AES.roundConstant(8);
        byte[] predictedRCon_8 = {(byte) 128, 0, 0, 0};
        assertTrue(Arrays.equals(eightRoundConstant, predictedRCon_8));
        byte[] ninthRoundConstant = AES.roundConstant(9);
        byte[] predictedRCon_9 = {(byte) 27, 0, 0, 0};
        assertTrue(Arrays.equals(ninthRoundConstant, predictedRCon_9));
        byte[] tenthRoundConstant = AES.roundConstant(10);
        byte[] predictedRCon_10 = {(byte) 54, 0, 0, 0};
        assertTrue(Arrays.equals(tenthRoundConstant, predictedRCon_10));
    }

    @Test
    public void calculateRoundCoefficientTest() {
        int roundCoefficient = AES.calculateCoefficientValue(0);
        assertEquals(0, roundCoefficient);
        roundCoefficient = AES.calculateCoefficientValue(1);
        assertEquals(1, roundCoefficient);
        roundCoefficient = AES.calculateCoefficientValue(2);
        assertEquals(2, roundCoefficient);
        roundCoefficient = AES.calculateCoefficientValue(-1);
        assertEquals(0, roundCoefficient);
        roundCoefficient = AES.calculateCoefficientValue(8);
        assertEquals(128, roundCoefficient);
        roundCoefficient = AES.calculateCoefficientValue(9);
        assertEquals(27, roundCoefficient);
        roundCoefficient = AES.calculateCoefficientValue(10);
        assertEquals(54, roundCoefficient);
    }

    @Test
    public void addRoundKeyTest() {         // Jednocześnie jest to test dla metody invertedAddRoundKeys
        byte[][] stateMatrix = {
                {0x61, 0x62, 0x63, 0x64},
                {0x31, 0x32, 0x33, 0x34},
                {0x65, 0x66, 0x67, 0x68},
                {0x35, 0x36, 0x37, 0x38}
        };

        byte[][] key = {
                {0x69, 0x6A, 0x6B, 0x6C},
                {0x39, 0x30, 0x31, 0x32},
                {0x6D, 0x6E, 0x6F, 0x70},
                {0x33, 0x34, 0x35, 0x36}
        };

        byte[][] resultStateMatrix = AES.addRoundKey(stateMatrix, key, 0);

        byte[][] predictedStateMatrix = {
                {0x08, 0x5B, 0x0E, 0x57},
                {0x5B, 0x02, 0x5D, 0x00},
                {0x0E, 0x57, 0x08, 0x5D},
                {0x59, 0x04, 0x47, 0x0E}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(resultStateMatrix[i][j], predictedStateMatrix[i][j]);
            }
        }
    }

    @Test
    public void shiftRowsTest() {
        byte[][] stateMatrix = {
                {0x61, 0x62, 0x63, 0x64},
                {0x31, 0x32, 0x33, 0x34},
                {0x65, 0x66, 0x67, 0x68},
                {0x35, 0x36, 0x37, 0x38}
        };

        byte[][] predictedStateMatrix = {
                {0x61, 0x62, 0x63, 0x64},
                {0x32, 0x33, 0x34, 0x31},
                {0x67, 0x68, 0x65, 0x66},
                {0x38, 0x35, 0x36, 0x37}
        };

        byte[][] resultStateMatrix = AES.shiftRows(stateMatrix);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(resultStateMatrix[i][j], predictedStateMatrix[i][j]);
            }
        }
    }

    @Test
    public void invertedShiftRowsTest() {
        byte[][] stateMatrix = {
                {0x61, 0x62, 0x63, 0x64},
                {0x31, 0x32, 0x33, 0x34},
                {0x65, 0x66, 0x67, 0x68},
                {0x35, 0x36, 0x37, 0x38}
        };

        byte[][] predictedStateMatrix = {
                {0x61, 0x62, 0x63, 0x64},
                {0x34, 0x31, 0x32, 0x33},
                {0x67, 0x68, 0x65, 0x66},
                {0x36, 0x37, 0x38, 0x35}
        };

        byte[][] resultStateMatrix = AES.invertedShiftRows(stateMatrix);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(resultStateMatrix[i][j], predictedStateMatrix[i][j]);
            }
        }
    }

    @Test
    public void subBytesMethodTest() {
        byte[][] stateMatrix = {
                {0x61, 0x62, 0x63, 0x64},
                {0x31, 0x32, 0x33, 0x34},
                {0x65, 0x66, 0x67, 0x68},
                {0x35, 0x36, 0x37, 0x38}
        };

        byte[][] predictedStateMatrix = {
                {(byte) 0xEF, (byte) 0xAA, (byte) 0xFB, (byte) 0x43},
                {(byte) 0xC7, (byte) 0x23, (byte) 0xC3, (byte) 0x18},
                {(byte) 0x4D, (byte) 0x33, (byte) 0x85, (byte) 0x45},
                {(byte) 0x96, (byte) 0x05, (byte) 0x9A, (byte) 0x07}
        };

        byte[][] resultStateMatrix = AES.subBytes(stateMatrix);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(resultStateMatrix[i][j], predictedStateMatrix[i][j]);
            }
        }
    }

    @Test
    public void invertedSubBytesMethodTest() {
        byte[][] stateMatrix = {
                {0x61, 0x62, 0x63, 0x64},
                {0x31, 0x32, 0x33, 0x34},
                {0x65, 0x66, 0x67, 0x68},
                {0x35, 0x36, 0x37, 0x38}
        };

        byte[][] predictedStateMatrix = {
                {(byte) 0xD8, (byte) 0xAB, (byte) 0x00, (byte) 0x8C},
                {(byte) 0x2E, (byte) 0xA1, (byte) 0x66, (byte) 0x28},
                {(byte) 0xBC, (byte) 0xD3, (byte) 0x0A, (byte) 0xF7},
                {(byte) 0xD9, (byte) 0x24, (byte) 0xB2, (byte) 0x76}
        };

        byte[][] resultStateMatrix = AES.invertedSubBytes(stateMatrix);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(resultStateMatrix[i][j], predictedStateMatrix[i][j]);
            }
        }
    }

    @Test
    public void byteMultiplicationTest() {
        byte firstByte = (byte) 0x57;
        byte secondByte = (byte) 0x83;
        byte resultByte = AES.byteMultiplication(firstByte, secondByte);
        assertEquals(resultByte, (byte) 0xC1);
    }

    @Test
    public void overallEncryptionAndDecryptionTest() {
        String inputMessage = "some input message from a user.";
        String inputKey = "abcd1234abcd1234abcd1234abcd1234";
        byte[] encryptedMessage = AES.encryptMessage(inputMessage.getBytes(), inputKey.getBytes());
        byte[] decryptedMessage = AES.decryptMessage(encryptedMessage, inputKey.getBytes());
        assertTrue(inputMessage.equals(new String(decryptedMessage, StandardCharsets.UTF_8)));
    }
}
