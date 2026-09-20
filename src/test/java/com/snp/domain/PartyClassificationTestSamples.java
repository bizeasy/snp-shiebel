package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PartyClassificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PartyClassification getPartyClassificationSample1() {
        return new PartyClassification().id(1L);
    }

    public static PartyClassification getPartyClassificationSample2() {
        return new PartyClassification().id(2L);
    }

    public static PartyClassification getPartyClassificationRandomSampleGenerator() {
        return new PartyClassification().id(longCount.incrementAndGet());
    }
}
