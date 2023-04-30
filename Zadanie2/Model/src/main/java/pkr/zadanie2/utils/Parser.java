package pkr.zadanie2.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    /*
        @ Method: parseForSavingToAFile

        @ Parameters:

        * BigInteger gNumber -> BigInteger value of gNumber. It is one of three elements of public key.
        * BigInteger pNumber -> BigInteger value of pNumber. It is one of three elements of public key.
        * BigInteger hNumber -> BigInteger value of hNumber. It is one of three elements of public key.

        @ Returns: Byte array containing three elements of public key: gNumber, pNumber and hNumber, that can be saved
        to a file.

        @ Description: This method is used for parsing public key to a form savable to a file. It is worth noting,
        that this array contains also sizes of byte arrays containing this numbers
        so that they can be parsed back to big integer form even though they can have variable size.
     */

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

    /*
        @ Method: parseByteArrayReadFromAFile

        @ Parameters:

        * byte[] byteArrayFromAFile -> byte array read from a file, that contains gNumber, pNumber and hNumber
        formatted by above method.

        @ Returns:

        * List<byte[]> -> list of byte arrays representing gNumber, pNumber and hNumber in that specific order.

        @ Description: This method is used for parsing back public key saved to a file, to individual values
        of pNumber, gNumber and hNumber, used in ElGamalSystem.
     */

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

        List<byte[]> publicKey = new ArrayList<>();
        publicKey.add(gNumber);
        publicKey.add(pNumber);
        publicKey.add(hNumber);
        return publicKey;
    }

    /*
        @ Method: parseSignatureToBeSavedToAFile

        @ Parameters:

        * BigInteger sNo1Number -> First element of signature, made with use of ElGamal crypto-system.
        * BigInteger sNo2Number -> Second element of signature, made with use of ElGamal crypto-system.

        @ Returns:

        * byte[] -> byte array containing signature, in a form to be saved to a file.

        @ Description: This method is used for parsing a generated signature into a form, that can
        be saved to a file, and then parsed back to array of BigInteger.
     */

    public static byte[] parseSignatureToBeSavedToAFile(BigInteger sNo1Number, BigInteger sNo2Number) {
        byte[] sNo1NumberInByteArray = sNo1Number.toByteArray();
        byte[] sNo2NumberInByteArray = sNo2Number.toByteArray();
        int sNo1NumberArraySize = sNo1NumberInByteArray.length;
        int sNo2NumberArraySize = sNo2NumberInByteArray.length;
        byte[] sNo1NumberLengthInByteArray = intToByteArrayConversion(sNo1NumberArraySize);
        byte[] sNo2NumberLengthInByteArray = intToByteArrayConversion(sNo2NumberArraySize);
        byte[] finalByteArray = new byte[2 * 4 + 2 + sNo1NumberArraySize + sNo2NumberArraySize];
        int currentPointer = 0;
        System.arraycopy(sNo1NumberLengthInByteArray, 0, finalByteArray, currentPointer, sNo1NumberLengthInByteArray.length);
        finalByteArray[sNo1NumberLengthInByteArray.length] = ':';
        currentPointer += sNo1NumberLengthInByteArray.length + 1;
        System.arraycopy(sNo2NumberLengthInByteArray, 0, finalByteArray, currentPointer, sNo2NumberLengthInByteArray.length);
        finalByteArray[currentPointer + sNo2NumberLengthInByteArray.length] = ':';
        currentPointer += sNo2NumberLengthInByteArray.length + 1;
        System.arraycopy(sNo1NumberInByteArray, 0, finalByteArray, currentPointer, sNo1NumberArraySize);
        currentPointer += sNo1NumberArraySize;
        System.arraycopy(sNo2NumberInByteArray, 0, finalByteArray, currentPointer, sNo2NumberArraySize);
        return finalByteArray;
    }

    /*
        @ Method: parseSignatureToBeShownInsideTheProgram

        @ Parameters:

        * byte[] byteArrayFromAFile -> Byte array read from a file, that contains formatted signature.

        @ Returns:

        * List<byte[]> -> List of byte arrays representing sNo1Number and sNo2Number in that specific order.

        @ Description: This method is used for parsing back signature saved to a file, to the form of individual
        values of sNo1Number and sNo2Number.
     */

    public static List<byte[]> parseSignatureToBeShownInsideTheProgram(byte[] byteArrayFromAFile) {
        int filePositionPointer = 0;
        int lastOnePointer = 0;
        while(byteArrayFromAFile[filePositionPointer] != ':') {
            filePositionPointer++;
        }
        byte[] sNo1NumberLengthInArray = new byte[filePositionPointer];
        System.arraycopy(byteArrayFromAFile, 0, sNo1NumberLengthInArray, 0, filePositionPointer);
        filePositionPointer++;
        lastOnePointer = filePositionPointer;

        while(byteArrayFromAFile[filePositionPointer] != ':') {
            filePositionPointer++;
        }
        byte[] sNo2NumberLengthInArray = new byte[filePositionPointer - lastOnePointer + 1];
        System.arraycopy(byteArrayFromAFile, lastOnePointer, sNo2NumberLengthInArray, 0, filePositionPointer - lastOnePointer + 1);
        filePositionPointer++;
        lastOnePointer = filePositionPointer;

        int sNo1NumberLength = byteArrayToIntConversion(sNo1NumberLengthInArray);
        int sNo2NumberLength = byteArrayToIntConversion(sNo2NumberLengthInArray);

        byte[] sNo1Number = new byte[sNo1NumberLength];
        byte[] sNo2Number = new byte[sNo2NumberLength];

        System.arraycopy(byteArrayFromAFile, lastOnePointer, sNo1Number, 0, sNo1NumberLength);
        lastOnePointer += sNo1NumberLength;
        System.arraycopy(byteArrayFromAFile, lastOnePointer, sNo2Number, 0, sNo2NumberLength);

        List<byte[]> signature = new ArrayList<>();
        signature.add(sNo1Number);
        signature.add(sNo2Number);
        return signature;
    }

    /*
        @ Method: intToByteArrayConversion

        @ Parameters:

        * int someNumber -> some integer value, that later will be converted to byte array form.

        @ Returns:

        * byte[] -> byte array that contains converted int number.

        @ Description: This method is used for converting int number to byte array representation - used for
        converting length of a byte array to byte array, which can be saved to a file easily.
     */

    public static byte[] intToByteArrayConversion(int someNumber) {
        byte[] byteArrayRepresentationOfInt = new byte[4];
        byteArrayRepresentationOfInt[0] = (byte) ((someNumber >> 24) & 0xFF);
        byteArrayRepresentationOfInt[1] = (byte) ((someNumber >> 16) & 0xFF);
        byteArrayRepresentationOfInt[2] = (byte) ((someNumber >> 8) & 0xFF);
        byteArrayRepresentationOfInt[3] = (byte) ((someNumber) & 0xFF);
        return byteArrayRepresentationOfInt;
    }

    /*
        @ Method: byteArrayToIntConversion

        @ Parameters:

        * byte[] someNumberInByteArray -> byte array that contains some integer values, stored in separate bytes.

        @ Returns:

        * int -> some integer value, that was previously converted to someNumberInByteArray byte array.

        @ Description: This method is used for converting byte array, containing an int number, back to its
        integer representation.
     */

    public static int byteArrayToIntConversion(byte[] someNumberInByteArray) {
        int someNumber = 0;
        someNumber |= ((someNumberInByteArray[0] << 24) & 0xFF000000);
        someNumber |= ((someNumberInByteArray[1] << 16) & 0x00FF0000);
        someNumber |= ((someNumberInByteArray[2] << 8) & 0x0000FF00);
        someNumber |= ((someNumberInByteArray[3]) & 0x000000FF);
        return someNumber;
    }
}
