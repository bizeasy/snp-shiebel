package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Quote getQuoteSample1() {
        return new Quote().id(1L).name("name1").offerNo("offerNo1").description("description1").remarks("remarks1");
    }

    public static Quote getQuoteSample2() {
        return new Quote().id(2L).name("name2").offerNo("offerNo2").description("description2").remarks("remarks2");
    }

    public static Quote getQuoteRandomSampleGenerator() {
        return new Quote()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .offerNo(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .remarks(UUID.randomUUID().toString());
    }
}
