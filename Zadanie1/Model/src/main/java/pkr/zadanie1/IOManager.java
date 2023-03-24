package pkr.zadanie1;

import pkr.exceptions.IOOperationException;
import pkr.exceptions.IOReadException;
import pkr.exceptions.IOWriteException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class IOManager {

    private final String fileName;

    private static final byte[] HEX_LETTERS = "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);

    public IOManager(String fileName) {
        this.fileName = fileName;
    }

    public byte[] readBytesFromFile() throws IOReadException {
        byte[] inputFromFile = null;
        try (FileInputStream fileInput = new FileInputStream(fileName)) {
            inputFromFile = fileInput.readAllBytes();
        } catch (IOException ioExc) {
            throw new IOReadException("Nie powiodło się zczytanie tekstu jawnego z pliku.");
        }
        return inputFromFile;
    }

    public void saveBytesToFile(byte[] cipherText) throws IOWriteException {
        try (FileOutputStream fileOutput = new FileOutputStream(fileName)) {
            fileOutput.write(cipherText);
        } catch (IOException ioExc) {
            throw new IOWriteException("Nie powiodło się zapisanie szyfrogramu do pliku.");
        }
    }

    public static String convertByteArrayToHex(byte[] inputByteArray) {
        byte[] hexCharArray = new byte[inputByteArray.length * 2];
        for (int i = 0; i < inputByteArray.length; i++) {
            int value = inputByteArray[i] & 0xFF;
            hexCharArray[2 * i] = HEX_LETTERS[value >> 4];
            hexCharArray[2 * i + 1] = HEX_LETTERS[value & 0xF];
        }
        return new String(hexCharArray, StandardCharsets.US_ASCII);
    }

    public static byte[] convertHexToByteArray(String inputHexMessage) {
        byte[] messageByteArray = inputHexMessage.getBytes(StandardCharsets.UTF_8);
        byte[] resultByteArray = new byte[inputHexMessage.length() / 2];
        for (int i = 0; i < inputHexMessage.length(); i += 2) {
            resultByteArray[i / 2] = (byte) ((digitValue(messageByteArray[i]) << 4) + digitValue(messageByteArray[i + 1]));
        }
        return resultByteArray;
    }

    private static int digitValue(byte value) {
        for (int i = 0; i < HEX_LETTERS.length; i++) {
            if (value == HEX_LETTERS[i]) {
                return i;
            }
        }
        return -1;
    }
}
