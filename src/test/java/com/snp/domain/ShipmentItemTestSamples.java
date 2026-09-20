package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ShipmentItem getShipmentItemSample1() {
        return new ShipmentItem().id(1L).sequenceNo(1).message("message1");
    }

    public static ShipmentItem getShipmentItemSample2() {
        return new ShipmentItem().id(2L).sequenceNo(2).message("message2");
    }

    public static ShipmentItem getShipmentItemRandomSampleGenerator() {
        return new ShipmentItem()
            .id(longCount.incrementAndGet())
            .sequenceNo(intCount.incrementAndGet())
            .message(UUID.randomUUID().toString());
    }
}
