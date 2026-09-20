package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FileShareCriteriaTest {

    @Test
    void newFileShareCriteriaHasAllFiltersNullTest() {
        var fileShareCriteria = new FileShareCriteria();
        assertThat(fileShareCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void fileShareCriteriaFluentMethodsCreatesFiltersTest() {
        var fileShareCriteria = new FileShareCriteria();

        setAllFilters(fileShareCriteria);

        assertThat(fileShareCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void fileShareCriteriaCopyCreatesNullFilterTest() {
        var fileShareCriteria = new FileShareCriteria();
        var copy = fileShareCriteria.copy();

        assertThat(fileShareCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(fileShareCriteria)
        );
    }

    @Test
    void fileShareCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var fileShareCriteria = new FileShareCriteria();
        setAllFilters(fileShareCriteria);

        var copy = fileShareCriteria.copy();

        assertThat(fileShareCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(fileShareCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var fileShareCriteria = new FileShareCriteria();

        assertThat(fileShareCriteria).hasToString("FileShareCriteria{}");
    }

    private static void setAllFilters(FileShareCriteria fileShareCriteria) {
        fileShareCriteria.id();
        fileShareCriteria.permission();
        fileShareCriteria.sharedWithEmail();
        fileShareCriteria.sharedDate();
        fileShareCriteria.expiryDate();
        fileShareCriteria.accessToken();
        fileShareCriteria.isRevoked();
        fileShareCriteria.folderId();
        fileShareCriteria.attachmentId();
        fileShareCriteria.sharedWithId();
        fileShareCriteria.sharedById();
        fileShareCriteria.distinct();
    }

    private static Condition<FileShareCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPermission()) &&
                condition.apply(criteria.getSharedWithEmail()) &&
                condition.apply(criteria.getSharedDate()) &&
                condition.apply(criteria.getExpiryDate()) &&
                condition.apply(criteria.getAccessToken()) &&
                condition.apply(criteria.getIsRevoked()) &&
                condition.apply(criteria.getFolderId()) &&
                condition.apply(criteria.getAttachmentId()) &&
                condition.apply(criteria.getSharedWithId()) &&
                condition.apply(criteria.getSharedById()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FileShareCriteria> copyFiltersAre(FileShareCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPermission(), copy.getPermission()) &&
                condition.apply(criteria.getSharedWithEmail(), copy.getSharedWithEmail()) &&
                condition.apply(criteria.getSharedDate(), copy.getSharedDate()) &&
                condition.apply(criteria.getExpiryDate(), copy.getExpiryDate()) &&
                condition.apply(criteria.getAccessToken(), copy.getAccessToken()) &&
                condition.apply(criteria.getIsRevoked(), copy.getIsRevoked()) &&
                condition.apply(criteria.getFolderId(), copy.getFolderId()) &&
                condition.apply(criteria.getAttachmentId(), copy.getAttachmentId()) &&
                condition.apply(criteria.getSharedWithId(), copy.getSharedWithId()) &&
                condition.apply(criteria.getSharedById(), copy.getSharedById()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
