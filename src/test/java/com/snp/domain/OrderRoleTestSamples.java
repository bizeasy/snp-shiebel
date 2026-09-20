package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderRoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static OrderRole getOrderRoleSample1() {
        return new OrderRole().id(1L);
    }

    public static OrderRole getOrderRoleSample2() {
        return new OrderRole().id(2L);
    }

    public static OrderRole getOrderRoleRandomSampleGenerator() {
        return new OrderRole().id(longCount.incrementAndGet());
    }
}
