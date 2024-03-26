package pkr.zadanie2.utils;

import java.util.Base64;

public class Converter {

    /*
        @ Class: Converter

        @ Description: This class (or rather its methods) is mainly used for converting byte arrays containing some
        sort of letters (e.g. in base64) and converted them back to byte values in order to avoid encoding problems.
     */

    /*
        @ Method: byteValueToBase64Converter

        @ Parameters:

        byte[] arrayOfByteValues -> array of bytes, where each and every byte represent a byte value, which later will
        be represented in base64 letters.
     */

    public static byte[] byteValueToBase64Converter(byte[] arrayOfByteValues) {
        return Base64.getEncoder().encode(arrayOfByteValues);
    }

    /*
        @ Method: base64ToByteValueConverter

        @ Parameters:

        byte[] arrayOfBase64Letters -> array of bytes, where each and every byte represent a base64 value, which later will
        be represented in byte values.
     */

    public static byte[] base64ToByteValueConverter(byte[] arrayOfBase64Letters) {
        return Base64.getDecoder().decode(arrayOfBase64Letters);
    }
}
