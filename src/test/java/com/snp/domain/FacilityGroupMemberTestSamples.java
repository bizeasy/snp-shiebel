package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FacilityGroupMemberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static FacilityGroupMember getFacilityGroupMemberSample1() {
        return new FacilityGroupMember().id(1L).sequenceNo(1);
    }

    public static FacilityGroupMember getFacilityGroupMemberSample2() {
        return new FacilityGroupMember().id(2L).sequenceNo(2);
    }

    public static FacilityGroupMember getFacilityGroupMemberRandomSampleGenerator() {
        return new FacilityGroupMember().id(longCount.incrementAndGet()).sequenceNo(intCount.incrementAndGet());
    }
}
