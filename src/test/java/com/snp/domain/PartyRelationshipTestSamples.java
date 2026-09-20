package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PartyRelationshipTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PartyRelationship getPartyRelationshipSample1() {
        return new PartyRelationship().id(1L).relationshipName("relationshipName1");
    }

    public static PartyRelationship getPartyRelationshipSample2() {
        return new PartyRelationship().id(2L).relationshipName("relationshipName2");
    }

    public static PartyRelationship getPartyRelationshipRandomSampleGenerator() {
        return new PartyRelationship().id(longCount.incrementAndGet()).relationshipName(UUID.randomUUID().toString());
    }
}
