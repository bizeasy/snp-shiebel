package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TransactionCategory getTransactionCategorySample1() {
        return new TransactionCategory().id(1L).name("name1").description("description1");
    }

    public static TransactionCategory getTransactionCategorySample2() {
        return new TransactionCategory().id(2L).name("name2").description("description2");
    }

    public static TransactionCategory getTransactionCategoryRandomSampleGenerator() {
        return new TransactionCategory()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
