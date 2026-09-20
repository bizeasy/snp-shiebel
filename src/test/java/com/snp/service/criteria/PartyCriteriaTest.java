package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PartyCriteriaTest {

    @Test
    void newPartyCriteriaHasAllFiltersNullTest() {
        var partyCriteria = new PartyCriteria();
        assertThat(partyCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void partyCriteriaFluentMethodsCreatesFiltersTest() {
        var partyCriteria = new PartyCriteria();

        setAllFilters(partyCriteria);

        assertThat(partyCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void partyCriteriaCopyCreatesNullFilterTest() {
        var partyCriteria = new PartyCriteria();
        var copy = partyCriteria.copy();

        assertThat(partyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(partyCriteria)
        );
    }

    @Test
    void partyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var partyCriteria = new PartyCriteria();
        setAllFilters(partyCriteria);

        var copy = partyCriteria.copy();

        assertThat(partyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(partyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var partyCriteria = new PartyCriteria();

        assertThat(partyCriteria).hasToString("PartyCriteria{}");
    }

    private static void setAllFilters(PartyCriteria partyCriteria) {
        partyCriteria.id();
        partyCriteria.isOrganisation();
        partyCriteria.organisationName();
        partyCriteria.organisationShortName();
        partyCriteria.salutation();
        partyCriteria.firstName();
        partyCriteria.middleName();
        partyCriteria.lastName();
        partyCriteria.gender();
        partyCriteria.birthDate();
        partyCriteria.primaryPhone();
        partyCriteria.primaryEmail();
        partyCriteria.isTemporaryPassword();
        partyCriteria.logoImageUrl();
        partyCriteria.profileImageUrl();
        partyCriteria.userId();
        partyCriteria.partyTypeId();
        partyCriteria.statusId();
        partyCriteria.addressId();
        partyCriteria.distinct();
    }

    private static Condition<PartyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIsOrganisation()) &&
                condition.apply(criteria.getOrganisationName()) &&
                condition.apply(criteria.getOrganisationShortName()) &&
                condition.apply(criteria.getSalutation()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getMiddleName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getBirthDate()) &&
                condition.apply(criteria.getPrimaryPhone()) &&
                condition.apply(criteria.getPrimaryEmail()) &&
                condition.apply(criteria.getIsTemporaryPassword()) &&
                condition.apply(criteria.getLogoImageUrl()) &&
                condition.apply(criteria.getProfileImageUrl()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getPartyTypeId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getAddressId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PartyCriteria> copyFiltersAre(PartyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIsOrganisation(), copy.getIsOrganisation()) &&
                condition.apply(criteria.getOrganisationName(), copy.getOrganisationName()) &&
                condition.apply(criteria.getOrganisationShortName(), copy.getOrganisationShortName()) &&
                condition.apply(criteria.getSalutation(), copy.getSalutation()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getMiddleName(), copy.getMiddleName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getBirthDate(), copy.getBirthDate()) &&
                condition.apply(criteria.getPrimaryPhone(), copy.getPrimaryPhone()) &&
                condition.apply(criteria.getPrimaryEmail(), copy.getPrimaryEmail()) &&
                condition.apply(criteria.getIsTemporaryPassword(), copy.getIsTemporaryPassword()) &&
                condition.apply(criteria.getLogoImageUrl(), copy.getLogoImageUrl()) &&
                condition.apply(criteria.getProfileImageUrl(), copy.getProfileImageUrl()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getPartyTypeId(), copy.getPartyTypeId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getAddressId(), copy.getAddressId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
