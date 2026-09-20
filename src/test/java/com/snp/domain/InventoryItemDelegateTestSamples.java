package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryItemDelegateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static InventoryItemDelegate getInventoryItemDelegateSample1() {
        return new InventoryItemDelegate().id(1L).sequenceNo(1).remarks("remarks1");
    }

    public static InventoryItemDelegate getInventoryItemDelegateSample2() {
        return new InventoryItemDelegate().id(2L).sequenceNo(2).remarks("remarks2");
    }

    public static InventoryItemDelegate getInventoryItemDelegateRandomSampleGenerator() {
        return new InventoryItemDelegate()
            .id(longCount.incrementAndGet())
            .sequenceNo(intCount.incrementAndGet())
            .remarks(UUID.randomUUID().toString());
    }
}
