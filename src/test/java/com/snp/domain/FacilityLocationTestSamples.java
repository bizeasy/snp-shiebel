package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FacilityLocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static FacilityLocation getFacilityLocationSample1() {
        return new FacilityLocation()
            .id(1L)
            .locationSeqId("locationSeqId1")
            .areaId("areaId1")
            .aisleId("aisleId1")
            .sectionId("sectionId1")
            .levelId("levelId1")
            .positionId("positionId1");
    }

    public static FacilityLocation getFacilityLocationSample2() {
        return new FacilityLocation()
            .id(2L)
            .locationSeqId("locationSeqId2")
            .areaId("areaId2")
            .aisleId("aisleId2")
            .sectionId("sectionId2")
            .levelId("levelId2")
            .positionId("positionId2");
    }

    public static FacilityLocation getFacilityLocationRandomSampleGenerator() {
        return new FacilityLocation()
            .id(longCount.incrementAndGet())
            .locationSeqId(UUID.randomUUID().toString())
            .areaId(UUID.randomUUID().toString())
            .aisleId(UUID.randomUUID().toString())
            .sectionId(UUID.randomUUID().toString())
            .levelId(UUID.randomUUID().toString())
            .positionId(UUID.randomUUID().toString());
    }
}
