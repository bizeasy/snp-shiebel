package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static InventoryItem getInventoryItemSample1() {
        return new InventoryItem()
            .id(1L)
            .containerId("containerId1")
            .batchNo("batchNo1")
            .mfgBatchNo("mfgBatchNo1")
            .lotNoStr("lotNoStr1")
            .binNumber("binNumber1")
            .comments("comments1")
            .serialNumber("serialNumber1")
            .softIdentifier("softIdentifier1")
            .activationNumber("activationNumber1");
    }

    public static InventoryItem getInventoryItemSample2() {
        return new InventoryItem()
            .id(2L)
            .containerId("containerId2")
            .batchNo("batchNo2")
            .mfgBatchNo("mfgBatchNo2")
            .lotNoStr("lotNoStr2")
            .binNumber("binNumber2")
            .comments("comments2")
            .serialNumber("serialNumber2")
            .softIdentifier("softIdentifier2")
            .activationNumber("activationNumber2");
    }

    public static InventoryItem getInventoryItemRandomSampleGenerator() {
        return new InventoryItem()
            .id(longCount.incrementAndGet())
            .containerId(UUID.randomUUID().toString())
            .batchNo(UUID.randomUUID().toString())
            .mfgBatchNo(UUID.randomUUID().toString())
            .lotNoStr(UUID.randomUUID().toString())
            .binNumber(UUID.randomUUID().toString())
            .comments(UUID.randomUUID().toString())
            .serialNumber(UUID.randomUUID().toString())
            .softIdentifier(UUID.randomUUID().toString())
            .activationNumber(UUID.randomUUID().toString());
    }
}
