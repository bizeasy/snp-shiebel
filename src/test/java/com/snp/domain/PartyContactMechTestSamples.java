package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PartyContactMechTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PartyContactMech getPartyContactMechSample1() {
        return new PartyContactMech().id(1L);
    }

    public static PartyContactMech getPartyContactMechSample2() {
        return new PartyContactMech().id(2L);
    }

    public static PartyContactMech getPartyContactMechRandomSampleGenerator() {
        return new PartyContactMech().id(longCount.incrementAndGet());
    }
}
