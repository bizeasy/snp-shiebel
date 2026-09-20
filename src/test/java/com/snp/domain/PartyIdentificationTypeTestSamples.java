package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PartyIdentificationTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PartyIdentificationType getPartyIdentificationTypeSample1() {
        return new PartyIdentificationType().id(1L).name("name1").description("description1");
    }

    public static PartyIdentificationType getPartyIdentificationTypeSample2() {
        return new PartyIdentificationType().id(2L).name("name2").description("description2");
    }

    public static PartyIdentificationType getPartyIdentificationTypeRandomSampleGenerator() {
        return new PartyIdentificationType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
