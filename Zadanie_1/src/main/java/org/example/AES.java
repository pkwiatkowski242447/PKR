package org.example;

public class AES {
    public byte[][] string_to_byte_blocks(String text) {
        byte[] message_in_bytes = text.getBytes();
        StringBuilder text_in_binary = new StringBuilder();
        for (byte b : message_in_bytes) {                                                                               //Zamiana na binarkę
            int val = b;
            for (int i = 0; i < 8; i++) {
                text_in_binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        int text_length_div_128 = text_in_binary.length() / 128;
        int text_length_mod_128 = text_in_binary.length() % 128;
        int how_many_blocks;
        int where_it_ends_column;
        if (text_length_mod_128 != 0) {
            how_many_blocks = text_length_div_128 + 1;
        } else {
            how_many_blocks = text_length_div_128;
        }
        byte[][] text_in_bit_tab = new byte[how_many_blocks][128];                                                      //Tablica dwuwymiarowa z blokami 16 bajtowymi
        where_it_ends_column = (text_in_binary.length() - text_length_div_128 * 128) - 1;                               //Kiedy trzeba wpisywać 0 żeby dopełnić resztę wiersza/wierszy
        for (int i = 0; i < how_many_blocks; i++) {
            for (int j = 0; j < 128; j++) {
                if ((i == how_many_blocks - 1) && (j > text_in_binary.length() - 1)) {
                    while (where_it_ends_column < 128) {
                        text_in_bit_tab[i][where_it_ends_column] = 0;
                        where_it_ends_column++;
                    }
                } else text_in_bit_tab[i][j] = (byte) Character.getNumericValue(text_in_binary.charAt(j));
            }
        }
        return text_in_bit_tab;
    }
}
