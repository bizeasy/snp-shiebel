package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class QuoteRoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static QuoteRole getQuoteRoleSample1() {
        return new QuoteRole().id(1L);
    }

    public static QuoteRole getQuoteRoleSample2() {
        return new QuoteRole().id(2L);
    }

    public static QuoteRole getQuoteRoleRandomSampleGenerator() {
        return new QuoteRole().id(longCount.incrementAndGet());
    }
}
