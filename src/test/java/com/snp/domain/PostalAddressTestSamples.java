package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PostalAddressTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PostalAddress getPostalAddressSample1() {
        return new PostalAddress()
            .id(1L)
            .toName("toName1")
            .address1("address11")
            .address2("address21")
            .city("city1")
            .landmark("landmark1")
            .postalCode("postalCode1")
            .customAddressType("customAddressType1")
            .stateStr("stateStr1")
            .countryStr("countryStr1")
            .note("note1")
            .directions("directions1");
    }

    public static PostalAddress getPostalAddressSample2() {
        return new PostalAddress()
            .id(2L)
            .toName("toName2")
            .address1("address12")
            .address2("address22")
            .city("city2")
            .landmark("landmark2")
            .postalCode("postalCode2")
            .customAddressType("customAddressType2")
            .stateStr("stateStr2")
            .countryStr("countryStr2")
            .note("note2")
            .directions("directions2");
    }

    public static PostalAddress getPostalAddressRandomSampleGenerator() {
        return new PostalAddress()
            .id(longCount.incrementAndGet())
            .toName(UUID.randomUUID().toString())
            .address1(UUID.randomUUID().toString())
            .address2(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .landmark(UUID.randomUUID().toString())
            .postalCode(UUID.randomUUID().toString())
            .customAddressType(UUID.randomUUID().toString())
            .stateStr(UUID.randomUUID().toString())
            .countryStr(UUID.randomUUID().toString())
            .note(UUID.randomUUID().toString())
            .directions(UUID.randomUUID().toString());
    }
}
