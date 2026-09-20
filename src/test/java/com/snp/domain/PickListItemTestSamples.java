package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PickListItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PickListItem getPickListItemSample1() {
        return new PickListItem().id(1L);
    }

    public static PickListItem getPickListItemSample2() {
        return new PickListItem().id(2L);
    }

    public static PickListItem getPickListItemRandomSampleGenerator() {
        return new PickListItem().id(longCount.incrementAndGet());
    }
}
