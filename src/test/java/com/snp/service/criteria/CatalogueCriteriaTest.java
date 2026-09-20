package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CatalogueCriteriaTest {

    @Test
    void newCatalogueCriteriaHasAllFiltersNullTest() {
        var catalogueCriteria = new CatalogueCriteria();
        assertThat(catalogueCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void catalogueCriteriaFluentMethodsCreatesFiltersTest() {
        var catalogueCriteria = new CatalogueCriteria();

        setAllFilters(catalogueCriteria);

        assertThat(catalogueCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void catalogueCriteriaCopyCreatesNullFilterTest() {
        var catalogueCriteria = new CatalogueCriteria();
        var copy = catalogueCriteria.copy();

        assertThat(catalogueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(catalogueCriteria)
        );
    }

    @Test
    void catalogueCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var catalogueCriteria = new CatalogueCriteria();
        setAllFilters(catalogueCriteria);

        var copy = catalogueCriteria.copy();

        assertThat(catalogueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(catalogueCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var catalogueCriteria = new CatalogueCriteria();

        assertThat(catalogueCriteria).hasToString("CatalogueCriteria{}");
    }

    private static void setAllFilters(CatalogueCriteria catalogueCriteria) {
        catalogueCriteria.id();
        catalogueCriteria.name();
        catalogueCriteria.description();
        catalogueCriteria.sequenceNo();
        catalogueCriteria.imagePath();
        catalogueCriteria.mobileImagePath();
        catalogueCriteria.altImage1();
        catalogueCriteria.altImage2();
        catalogueCriteria.altImage3();
        catalogueCriteria.bannerUrl();
        catalogueCriteria.isBanner();
        catalogueCriteria.distinct();
    }

    private static Condition<CatalogueCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getImagePath()) &&
                condition.apply(criteria.getMobileImagePath()) &&
                condition.apply(criteria.getAltImage1()) &&
                condition.apply(criteria.getAltImage2()) &&
                condition.apply(criteria.getAltImage3()) &&
                condition.apply(criteria.getBannerUrl()) &&
                condition.apply(criteria.getIsBanner()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CatalogueCriteria> copyFiltersAre(CatalogueCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getImagePath(), copy.getImagePath()) &&
                condition.apply(criteria.getMobileImagePath(), copy.getMobileImagePath()) &&
                condition.apply(criteria.getAltImage1(), copy.getAltImage1()) &&
                condition.apply(criteria.getAltImage2(), copy.getAltImage2()) &&
                condition.apply(criteria.getAltImage3(), copy.getAltImage3()) &&
                condition.apply(criteria.getBannerUrl(), copy.getBannerUrl()) &&
                condition.apply(criteria.getIsBanner(), copy.getIsBanner()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
