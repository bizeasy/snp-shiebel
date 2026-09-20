package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FileFolderCriteriaTest {

    @Test
    void newFileFolderCriteriaHasAllFiltersNullTest() {
        var fileFolderCriteria = new FileFolderCriteria();
        assertThat(fileFolderCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void fileFolderCriteriaFluentMethodsCreatesFiltersTest() {
        var fileFolderCriteria = new FileFolderCriteria();

        setAllFilters(fileFolderCriteria);

        assertThat(fileFolderCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void fileFolderCriteriaCopyCreatesNullFilterTest() {
        var fileFolderCriteria = new FileFolderCriteria();
        var copy = fileFolderCriteria.copy();

        assertThat(fileFolderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(fileFolderCriteria)
        );
    }

    @Test
    void fileFolderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var fileFolderCriteria = new FileFolderCriteria();
        setAllFilters(fileFolderCriteria);

        var copy = fileFolderCriteria.copy();

        assertThat(fileFolderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(fileFolderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var fileFolderCriteria = new FileFolderCriteria();

        assertThat(fileFolderCriteria).hasToString("FileFolderCriteria{}");
    }

    private static void setAllFilters(FileFolderCriteria fileFolderCriteria) {
        fileFolderCriteria.id();
        fileFolderCriteria.name();
        fileFolderCriteria.path();
        fileFolderCriteria.isSystemFolder();
        fileFolderCriteria.isTrashed();
        fileFolderCriteria.createdDate();
        fileFolderCriteria.parentId();
        fileFolderCriteria.ownerId();
        fileFolderCriteria.distinct();
    }

    private static Condition<FileFolderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPath()) &&
                condition.apply(criteria.getIsSystemFolder()) &&
                condition.apply(criteria.getIsTrashed()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FileFolderCriteria> copyFiltersAre(FileFolderCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPath(), copy.getPath()) &&
                condition.apply(criteria.getIsSystemFolder(), copy.getIsSystemFolder()) &&
                condition.apply(criteria.getIsTrashed(), copy.getIsTrashed()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
