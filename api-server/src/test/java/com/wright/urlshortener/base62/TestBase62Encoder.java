package com.wright.urlshortener.base62;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBase62Encoder {

    @Test
    public void test() {
        String actual = Base62Encoder.encode(0);
        assertEquals("0000000", actual);

        actual = Base62Encoder.encode(1);
        assertEquals("0000001", actual);

        actual = Base62Encoder.encode(2);
        assertEquals("0000002", actual);

        actual = Base62Encoder.encode(11357);
        assertEquals("0000bX2", actual);
    }
}
