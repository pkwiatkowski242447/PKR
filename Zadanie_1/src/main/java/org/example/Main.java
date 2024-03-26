package org.example;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {

        AES object = new AES();
        System.out.println("Wpisz tekst do zaszyfrowania");
        Scanner sc = new Scanner(System.in);
        String message_input = sc.nextLine();
    }
}