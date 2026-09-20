package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GeoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Geo getGeoSample1() {
        return new Geo().id(1L).name("name1").code("code1").abbreviation("abbreviation1");
    }

    public static Geo getGeoSample2() {
        return new Geo().id(2L).name("name2").code("code2").abbreviation("abbreviation2");
    }

    public static Geo getGeoRandomSampleGenerator() {
        return new Geo()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .abbreviation(UUID.randomUUID().toString());
    }
}
