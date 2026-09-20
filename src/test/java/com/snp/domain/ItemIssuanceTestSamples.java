package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ItemIssuanceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ItemIssuance getItemIssuanceSample1() {
        return new ItemIssuance().id(1L).message("message1").issuedBy("issuedBy1");
    }

    public static ItemIssuance getItemIssuanceSample2() {
        return new ItemIssuance().id(2L).message("message2").issuedBy("issuedBy2");
    }

    public static ItemIssuance getItemIssuanceRandomSampleGenerator() {
        return new ItemIssuance()
            .id(longCount.incrementAndGet())
            .message(UUID.randomUUID().toString())
            .issuedBy(UUID.randomUUID().toString());
    }
}
