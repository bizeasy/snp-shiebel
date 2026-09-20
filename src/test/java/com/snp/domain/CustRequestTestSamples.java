package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CustRequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static CustRequest getCustRequestSample1() {
        return new CustRequest()
            .id(1L)
            .name("name1")
            .description("description1")
            .priority(1)
            .internalComment("internalComment1")
            .custEstimatedDurationDays(1);
    }

    public static CustRequest getCustRequestSample2() {
        return new CustRequest()
            .id(2L)
            .name("name2")
            .description("description2")
            .priority(2)
            .internalComment("internalComment2")
            .custEstimatedDurationDays(2);
    }

    public static CustRequest getCustRequestRandomSampleGenerator() {
        return new CustRequest()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .priority(intCount.incrementAndGet())
            .internalComment(UUID.randomUUID().toString())
            .custEstimatedDurationDays(intCount.incrementAndGet());
    }
}
