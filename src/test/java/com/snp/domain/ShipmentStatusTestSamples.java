package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ShipmentStatus getShipmentStatusSample1() {
        return new ShipmentStatus().id(1L).sequenceNo(1);
    }

    public static ShipmentStatus getShipmentStatusSample2() {
        return new ShipmentStatus().id(2L).sequenceNo(2);
    }

    public static ShipmentStatus getShipmentStatusRandomSampleGenerator() {
        return new ShipmentStatus().id(longCount.incrementAndGet()).sequenceNo(intCount.incrementAndGet());
    }
}
