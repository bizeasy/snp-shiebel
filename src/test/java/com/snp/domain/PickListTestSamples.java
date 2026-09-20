package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PickListTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PickList getPickListSample1() {
        return new PickList().id(1L).description("description1");
    }

    public static PickList getPickListSample2() {
        return new PickList().id(2L).description("description2");
    }

    public static PickList getPickListRandomSampleGenerator() {
        return new PickList().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
