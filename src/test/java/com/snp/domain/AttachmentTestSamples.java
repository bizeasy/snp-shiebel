package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AttachmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Attachment getAttachmentSample1() {
        return new Attachment()
            .id(1L)
            .name("name1")
            .attachmentUrl("attachmentUrl1")
            .mimeType("mimeType1")
            .fileSize(1L)
            .checksum("checksum1");
    }

    public static Attachment getAttachmentSample2() {
        return new Attachment()
            .id(2L)
            .name("name2")
            .attachmentUrl("attachmentUrl2")
            .mimeType("mimeType2")
            .fileSize(2L)
            .checksum("checksum2");
    }

    public static Attachment getAttachmentRandomSampleGenerator() {
        return new Attachment()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .attachmentUrl(UUID.randomUUID().toString())
            .mimeType(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .checksum(UUID.randomUUID().toString());
    }
}
