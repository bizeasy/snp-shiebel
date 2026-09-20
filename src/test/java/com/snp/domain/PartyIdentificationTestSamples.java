package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PartyIdentificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PartyIdentification getPartyIdentificationSample1() {
        return new PartyIdentification().id(1L).idValue("idValue1");
    }

    public static PartyIdentification getPartyIdentificationSample2() {
        return new PartyIdentification().id(2L).idValue("idValue2");
    }

    public static PartyIdentification getPartyIdentificationRandomSampleGenerator() {
        return new PartyIdentification().id(longCount.incrementAndGet()).idValue(UUID.randomUUID().toString());
    }
}
