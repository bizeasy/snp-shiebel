package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PartyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Party getPartySample1() {
        return new Party()
            .id(1L)
            .organisationName("organisationName1")
            .organisationShortName("organisationShortName1")
            .salutation("salutation1")
            .firstName("firstName1")
            .middleName("middleName1")
            .lastName("lastName1")
            .primaryPhone("primaryPhone1")
            .primaryEmail("primaryEmail1")
            .logoImageUrl("logoImageUrl1")
            .profileImageUrl("profileImageUrl1");
    }

    public static Party getPartySample2() {
        return new Party()
            .id(2L)
            .organisationName("organisationName2")
            .organisationShortName("organisationShortName2")
            .salutation("salutation2")
            .firstName("firstName2")
            .middleName("middleName2")
            .lastName("lastName2")
            .primaryPhone("primaryPhone2")
            .primaryEmail("primaryEmail2")
            .logoImageUrl("logoImageUrl2")
            .profileImageUrl("profileImageUrl2");
    }

    public static Party getPartyRandomSampleGenerator() {
        return new Party()
            .id(longCount.incrementAndGet())
            .organisationName(UUID.randomUUID().toString())
            .organisationShortName(UUID.randomUUID().toString())
            .salutation(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .middleName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .primaryPhone(UUID.randomUUID().toString())
            .primaryEmail(UUID.randomUUID().toString())
            .logoImageUrl(UUID.randomUUID().toString())
            .profileImageUrl(UUID.randomUUID().toString());
    }
}
