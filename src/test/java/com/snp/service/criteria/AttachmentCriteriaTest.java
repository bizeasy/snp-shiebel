package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AttachmentCriteriaTest {

    @Test
    void newAttachmentCriteriaHasAllFiltersNullTest() {
        var attachmentCriteria = new AttachmentCriteria();
        assertThat(attachmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void attachmentCriteriaFluentMethodsCreatesFiltersTest() {
        var attachmentCriteria = new AttachmentCriteria();

        setAllFilters(attachmentCriteria);

        assertThat(attachmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void attachmentCriteriaCopyCreatesNullFilterTest() {
        var attachmentCriteria = new AttachmentCriteria();
        var copy = attachmentCriteria.copy();

        assertThat(attachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(attachmentCriteria)
        );
    }

    @Test
    void attachmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var attachmentCriteria = new AttachmentCriteria();
        setAllFilters(attachmentCriteria);

        var copy = attachmentCriteria.copy();

        assertThat(attachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(attachmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var attachmentCriteria = new AttachmentCriteria();

        assertThat(attachmentCriteria).hasToString("AttachmentCriteria{}");
    }

    private static void setAllFilters(AttachmentCriteria attachmentCriteria) {
        attachmentCriteria.id();
        attachmentCriteria.name();
        attachmentCriteria.attachmentUrl();
        attachmentCriteria.mimeType();
        attachmentCriteria.fileSize();
        attachmentCriteria.uploadedDate();
        attachmentCriteria.isTrashed();
        attachmentCriteria.checksum();
        attachmentCriteria.contentTypeId();
        attachmentCriteria.folderId();
        attachmentCriteria.ownerId();
        attachmentCriteria.distinct();
    }

    private static Condition<AttachmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAttachmentUrl()) &&
                condition.apply(criteria.getMimeType()) &&
                condition.apply(criteria.getFileSize()) &&
                condition.apply(criteria.getUploadedDate()) &&
                condition.apply(criteria.getIsTrashed()) &&
                condition.apply(criteria.getChecksum()) &&
                condition.apply(criteria.getContentTypeId()) &&
                condition.apply(criteria.getFolderId()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AttachmentCriteria> copyFiltersAre(AttachmentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAttachmentUrl(), copy.getAttachmentUrl()) &&
                condition.apply(criteria.getMimeType(), copy.getMimeType()) &&
                condition.apply(criteria.getFileSize(), copy.getFileSize()) &&
                condition.apply(criteria.getUploadedDate(), copy.getUploadedDate()) &&
                condition.apply(criteria.getIsTrashed(), copy.getIsTrashed()) &&
                condition.apply(criteria.getChecksum(), copy.getChecksum()) &&
                condition.apply(criteria.getContentTypeId(), copy.getContentTypeId()) &&
                condition.apply(criteria.getFolderId(), copy.getFolderId()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
