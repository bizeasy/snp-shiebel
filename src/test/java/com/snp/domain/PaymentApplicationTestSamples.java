package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentApplicationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PaymentApplication getPaymentApplicationSample1() {
        return new PaymentApplication().id(1L);
    }

    public static PaymentApplication getPaymentApplicationSample2() {
        return new PaymentApplication().id(2L);
    }

    public static PaymentApplication getPaymentApplicationRandomSampleGenerator() {
        return new PaymentApplication().id(longCount.incrementAndGet());
    }
}
