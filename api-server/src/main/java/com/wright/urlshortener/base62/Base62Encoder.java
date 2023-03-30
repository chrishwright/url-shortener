package com.wright.urlshortener.base62;

public class Base62Encoder {

    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(long number) {
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            final long remainder = number % 62;
            sb.insert(0, CHARS.charAt((int) remainder));
            number /= 62;
        }

        while (sb.length() < 7) {
            sb.insert(0, "0");
        }

        return sb.toString();
    }
}
