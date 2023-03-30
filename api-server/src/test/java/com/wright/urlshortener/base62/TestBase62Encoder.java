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
}
