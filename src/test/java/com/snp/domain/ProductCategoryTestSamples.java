package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ProductCategory getProductCategorySample1() {
        return new ProductCategory()
            .id(1L)
            .name("name1")
            .description("description1")
            .longDescription("longDescription1")
            .attribute("attribute1")
            .sequenceNo(1)
            .imageUrl("imageUrl1")
            .altImageUrl("altImageUrl1")
            .bannerUrl("bannerUrl1")
            .largeImageUrl("largeImageUrl1")
            .altLargeImageUrl("altLargeImageUrl1");
    }

    public static ProductCategory getProductCategorySample2() {
        return new ProductCategory()
            .id(2L)
            .name("name2")
            .description("description2")
            .longDescription("longDescription2")
            .attribute("attribute2")
            .sequenceNo(2)
            .imageUrl("imageUrl2")
            .altImageUrl("altImageUrl2")
            .bannerUrl("bannerUrl2")
            .largeImageUrl("largeImageUrl2")
            .altLargeImageUrl("altLargeImageUrl2");
    }

    public static ProductCategory getProductCategoryRandomSampleGenerator() {
        return new ProductCategory()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .longDescription(UUID.randomUUID().toString())
            .attribute(UUID.randomUUID().toString())
            .sequenceNo(intCount.incrementAndGet())
            .imageUrl(UUID.randomUUID().toString())
            .altImageUrl(UUID.randomUUID().toString())
            .bannerUrl(UUID.randomUUID().toString())
            .largeImageUrl(UUID.randomUUID().toString())
            .altLargeImageUrl(UUID.randomUUID().toString());
    }
}
