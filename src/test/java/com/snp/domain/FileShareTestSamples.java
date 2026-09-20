package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FileShareTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static FileShare getFileShareSample1() {
        return new FileShare().id(1L).sharedWithEmail("sharedWithEmail1").accessToken("accessToken1");
    }

    public static FileShare getFileShareSample2() {
        return new FileShare().id(2L).sharedWithEmail("sharedWithEmail2").accessToken("accessToken2");
    }

    public static FileShare getFileShareRandomSampleGenerator() {
        return new FileShare()
            .id(longCount.incrementAndGet())
            .sharedWithEmail(UUID.randomUUID().toString())
            .accessToken(UUID.randomUUID().toString());
    }
}
