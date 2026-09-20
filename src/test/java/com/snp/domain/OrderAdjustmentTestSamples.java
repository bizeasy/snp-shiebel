package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderAdjustmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static OrderAdjustment getOrderAdjustmentSample1() {
        return new OrderAdjustment().id(1L).description("description1").comments("comments1").sourceRefId("sourceRefId1");
    }

    public static OrderAdjustment getOrderAdjustmentSample2() {
        return new OrderAdjustment().id(2L).description("description2").comments("comments2").sourceRefId("sourceRefId2");
    }

    public static OrderAdjustment getOrderAdjustmentRandomSampleGenerator() {
        return new OrderAdjustment()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .comments(UUID.randomUUID().toString())
            .sourceRefId(UUID.randomUUID().toString());
    }
}
