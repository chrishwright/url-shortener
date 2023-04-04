package com.wright.urlshortener.idgenerator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Unique ID generator loosely based off of the Snowflake ID generator.
 * The correlation between the number of sequence bits, worker bits,
 * and sequence max is important.  If you decrease the amount of bits in a sequence,
 * then the amount of time to process the next ID becomes longer because the code
 * has to recompute the sequence and wait for the timestamp to become updated.
 * The longer the sequence bits, the less frequently this has to occur.  However,
 * the longer the sequence bits, the longer the resulting ID becomes because everything
 * else gets shifted over, creating a longer ID.  So the length of the ID and the amount of
 * processing time become factors in choosing the appropriate numbers here.
 */
public class UniqueIdGenerator {

    private static final long EPOCH_TIME = 1680484979000L;
    private static final int WORKER_BITS = 5;
    private static final int SEQUENCE_BITS = 9;
    private static final long SEQUENCE_MAX = (long) Math.pow(2, SEQUENCE_BITS) - 1;
    private final long workerId;

    private final AtomicLong sequence = new AtomicLong();

    public UniqueIdGenerator(long workerId) {
        this.workerId = workerId;
    }

    public long nextId() {
        long timestamp = System.currentTimeMillis() - EPOCH_TIME;

        if (sequence.get() == SEQUENCE_MAX + 1) {
            timestamp = waitUntilNextMilli(timestamp);
            sequence.set(0);
        }

        // TODO: explore choosing a slightly shorter timestamp to save 4 bits?
        // E.G if the 1st number is removed from the timestamp, we might still have
        // ~70 years worth of unique IDs before we run into conflicts.  This might help
        // improve performance and be a good compromise between performance and longevity.
        timestamp <<= WORKER_BITS + SEQUENCE_BITS;

        timestamp |= workerId << SEQUENCE_BITS;

        timestamp |= sequence.getAndIncrement();

        return timestamp;
    }

    private long waitUntilNextMilli(long timestamp) {
        long newTimestamp = timestamp;
        while (newTimestamp == timestamp) {
            newTimestamp = System.currentTimeMillis() - EPOCH_TIME;
        }
        return newTimestamp;
    }
}
