package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderAdjustmentTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static OrderAdjustmentType getOrderAdjustmentTypeSample1() {
        return new OrderAdjustmentType().id(1L).name("name1").description("description1");
    }

    public static OrderAdjustmentType getOrderAdjustmentTypeSample2() {
        return new OrderAdjustmentType().id(2L).name("name2").description("description2");
    }

    public static OrderAdjustmentType getOrderAdjustmentTypeRandomSampleGenerator() {
        return new OrderAdjustmentType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
