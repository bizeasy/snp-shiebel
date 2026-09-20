package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomTimePeriodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CustomTimePeriod getCustomTimePeriodSample1() {
        return new CustomTimePeriod().id(1L).name("name1");
    }

    public static CustomTimePeriod getCustomTimePeriodSample2() {
        return new CustomTimePeriod().id(2L).name("name2");
    }

    public static CustomTimePeriod getCustomTimePeriodRandomSampleGenerator() {
        return new CustomTimePeriod().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
