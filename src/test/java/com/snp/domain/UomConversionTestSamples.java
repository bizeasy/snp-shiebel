package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UomConversionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static UomConversion getUomConversionSample1() {
        return new UomConversion().id(1L).name("name1");
    }

    public static UomConversion getUomConversionSample2() {
        return new UomConversion().id(2L).name("name2");
    }

    public static UomConversion getUomConversionRandomSampleGenerator() {
        return new UomConversion().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
