package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReturnHeaderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ReturnHeader getReturnHeaderSample1() {
        return new ReturnHeader().id(1L).supplierRmaId("supplierRmaId1");
    }

    public static ReturnHeader getReturnHeaderSample2() {
        return new ReturnHeader().id(2L).supplierRmaId("supplierRmaId2");
    }

    public static ReturnHeader getReturnHeaderRandomSampleGenerator() {
        return new ReturnHeader().id(longCount.incrementAndGet()).supplierRmaId(UUID.randomUUID().toString());
    }
}
