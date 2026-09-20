package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryTransferTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static InventoryTransfer getInventoryTransferSample1() {
        return new InventoryTransfer().id(1L).comments("comments1");
    }

    public static InventoryTransfer getInventoryTransferSample2() {
        return new InventoryTransfer().id(2L).comments("comments2");
    }

    public static InventoryTransfer getInventoryTransferRandomSampleGenerator() {
        return new InventoryTransfer().id(longCount.incrementAndGet()).comments(UUID.randomUUID().toString());
    }
}
