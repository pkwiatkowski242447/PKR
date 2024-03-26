import org.junit.jupiter.api.Test;
import pkr.zadanie2.exceptions.IOManagerReadException;
import pkr.zadanie2.exceptions.IOManagerWriteException;
import pkr.zadanie2.exceptions.IncorrectMessageDigestAlgorithm;
import pkr.zadanie2.model.ElGamalSystem;
import pkr.zadanie2.utils.IOManager;
import pkr.zadanie2.utils.Parser;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void intToByteArrayConversion() {
        int someNumber = 45236;
        byte[] byteArrayOfSomeNumber = Parser.intToByteArrayConversion(someNumber);
        byte[] expectedByteArray = {0x00, 0x00, (byte) 0xB0, (byte) 0xB4};
        assertArrayEquals(expectedByteArray, byteArrayOfSomeNumber);
    }

    @Test
    public void byteArrayToIntConversion() {
        byte[] someByteArray = {0x00, 0x00, (byte) 0xB0, (byte) 0xB4};
        int expectedIntNumber = 45236;
        int actualIntValue = Parser.byteArrayToIntConversion(someByteArray);
        assertEquals(expectedIntNumber, actualIntValue);
    }

    @Test
    public void savingPublicKeyToFilesAndReadingThemTest() {
        try {
            ElGamalSystem elGamal = new ElGamalSystem();
            elGamal.generateKeyMethod();
            byte[] toFile = Parser.parseForSavingToAFile(new BigInteger(elGamal.getGNumber()), new BigInteger(elGamal.getPNumber()), new BigInteger(elGamal.getHNumber()));
            IOManager.writeBytesToAFile("publicKey.txt", toFile);
            byte[] fromFile = IOManager.readBytesFromAGivenFile("publicKey.txt");
            List<byte[]> publicKey = Parser.parseByteArrayReadFromAFile(fromFile);
            assertEquals(new BigInteger(elGamal.getGNumber()), new BigInteger(publicKey.get(0)));
            assertEquals(new BigInteger(elGamal.getPNumber()), new BigInteger(publicKey.get(1)));
            assertEquals(new BigInteger(elGamal.getHNumber()), new BigInteger(publicKey.get(2)));
        } catch(IncorrectMessageDigestAlgorithm | IOManagerWriteException | IOManagerReadException exception) {
            fail();
        }
    }
}
