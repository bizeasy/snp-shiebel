package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderContactMechTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static OrderContactMech getOrderContactMechSample1() {
        return new OrderContactMech().id(1L);
    }

    public static OrderContactMech getOrderContactMechSample2() {
        return new OrderContactMech().id(2L);
    }

    public static OrderContactMech getOrderContactMechRandomSampleGenerator() {
        return new OrderContactMech().id(longCount.incrementAndGet());
    }
}
