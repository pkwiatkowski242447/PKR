package pkr.zadanie1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        AdvancedEncryptionStandard AES = new AdvancedEncryptionStandard();
        System.out.print("Zadanie - Podstawy kryptografii. Wpisz tekst do zaszfrowania: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String toCipher = reader.readLine();
        System.out.print("Podaj klucz do zaszyfrowania wiadomości: ");
        String encryptionKey = reader.readLine();
        byte[] encodedText = AES.encryptMessage(toCipher.getBytes(), encryptionKey.getBytes());
        System.out.println("Zaszyfrowana wiadomość: " + new String(encodedText, StandardCharsets.UTF_8));
        byte[] decodedText = AES.decryptMessage(encodedText, encryptionKey.getBytes());
        System.out.println("Odszyfrowana wiadomość: " + new String(decodedText, StandardCharsets.UTF_8));
    }
}