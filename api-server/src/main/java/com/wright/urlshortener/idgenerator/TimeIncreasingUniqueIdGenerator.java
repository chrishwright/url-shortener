package com.wright.urlshortener.idgenerator;

import java.util.concurrent.atomic.AtomicLong;

public class TimeIncreasingUniqueIdGenerator implements UniqueIdGenerator {
    public static final int WORKER_BITS = 4;
    public static final int SEQUENCE_BITS = 9;
    public static final long EPOCH_TIME = 1680484979000L;
    private static final long SEQUENCE_MAX = (long) Math.pow(2, SEQUENCE_BITS);

    private final long workerId;
    private final AtomicLong sequence = new AtomicLong();

    public TimeIncreasingUniqueIdGenerator(long workerId) {
        this.workerId = workerId;
    }

    public long nextId() {
        long timestamp = System.currentTimeMillis() - EPOCH_TIME;

        if (sequence.get() == SEQUENCE_MAX) {
            timestamp = waitUntilNextMilli(timestamp);
            sequence.set(0);
        }

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
