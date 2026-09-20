package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentReceiptTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ShipmentReceipt getShipmentReceiptSample1() {
        return new ShipmentReceipt().id(1L).sequenceNo(1).message("message1");
    }

    public static ShipmentReceipt getShipmentReceiptSample2() {
        return new ShipmentReceipt().id(2L).sequenceNo(2).message("message2");
    }

    public static ShipmentReceipt getShipmentReceiptRandomSampleGenerator() {
        return new ShipmentReceipt()
            .id(longCount.incrementAndGet())
            .sequenceNo(intCount.incrementAndGet())
            .message(UUID.randomUUID().toString());
    }
}
