package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PaymentType getPaymentTypeSample1() {
        return new PaymentType().id(1L).name("name1").description("description1");
    }

    public static PaymentType getPaymentTypeSample2() {
        return new PaymentType().id(2L).name("name2").description("description2");
    }

    public static PaymentType getPaymentTypeRandomSampleGenerator() {
        return new PaymentType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
