package pkr.zadanie1;

import pkr.exceptions.IOOperationException;
import pkr.exceptions.IOReadException;
import pkr.exceptions.IOWriteException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOManager {

    private final String fileName;

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
}
