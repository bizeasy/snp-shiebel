package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ReturnItemBillingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ReturnItemBilling getReturnItemBillingSample1() {
        return new ReturnItemBilling().id(1L);
    }

    public static ReturnItemBilling getReturnItemBillingSample2() {
        return new ReturnItemBilling().id(2L);
    }

    public static ReturnItemBilling getReturnItemBillingRandomSampleGenerator() {
        return new ReturnItemBilling().id(longCount.incrementAndGet());
    }
}
