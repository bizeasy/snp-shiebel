package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FacilityPartyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static FacilityParty getFacilityPartySample1() {
        return new FacilityParty().id(1L);
    }

    public static FacilityParty getFacilityPartySample2() {
        return new FacilityParty().id(2L);
    }

    public static FacilityParty getFacilityPartyRandomSampleGenerator() {
        return new FacilityParty().id(longCount.incrementAndGet());
    }
}
