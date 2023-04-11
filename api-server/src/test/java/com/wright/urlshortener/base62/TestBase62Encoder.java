package com.wright.urlshortener.base62;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBase62Encoder {

    @Test
    public void test() {
        String actual = Base62Encoder.encode(0);
        assertEquals("0000000", actual);

        actual = Base62Encoder.encode(1);
        assertEquals("0000001", actual);

        actual = Base62Encoder.encode(2);
        assertEquals("0000002", actual);

        actual = Base62Encoder.encode(10);
        assertEquals("000000a", actual);

        actual = Base62Encoder.encode(11);
        assertEquals("000000b", actual);

        actual = Base62Encoder.encode(62);
        assertEquals("0000010", actual);

        actual = Base62Encoder.encode(63);
        assertEquals("0000011", actual);

        actual = Base62Encoder.encode(11357);
        assertEquals("00002Xb", actual);

        actual = Base62Encoder.encode(11157);
        assertEquals("00002TX", actual);

        actual = Base62Encoder.encode(1000);
        assertEquals("00000g8", actual);
    }

    @Test
    public void testLongerTimestamps() {
        long nextId = 3_521_614_606_207L;
        String actual = Base62Encoder.encode(nextId);
        assertTrue(actual.length() <= 7);

        nextId = 2_199_023_255_551L;
        actual = Base62Encoder.encode(nextId);
        assertTrue(actual.length() <= 7);

        nextId = 4_398_046_511_103L;
        actual = Base62Encoder.encode(nextId);
        assertFalse(actual.length() <= 7);
    }
}
