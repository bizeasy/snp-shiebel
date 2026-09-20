package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderShipmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static OrderShipment getOrderShipmentSample1() {
        return new OrderShipment().id(1L);
    }

    public static OrderShipment getOrderShipmentSample2() {
        return new OrderShipment().id(2L);
    }

    public static OrderShipment getOrderShipmentRandomSampleGenerator() {
        return new OrderShipment().id(longCount.incrementAndGet());
    }
}
