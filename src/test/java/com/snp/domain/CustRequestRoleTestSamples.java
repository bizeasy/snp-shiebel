package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CustRequestRoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CustRequestRole getCustRequestRoleSample1() {
        return new CustRequestRole().id(1L);
    }

    public static CustRequestRole getCustRequestRoleSample2() {
        return new CustRequestRole().id(2L);
    }

    public static CustRequestRole getCustRequestRoleRandomSampleGenerator() {
        return new CustRequestRole().id(longCount.incrementAndGet());
    }
}
