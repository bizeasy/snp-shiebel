package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FacilityCriteriaTest {

    @Test
    void newFacilityCriteriaHasAllFiltersNullTest() {
        var facilityCriteria = new FacilityCriteria();
        assertThat(facilityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void facilityCriteriaFluentMethodsCreatesFiltersTest() {
        var facilityCriteria = new FacilityCriteria();

        setAllFilters(facilityCriteria);

        assertThat(facilityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void facilityCriteriaCopyCreatesNullFilterTest() {
        var facilityCriteria = new FacilityCriteria();
        var copy = facilityCriteria.copy();

        assertThat(facilityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityCriteria)
        );
    }

    @Test
    void facilityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var facilityCriteria = new FacilityCriteria();
        setAllFilters(facilityCriteria);

        var copy = facilityCriteria.copy();

        assertThat(facilityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var facilityCriteria = new FacilityCriteria();

        assertThat(facilityCriteria).hasToString("FacilityCriteria{}");
    }

    private static void setAllFilters(FacilityCriteria facilityCriteria) {
        facilityCriteria.id();
        facilityCriteria.name();
        facilityCriteria.description();
        facilityCriteria.facilitySize();
        facilityCriteria.facilityTypeId();
        facilityCriteria.parentFacilityId();
        facilityCriteria.ownerPartyId();
        facilityCriteria.facilityGroupId();
        facilityCriteria.sizeUomId();
        facilityCriteria.geoPointId();
        facilityCriteria.inventoryItemTypeId();
        facilityCriteria.productStoreId();
        facilityCriteria.distinct();
    }

    private static Condition<FacilityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getFacilitySize()) &&
                condition.apply(criteria.getFacilityTypeId()) &&
                condition.apply(criteria.getParentFacilityId()) &&
                condition.apply(criteria.getOwnerPartyId()) &&
                condition.apply(criteria.getFacilityGroupId()) &&
                condition.apply(criteria.getSizeUomId()) &&
                condition.apply(criteria.getGeoPointId()) &&
                condition.apply(criteria.getInventoryItemTypeId()) &&
                condition.apply(criteria.getProductStoreId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FacilityCriteria> copyFiltersAre(FacilityCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getFacilitySize(), copy.getFacilitySize()) &&
                condition.apply(criteria.getFacilityTypeId(), copy.getFacilityTypeId()) &&
                condition.apply(criteria.getParentFacilityId(), copy.getParentFacilityId()) &&
                condition.apply(criteria.getOwnerPartyId(), copy.getOwnerPartyId()) &&
                condition.apply(criteria.getFacilityGroupId(), copy.getFacilityGroupId()) &&
                condition.apply(criteria.getSizeUomId(), copy.getSizeUomId()) &&
                condition.apply(criteria.getGeoPointId(), copy.getGeoPointId()) &&
                condition.apply(criteria.getInventoryItemTypeId(), copy.getInventoryItemTypeId()) &&
                condition.apply(criteria.getProductStoreId(), copy.getProductStoreId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
