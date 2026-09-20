package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OrderStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static OrderStatus getOrderStatusSample1() {
        return new OrderStatus().id(1L).sequenceNo(1);
    }

    public static OrderStatus getOrderStatusSample2() {
        return new OrderStatus().id(2L).sequenceNo(2);
    }

    public static OrderStatus getOrderStatusRandomSampleGenerator() {
        return new OrderStatus().id(longCount.incrementAndGet()).sequenceNo(intCount.incrementAndGet());
    }
}
