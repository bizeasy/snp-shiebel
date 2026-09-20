package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Product getProductSample1() {
        return new Product()
            .id(1L)
            .name("name1")
            .internalName("internalName1")
            .brandName("brandName1")
            .variant("variant1")
            .sku("sku1")
            .piecesIncluded(1)
            .smallImageUrl("smallImageUrl1")
            .mediumImageUrl("mediumImageUrl1")
            .largeImageUrl("largeImageUrl1")
            .detailImageUrl("detailImageUrl1")
            .originalImageUrl("originalImageUrl1")
            .altImageUrl1("altImageUrl11")
            .altImageUrl2("altImageUrl21")
            .altImageUrl3("altImageUrl31")
            .altImageUrl4("altImageUrl41")
            .altImageUrl5("altImageUrl51")
            .altImageUrl6("altImageUrl61")
            .videoUrl("videoUrl1")
            .description("description1")
            .longDescription("longDescription1")
            .bannerUrl("bannerUrl1");
    }

    public static Product getProductSample2() {
        return new Product()
            .id(2L)
            .name("name2")
            .internalName("internalName2")
            .brandName("brandName2")
            .variant("variant2")
            .sku("sku2")
            .piecesIncluded(2)
            .smallImageUrl("smallImageUrl2")
            .mediumImageUrl("mediumImageUrl2")
            .largeImageUrl("largeImageUrl2")
            .detailImageUrl("detailImageUrl2")
            .originalImageUrl("originalImageUrl2")
            .altImageUrl1("altImageUrl12")
            .altImageUrl2("altImageUrl22")
            .altImageUrl3("altImageUrl32")
            .altImageUrl4("altImageUrl42")
            .altImageUrl5("altImageUrl52")
            .altImageUrl6("altImageUrl62")
            .videoUrl("videoUrl2")
            .description("description2")
            .longDescription("longDescription2")
            .bannerUrl("bannerUrl2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .internalName(UUID.randomUUID().toString())
            .brandName(UUID.randomUUID().toString())
            .variant(UUID.randomUUID().toString())
            .sku(UUID.randomUUID().toString())
            .piecesIncluded(intCount.incrementAndGet())
            .smallImageUrl(UUID.randomUUID().toString())
            .mediumImageUrl(UUID.randomUUID().toString())
            .largeImageUrl(UUID.randomUUID().toString())
            .detailImageUrl(UUID.randomUUID().toString())
            .originalImageUrl(UUID.randomUUID().toString())
            .altImageUrl1(UUID.randomUUID().toString())
            .altImageUrl2(UUID.randomUUID().toString())
            .altImageUrl3(UUID.randomUUID().toString())
            .altImageUrl4(UUID.randomUUID().toString())
            .altImageUrl5(UUID.randomUUID().toString())
            .altImageUrl6(UUID.randomUUID().toString())
            .videoUrl(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .longDescription(UUID.randomUUID().toString())
            .bannerUrl(UUID.randomUUID().toString());
    }
}
