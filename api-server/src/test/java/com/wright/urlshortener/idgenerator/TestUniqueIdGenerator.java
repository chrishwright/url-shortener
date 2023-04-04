package com.wright.urlshortener.idgenerator;

import com.wright.urlshortener.base62.Base62Encoder;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUniqueIdGenerator {
    @Test
    public void testUniqueIdGeneratorWithManyGeneratorsToCheckCollisions() {
        UniqueIdGenerator uniqueIdGenerator = new UniqueIdGenerator(0);
        UniqueIdGenerator uniqueIdGeneratorTwo = new UniqueIdGenerator(30);
        UniqueIdGenerator uniqueIdGeneratorThree = new UniqueIdGenerator(25);

        Set<Long> seenValues = new HashSet<>();

        for (int i = 0; i < 1_250_000; i++) {
            long val = uniqueIdGenerator.nextId();
            long val2 = uniqueIdGeneratorTwo.nextId();
            long val3 = uniqueIdGeneratorThree.nextId();

            if (seenValues.contains(val) || seenValues.contains(val2) || seenValues.contains(val3)) {
                throw new IllegalArgumentException("blah seen before");
            } else {
                seenValues.add(val);
                seenValues.add(val2);
                seenValues.add(val3);
            }
        }
    }

    @Test
    public void testUniqueIdGenerator() {
        UniqueIdGenerator uniqueIdGenerator = new UniqueIdGenerator(0);

        for (int i = 0; i < 1_250_000; i++) {
            long val = uniqueIdGenerator.nextId();

            final String actual = Base62Encoder.encode(val);
            assertTrue(actual.length() <= 7);
        }
    }
}
