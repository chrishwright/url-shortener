package com.wright.urlshortener.idgenerator;

import com.wright.urlshortener.base62.Base62Encoder;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.wright.urlshortener.service.UrlServiceImpl.DESIRED_URL_LENGTH;
import static com.wright.urlshortener.idgenerator.TimeIncreasingUniqueIdGenerator.WORKER_BITS;

public class TestTimeIncreasingUniqueIdGenerator {

    @Test
    public void testUniqueIdGeneratorWithManyGeneratorsToCheckCollisions() {
        UniqueIdGenerator uniqueIdGenerator = new TimeIncreasingUniqueIdGenerator(0);
        UniqueIdGenerator uniqueIdGeneratorTwo = new TimeIncreasingUniqueIdGenerator(1);
        UniqueIdGenerator uniqueIdGeneratorThree = new TimeIncreasingUniqueIdGenerator(2);

        Set<Long> seenValues = new HashSet<>();

        for (int i = 0; i < 1_250_000; i++) {
            long val = uniqueIdGenerator.nextId();
            long val2 = uniqueIdGeneratorTwo.nextId();
            long val3 = uniqueIdGeneratorThree.nextId();

            String valEncoded = Base62Encoder.encode(val);
            String val2Encoded = Base62Encoder.encode(val2);
            String val3Encoded = Base62Encoder.encode(val3);

            assertTrue(valEncoded.length() <= DESIRED_URL_LENGTH &&
                    val2Encoded.length() <= DESIRED_URL_LENGTH &&
                    val3Encoded.length() <= DESIRED_URL_LENGTH);

            if (seenValues.contains(val) || seenValues.contains(val2) || seenValues.contains(val3)) {
                throw new IllegalArgumentException("This ID has been seen before");
            } else {
                seenValues.add(val);
                seenValues.add(val2);
                seenValues.add(val3);
            }
        }
    }

    @Test
    public void testUniqueIdGeneratorToCheckLengthWithMaxWorkerBits() {
        UniqueIdGenerator uniqueIdGenerator = new TimeIncreasingUniqueIdGenerator((long) Math.pow(2, WORKER_BITS) - 1);

        for (int i = 0; i < 1_250_000; i++) {
            long nextId = uniqueIdGenerator.nextId();

            final String actual = Base62Encoder.encode(nextId);

            assertTrue(actual.length() <= DESIRED_URL_LENGTH);
        }
    }
}
