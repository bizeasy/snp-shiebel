package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuoteTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static QuoteType getQuoteTypeSample1() {
        return new QuoteType().id(1L).name("name1").description("description1");
    }

    public static QuoteType getQuoteTypeSample2() {
        return new QuoteType().id(2L).name("name2").description("description2");
    }

    public static QuoteType getQuoteTypeRandomSampleGenerator() {
        return new QuoteType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
