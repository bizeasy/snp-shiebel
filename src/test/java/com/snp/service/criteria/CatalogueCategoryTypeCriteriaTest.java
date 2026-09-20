package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CatalogueCategoryTypeCriteriaTest {

    @Test
    void newCatalogueCategoryTypeCriteriaHasAllFiltersNullTest() {
        var catalogueCategoryTypeCriteria = new CatalogueCategoryTypeCriteria();
        assertThat(catalogueCategoryTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void catalogueCategoryTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var catalogueCategoryTypeCriteria = new CatalogueCategoryTypeCriteria();

        setAllFilters(catalogueCategoryTypeCriteria);

        assertThat(catalogueCategoryTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void catalogueCategoryTypeCriteriaCopyCreatesNullFilterTest() {
        var catalogueCategoryTypeCriteria = new CatalogueCategoryTypeCriteria();
        var copy = catalogueCategoryTypeCriteria.copy();

        assertThat(catalogueCategoryTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(catalogueCategoryTypeCriteria)
        );
    }

    @Test
    void catalogueCategoryTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var catalogueCategoryTypeCriteria = new CatalogueCategoryTypeCriteria();
        setAllFilters(catalogueCategoryTypeCriteria);

        var copy = catalogueCategoryTypeCriteria.copy();

        assertThat(catalogueCategoryTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(catalogueCategoryTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var catalogueCategoryTypeCriteria = new CatalogueCategoryTypeCriteria();

        assertThat(catalogueCategoryTypeCriteria).hasToString("CatalogueCategoryTypeCriteria{}");
    }

    private static void setAllFilters(CatalogueCategoryTypeCriteria catalogueCategoryTypeCriteria) {
        catalogueCategoryTypeCriteria.id();
        catalogueCategoryTypeCriteria.name();
        catalogueCategoryTypeCriteria.description();
        catalogueCategoryTypeCriteria.distinct();
    }

    private static Condition<CatalogueCategoryTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CatalogueCategoryTypeCriteria> copyFiltersAre(
        CatalogueCategoryTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
