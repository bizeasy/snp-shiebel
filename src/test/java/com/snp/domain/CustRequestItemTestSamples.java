package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CustRequestItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static CustRequestItem getCustRequestItemSample1() {
        return new CustRequestItem().id(1L).sequenceNo(1).priority(1).description("description1").story("story1");
    }

    public static CustRequestItem getCustRequestItemSample2() {
        return new CustRequestItem().id(2L).sequenceNo(2).priority(2).description("description2").story("story2");
    }

    public static CustRequestItem getCustRequestItemRandomSampleGenerator() {
        return new CustRequestItem()
            .id(longCount.incrementAndGet())
            .sequenceNo(intCount.incrementAndGet())
            .priority(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .story(UUID.randomUUID().toString());
    }
}
