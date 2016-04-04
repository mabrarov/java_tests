package ru.abrarov.javatests.windows_console_encoding;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException {
        final String data = "Айрат Натфуллин";
        final PrintStream ps = new PrintStream(System.out, false, "UTF-8");
        System.out.println(data);
        System.out.println(12);
        ps.print(data);
    }

}
