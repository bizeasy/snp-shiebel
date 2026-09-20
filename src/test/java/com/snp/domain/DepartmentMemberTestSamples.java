package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class DepartmentMemberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static DepartmentMember getDepartmentMemberSample1() {
        return new DepartmentMember().id(1L);
    }

    public static DepartmentMember getDepartmentMemberSample2() {
        return new DepartmentMember().id(2L);
    }

    public static DepartmentMember getDepartmentMemberRandomSampleGenerator() {
        return new DepartmentMember().id(longCount.incrementAndGet());
    }
}
