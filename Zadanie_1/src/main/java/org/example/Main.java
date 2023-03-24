package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        AES object = new AES();
        System.out.println("Wpisz tekst do zaszyfrowania");
        Scanner sc = new Scanner(System.in);
        String message_input = sc.nextLine();
    }
}