package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryItemTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static InventoryItemType getInventoryItemTypeSample1() {
        return new InventoryItemType().id(1L).name("name1").description("description1");
    }

    public static InventoryItemType getInventoryItemTypeSample2() {
        return new InventoryItemType().id(2L).name("name2").description("description2");
    }

    public static InventoryItemType getInventoryItemTypeRandomSampleGenerator() {
        return new InventoryItemType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
