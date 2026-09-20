package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryItemVarianceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static InventoryItemVariance getInventoryItemVarianceSample1() {
        return new InventoryItemVariance().id(1L).varianceReason("varianceReason1").comments("comments1");
    }

    public static InventoryItemVariance getInventoryItemVarianceSample2() {
        return new InventoryItemVariance().id(2L).varianceReason("varianceReason2").comments("comments2");
    }

    public static InventoryItemVariance getInventoryItemVarianceRandomSampleGenerator() {
        return new InventoryItemVariance()
            .id(longCount.incrementAndGet())
            .varianceReason(UUID.randomUUID().toString())
            .comments(UUID.randomUUID().toString());
    }
}
