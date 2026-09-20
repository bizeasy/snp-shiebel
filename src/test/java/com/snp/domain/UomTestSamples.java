package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UomTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Uom getUomSample1() {
        return new Uom().id(1L).name("name1").description("description1").sequenceNo(1).abbreviation("abbreviation1");
    }

    public static Uom getUomSample2() {
        return new Uom().id(2L).name("name2").description("description2").sequenceNo(2).abbreviation("abbreviation2");
    }

    public static Uom getUomRandomSampleGenerator() {
        return new Uom()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .sequenceNo(intCount.incrementAndGet())
            .abbreviation(UUID.randomUUID().toString());
    }
}
