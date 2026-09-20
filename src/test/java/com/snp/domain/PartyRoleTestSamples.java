package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PartyRoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PartyRole getPartyRoleSample1() {
        return new PartyRole().id(1L);
    }

    public static PartyRole getPartyRoleSample2() {
        return new PartyRole().id(2L);
    }

    public static PartyRole getPartyRoleRandomSampleGenerator() {
        return new PartyRole().id(longCount.incrementAndGet());
    }
}
