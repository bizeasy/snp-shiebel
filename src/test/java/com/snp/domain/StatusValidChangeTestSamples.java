package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StatusValidChangeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static StatusValidChange getStatusValidChangeSample1() {
        return new StatusValidChange().id(1L).transitionName("transitionName1");
    }

    public static StatusValidChange getStatusValidChangeSample2() {
        return new StatusValidChange().id(2L).transitionName("transitionName2");
    }

    public static StatusValidChange getStatusValidChangeRandomSampleGenerator() {
        return new StatusValidChange().id(longCount.incrementAndGet()).transitionName(UUID.randomUUID().toString());
    }
}
