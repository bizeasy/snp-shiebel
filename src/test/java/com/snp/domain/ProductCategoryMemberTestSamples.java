package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductCategoryMemberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ProductCategoryMember getProductCategoryMemberSample1() {
        return new ProductCategoryMember().id(1L).sequenceNo(1);
    }

    public static ProductCategoryMember getProductCategoryMemberSample2() {
        return new ProductCategoryMember().id(2L).sequenceNo(2);
    }

    public static ProductCategoryMember getProductCategoryMemberRandomSampleGenerator() {
        return new ProductCategoryMember().id(longCount.incrementAndGet()).sequenceNo(intCount.incrementAndGet());
    }
}
