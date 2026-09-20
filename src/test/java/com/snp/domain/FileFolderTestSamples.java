package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FileFolderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static FileFolder getFileFolderSample1() {
        return new FileFolder().id(1L).name("name1").path("path1");
    }

    public static FileFolder getFileFolderSample2() {
        return new FileFolder().id(2L).name("name2").path("path2");
    }

    public static FileFolder getFileFolderRandomSampleGenerator() {
        return new FileFolder().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).path(UUID.randomUUID().toString());
    }
}
