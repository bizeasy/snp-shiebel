package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderItemTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static OrderItemType getOrderItemTypeSample1() {
        return new OrderItemType().id(1L).name("name1").description("description1");
    }

    public static OrderItemType getOrderItemTypeSample2() {
        return new OrderItemType().id(2L).name("name2").description("description2");
    }

    public static OrderItemType getOrderItemTypeRandomSampleGenerator() {
        return new OrderItemType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
