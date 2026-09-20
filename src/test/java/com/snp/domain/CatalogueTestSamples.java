package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CatalogueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Catalogue getCatalogueSample1() {
        return new Catalogue()
            .id(1L)
            .name("name1")
            .description("description1")
            .sequenceNo(1)
            .imagePath("imagePath1")
            .mobileImagePath("mobileImagePath1")
            .altImage1("altImage11")
            .altImage2("altImage21")
            .altImage3("altImage31")
            .bannerUrl("bannerUrl1");
    }

    public static Catalogue getCatalogueSample2() {
        return new Catalogue()
            .id(2L)
            .name("name2")
            .description("description2")
            .sequenceNo(2)
            .imagePath("imagePath2")
            .mobileImagePath("mobileImagePath2")
            .altImage1("altImage12")
            .altImage2("altImage22")
            .altImage3("altImage32")
            .bannerUrl("bannerUrl2");
    }

    public static Catalogue getCatalogueRandomSampleGenerator() {
        return new Catalogue()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .sequenceNo(intCount.incrementAndGet())
            .imagePath(UUID.randomUUID().toString())
            .mobileImagePath(UUID.randomUUID().toString())
            .altImage1(UUID.randomUUID().toString())
            .altImage2(UUID.randomUUID().toString())
            .altImage3(UUID.randomUUID().toString())
            .bannerUrl(UUID.randomUUID().toString());
    }
}
