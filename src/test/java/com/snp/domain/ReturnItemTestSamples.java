package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReturnItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ReturnItem getReturnItemSample1() {
        return new ReturnItem().id(1L).sequenceNo(1).description("description1").returnItemPurpose("returnItemPurpose1");
    }

    public static ReturnItem getReturnItemSample2() {
        return new ReturnItem().id(2L).sequenceNo(2).description("description2").returnItemPurpose("returnItemPurpose2");
    }

    public static ReturnItem getReturnItemRandomSampleGenerator() {
        return new ReturnItem()
            .id(longCount.incrementAndGet())
            .sequenceNo(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .returnItemPurpose(UUID.randomUUID().toString());
    }
}
