package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuoteItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static QuoteItem getQuoteItemSample1() {
        return new QuoteItem().id(1L).sequenceNo(1).leadTimeDays(1).comments("comments1");
    }

    public static QuoteItem getQuoteItemSample2() {
        return new QuoteItem().id(2L).sequenceNo(2).leadTimeDays(2).comments("comments2");
    }

    public static QuoteItem getQuoteItemRandomSampleGenerator() {
        return new QuoteItem()
            .id(longCount.incrementAndGet())
            .sequenceNo(intCount.incrementAndGet())
            .leadTimeDays(intCount.incrementAndGet())
            .comments(UUID.randomUUID().toString());
    }
}
