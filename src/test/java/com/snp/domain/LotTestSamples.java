package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class LotTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Lot getLotSample1() {
        return new Lot().id(1L);
    }

    public static Lot getLotSample2() {
        return new Lot().id(2L);
    }

    public static Lot getLotRandomSampleGenerator() {
        return new Lot().id(longCount.incrementAndGet());
    }
}
