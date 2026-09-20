package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CatalogueCategoryCriteriaTest {

    @Test
    void newCatalogueCategoryCriteriaHasAllFiltersNullTest() {
        var catalogueCategoryCriteria = new CatalogueCategoryCriteria();
        assertThat(catalogueCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void catalogueCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var catalogueCategoryCriteria = new CatalogueCategoryCriteria();

        setAllFilters(catalogueCategoryCriteria);

        assertThat(catalogueCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void catalogueCategoryCriteriaCopyCreatesNullFilterTest() {
        var catalogueCategoryCriteria = new CatalogueCategoryCriteria();
        var copy = catalogueCategoryCriteria.copy();

        assertThat(catalogueCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(catalogueCategoryCriteria)
        );
    }

    @Test
    void catalogueCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var catalogueCategoryCriteria = new CatalogueCategoryCriteria();
        setAllFilters(catalogueCategoryCriteria);

        var copy = catalogueCategoryCriteria.copy();

        assertThat(catalogueCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(catalogueCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var catalogueCategoryCriteria = new CatalogueCategoryCriteria();

        assertThat(catalogueCategoryCriteria).hasToString("CatalogueCategoryCriteria{}");
    }

    private static void setAllFilters(CatalogueCategoryCriteria catalogueCategoryCriteria) {
        catalogueCategoryCriteria.id();
        catalogueCategoryCriteria.sequenceNo();
        catalogueCategoryCriteria.catalogueId();
        catalogueCategoryCriteria.productCategoryId();
        catalogueCategoryCriteria.catalogueCategoryTypeId();
        catalogueCategoryCriteria.distinct();
    }

    private static Condition<CatalogueCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getCatalogueId()) &&
                condition.apply(criteria.getProductCategoryId()) &&
                condition.apply(criteria.getCatalogueCategoryTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CatalogueCategoryCriteria> copyFiltersAre(
        CatalogueCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getCatalogueId(), copy.getCatalogueId()) &&
                condition.apply(criteria.getProductCategoryId(), copy.getProductCategoryId()) &&
                condition.apply(criteria.getCatalogueCategoryTypeId(), copy.getCatalogueCategoryTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
