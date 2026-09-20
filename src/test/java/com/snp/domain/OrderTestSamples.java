package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Order getOrderSample1() {
        return new Order()
            .id(1L)
            .name("name1")
            .orderRefNo("orderRefNo1")
            .externalId("externalId1")
            .priority(1)
            .projectName("projectName1")
            .version(1)
            .versionRef("versionRef1")
            .remarks("remarks1")
            .attr1("attr11")
            .attr2("attr21")
            .attr3("attr31");
    }

    public static Order getOrderSample2() {
        return new Order()
            .id(2L)
            .name("name2")
            .orderRefNo("orderRefNo2")
            .externalId("externalId2")
            .priority(2)
            .projectName("projectName2")
            .version(2)
            .versionRef("versionRef2")
            .remarks("remarks2")
            .attr1("attr12")
            .attr2("attr22")
            .attr3("attr32");
    }

    public static Order getOrderRandomSampleGenerator() {
        return new Order()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .orderRefNo(UUID.randomUUID().toString())
            .externalId(UUID.randomUUID().toString())
            .priority(intCount.incrementAndGet())
            .projectName(UUID.randomUUID().toString())
            .version(intCount.incrementAndGet())
            .versionRef(UUID.randomUUID().toString())
            .remarks(UUID.randomUUID().toString())
            .attr1(UUID.randomUUID().toString())
            .attr2(UUID.randomUUID().toString())
            .attr3(UUID.randomUUID().toString());
    }
}
