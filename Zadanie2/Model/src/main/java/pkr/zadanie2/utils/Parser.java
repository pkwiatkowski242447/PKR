package pkr.zadanie2.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static byte[] parseForSavingToAFile(BigInteger gNumber, BigInteger pNumber, BigInteger hNumber) {
        byte[] gNumberInByteArray = gNumber.toByteArray();
        byte[] pNumberInByteArray = pNumber.toByteArray();
        byte[] hNumberInByteArray = hNumber.toByteArray();
        int gNumberArraySize = gNumberInByteArray.length;
        int pNumberArraySize = pNumberInByteArray.length;
        int hNumberArraySize = hNumberInByteArray.length;
        byte[] gNumberLengthInByteArray = intToByteArrayConversion(gNumberArraySize);
        byte[] pNumberLengthInByteArray = intToByteArrayConversion(pNumberArraySize);
        byte[] hNumberLengthInByteArray = intToByteArrayConversion(hNumberArraySize);
        byte[] finalByteArray = new byte[3 * 4 + 3 + gNumberArraySize + pNumberArraySize + hNumberArraySize];
        int currentPointer = 0;
        System.arraycopy(gNumberLengthInByteArray, 0, finalByteArray, currentPointer, gNumberLengthInByteArray.length);
        finalByteArray[gNumberLengthInByteArray.length] = ':';
        currentPointer += gNumberLengthInByteArray.length + 1;
        System.arraycopy(pNumberLengthInByteArray, 0, finalByteArray, currentPointer, pNumberLengthInByteArray.length);
        finalByteArray[currentPointer + pNumberLengthInByteArray.length] = ':';
        currentPointer += pNumberLengthInByteArray.length + 1;
        System.arraycopy(hNumberLengthInByteArray, 0, finalByteArray, currentPointer, hNumberLengthInByteArray.length);
        finalByteArray[currentPointer + hNumberLengthInByteArray.length] = ':';
        currentPointer += hNumberLengthInByteArray.length + 1;
        System.arraycopy(gNumberInByteArray, 0, finalByteArray, currentPointer, gNumberArraySize);
        currentPointer += gNumberArraySize;
        System.arraycopy(pNumberInByteArray, 0, finalByteArray, currentPointer, pNumberArraySize);
        currentPointer += pNumberArraySize;
        System.arraycopy(hNumberInByteArray, 0, finalByteArray, currentPointer, hNumberArraySize);
        return finalByteArray;
    }

    public static List<byte[]> parseByteArrayReadFromAFile(byte[] byteArrayFromAFile) {
        int filePositionPointer = 0;
        int lastOnePointer = 0;
        while(byteArrayFromAFile[filePositionPointer] != ':') {
            filePositionPointer++;
        }
        byte[] gNumberLengthInArray = new byte[filePositionPointer];
        System.arraycopy(byteArrayFromAFile, 0, gNumberLengthInArray, 0, filePositionPointer);
        filePositionPointer++;
        lastOnePointer = filePositionPointer;

        while(byteArrayFromAFile[filePositionPointer] != ':') {
            filePositionPointer++;
        }
        byte[] pNumberLengthInArray = new byte[filePositionPointer - lastOnePointer + 1];
        System.arraycopy(byteArrayFromAFile, lastOnePointer, pNumberLengthInArray, 0, filePositionPointer - lastOnePointer + 1);
        filePositionPointer++;
        lastOnePointer = filePositionPointer;

        while(byteArrayFromAFile[filePositionPointer] != ':') {
            filePositionPointer++;
        }
        byte[] hNumberLengthInArray = new byte[filePositionPointer - lastOnePointer + 1];
        System.arraycopy(byteArrayFromAFile, lastOnePointer, hNumberLengthInArray, 0, filePositionPointer - lastOnePointer + 1);
        filePositionPointer++;
        lastOnePointer = filePositionPointer;

        int gNumberLength = byteArrayToIntConversion(gNumberLengthInArray);
        int pNumberLength = byteArrayToIntConversion(pNumberLengthInArray);
        int hNumberLength = byteArrayToIntConversion(hNumberLengthInArray);

        byte[] gNumber = new byte[gNumberLength];
        byte[] pNumber = new byte[pNumberLength];
        byte[] hNumber = new byte[hNumberLength];

        System.arraycopy(byteArrayFromAFile, lastOnePointer, gNumber, 0, gNumberLength);
        lastOnePointer += gNumberLength;
        System.arraycopy(byteArrayFromAFile, lastOnePointer, pNumber, 0, pNumberLength);
        lastOnePointer += pNumberLength;
        System.arraycopy(byteArrayFromAFile, lastOnePointer, hNumber, 0, hNumberLength);

        List<byte[]> publicKey = new ArrayList<byte[]>();
        publicKey.add(gNumber);
        publicKey.add(pNumber);
        publicKey.add(hNumber);
        return publicKey;
    }

    public static byte[] intToByteArrayConversion(int someNumber) {
        byte[] byteArrayRepresentationOfInt = new byte[4];
        byteArrayRepresentationOfInt[0] = (byte) ((someNumber >> 24) & 0xFF);
        byteArrayRepresentationOfInt[1] = (byte) ((someNumber >> 16) & 0xFF);
        byteArrayRepresentationOfInt[2] = (byte) ((someNumber >> 8) & 0xFF);
        byteArrayRepresentationOfInt[3] = (byte) ((someNumber) & 0xFF);
        return byteArrayRepresentationOfInt;
    }

    public static int byteArrayToIntConversion(byte[] someNumberInByteArray) {
        int someNumber = 0;
        someNumber |= ((someNumberInByteArray[0] << 24) & 0xFF000000);
        someNumber |= ((someNumberInByteArray[1] << 16) & 0x00FF0000);
        someNumber |= ((someNumberInByteArray[2] << 8) & 0x0000FF00);
        someNumber |= ((someNumberInByteArray[3]) & 0x000000FF);
        return someNumber;
    }
}
