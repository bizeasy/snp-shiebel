package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderItemBillingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static OrderItemBilling getOrderItemBillingSample1() {
        return new OrderItemBilling().id(1L);
    }

    public static OrderItemBilling getOrderItemBillingSample2() {
        return new OrderItemBilling().id(2L);
    }

    public static OrderItemBilling getOrderItemBillingRandomSampleGenerator() {
        return new OrderItemBilling().id(longCount.incrementAndGet());
    }
}
