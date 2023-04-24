package pkr.zadanie2.utils;

import pkr.zadanie2.exceptions.IOManagerReadException;
import pkr.zadanie2.exceptions.IOManagerWriteException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOManager {

    /*
        @ Class: IOManager

        @ Description: The main aim of this class existence is to manage input / output operations to file
        with that being writes and reads of bytes stored in files.
     */

    /*
        @ Method: readBytesFromAGivenFile

        @ Parameters:

        String fileName -> name of a file (that is with absolute path) which bytes will be read from. After that they
        are stored in byte array, which reference is returned by the method to.
     */

    public static byte[] readBytesFromAGivenFile(String fileName) throws IOManagerReadException {
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            return fileInputStream.readAllBytes();
        } catch (IOException ioException) {
            throw new IOManagerReadException("Read operation from file: " + fileName + " resulted in error! ; Cause: " + ioException.getMessage(), ioException);
        }
    }

    /*
        @ Method: writeBytesToAFile

        @ Parameters:

        String fileName -> name of a file (that is with absolute path) which bytes will be written to.
        byte[] bytesToBeWrittenToAFile -> array of bytes that will be written to a given file
     */

    public static void writeBytesToAFile(String fileName, byte[] bytesToBeWrittenToAFile) throws IOManagerWriteException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            fileOutputStream.write(bytesToBeWrittenToAFile);
        } catch (IOException ioException) {
            throw new IOManagerWriteException("Write operation to file: " + fileName + " resulted in error! ; Cause: " + ioException.getMessage(), ioException);
        }
    }

}
