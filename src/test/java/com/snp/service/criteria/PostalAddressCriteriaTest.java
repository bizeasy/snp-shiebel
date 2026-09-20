package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PostalAddressCriteriaTest {

    @Test
    void newPostalAddressCriteriaHasAllFiltersNullTest() {
        var postalAddressCriteria = new PostalAddressCriteria();
        assertThat(postalAddressCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void postalAddressCriteriaFluentMethodsCreatesFiltersTest() {
        var postalAddressCriteria = new PostalAddressCriteria();

        setAllFilters(postalAddressCriteria);

        assertThat(postalAddressCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void postalAddressCriteriaCopyCreatesNullFilterTest() {
        var postalAddressCriteria = new PostalAddressCriteria();
        var copy = postalAddressCriteria.copy();

        assertThat(postalAddressCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(postalAddressCriteria)
        );
    }

    @Test
    void postalAddressCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var postalAddressCriteria = new PostalAddressCriteria();
        setAllFilters(postalAddressCriteria);

        var copy = postalAddressCriteria.copy();

        assertThat(postalAddressCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(postalAddressCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var postalAddressCriteria = new PostalAddressCriteria();

        assertThat(postalAddressCriteria).hasToString("PostalAddressCriteria{}");
    }

    private static void setAllFilters(PostalAddressCriteria postalAddressCriteria) {
        postalAddressCriteria.id();
        postalAddressCriteria.toName();
        postalAddressCriteria.address1();
        postalAddressCriteria.address2();
        postalAddressCriteria.city();
        postalAddressCriteria.landmark();
        postalAddressCriteria.postalCode();
        postalAddressCriteria.isDefault();
        postalAddressCriteria.customAddressType();
        postalAddressCriteria.stateStr();
        postalAddressCriteria.countryStr();
        postalAddressCriteria.isIndianAddress();
        postalAddressCriteria.note();
        postalAddressCriteria.directions();
        postalAddressCriteria.contactMechId();
        postalAddressCriteria.stateId();
        postalAddressCriteria.pincodeId();
        postalAddressCriteria.countryId();
        postalAddressCriteria.geoPointId();
        postalAddressCriteria.distinct();
    }

    private static Condition<PostalAddressCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getToName()) &&
                condition.apply(criteria.getAddress1()) &&
                condition.apply(criteria.getAddress2()) &&
                condition.apply(criteria.getCity()) &&
                condition.apply(criteria.getLandmark()) &&
                condition.apply(criteria.getPostalCode()) &&
                condition.apply(criteria.getIsDefault()) &&
                condition.apply(criteria.getCustomAddressType()) &&
                condition.apply(criteria.getStateStr()) &&
                condition.apply(criteria.getCountryStr()) &&
                condition.apply(criteria.getIsIndianAddress()) &&
                condition.apply(criteria.getNote()) &&
                condition.apply(criteria.getDirections()) &&
                condition.apply(criteria.getContactMechId()) &&
                condition.apply(criteria.getStateId()) &&
                condition.apply(criteria.getPincodeId()) &&
                condition.apply(criteria.getCountryId()) &&
                condition.apply(criteria.getGeoPointId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PostalAddressCriteria> copyFiltersAre(
        PostalAddressCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getToName(), copy.getToName()) &&
                condition.apply(criteria.getAddress1(), copy.getAddress1()) &&
                condition.apply(criteria.getAddress2(), copy.getAddress2()) &&
                condition.apply(criteria.getCity(), copy.getCity()) &&
                condition.apply(criteria.getLandmark(), copy.getLandmark()) &&
                condition.apply(criteria.getPostalCode(), copy.getPostalCode()) &&
                condition.apply(criteria.getIsDefault(), copy.getIsDefault()) &&
                condition.apply(criteria.getCustomAddressType(), copy.getCustomAddressType()) &&
                condition.apply(criteria.getStateStr(), copy.getStateStr()) &&
                condition.apply(criteria.getCountryStr(), copy.getCountryStr()) &&
                condition.apply(criteria.getIsIndianAddress(), copy.getIsIndianAddress()) &&
                condition.apply(criteria.getNote(), copy.getNote()) &&
                condition.apply(criteria.getDirections(), copy.getDirections()) &&
                condition.apply(criteria.getContactMechId(), copy.getContactMechId()) &&
                condition.apply(criteria.getStateId(), copy.getStateId()) &&
                condition.apply(criteria.getPincodeId(), copy.getPincodeId()) &&
                condition.apply(criteria.getCountryId(), copy.getCountryId()) &&
                condition.apply(criteria.getGeoPointId(), copy.getGeoPointId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
