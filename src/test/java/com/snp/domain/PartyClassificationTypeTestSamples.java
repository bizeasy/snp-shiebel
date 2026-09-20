package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PartyClassificationTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PartyClassificationType getPartyClassificationTypeSample1() {
        return new PartyClassificationType().id(1L).name("name1").description("description1");
    }

    public static PartyClassificationType getPartyClassificationTypeSample2() {
        return new PartyClassificationType().id(2L).name("name2").description("description2");
    }

    public static PartyClassificationType getPartyClassificationTypeRandomSampleGenerator() {
        return new PartyClassificationType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
