package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SupplierProductCriteriaTest {

    @Test
    void newSupplierProductCriteriaHasAllFiltersNullTest() {
        var supplierProductCriteria = new SupplierProductCriteria();
        assertThat(supplierProductCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void supplierProductCriteriaFluentMethodsCreatesFiltersTest() {
        var supplierProductCriteria = new SupplierProductCriteria();

        setAllFilters(supplierProductCriteria);

        assertThat(supplierProductCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void supplierProductCriteriaCopyCreatesNullFilterTest() {
        var supplierProductCriteria = new SupplierProductCriteria();
        var copy = supplierProductCriteria.copy();

        assertThat(supplierProductCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(supplierProductCriteria)
        );
    }

    @Test
    void supplierProductCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var supplierProductCriteria = new SupplierProductCriteria();
        setAllFilters(supplierProductCriteria);

        var copy = supplierProductCriteria.copy();

        assertThat(supplierProductCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(supplierProductCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var supplierProductCriteria = new SupplierProductCriteria();

        assertThat(supplierProductCriteria).hasToString("SupplierProductCriteria{}");
    }

    private static void setAllFilters(SupplierProductCriteria supplierProductCriteria) {
        supplierProductCriteria.id();
        supplierProductCriteria.fromDate();
        supplierProductCriteria.thruDate();
        supplierProductCriteria.supplierProductId();
        supplierProductCriteria.minOrderQty();
        supplierProductCriteria.lastPrice();
        supplierProductCriteria.shippingPrice();
        supplierProductCriteria.cgst();
        supplierProductCriteria.igst();
        supplierProductCriteria.sgst();
        supplierProductCriteria.totalPrice();
        supplierProductCriteria.comments();
        supplierProductCriteria.leadTimeDays();
        supplierProductCriteria.unitsIncluded();
        supplierProductCriteria.canDropShip();
        supplierProductCriteria.productId();
        supplierProductCriteria.partyId();
        supplierProductCriteria.quantityUomId();
        supplierProductCriteria.currencyUomId();
        supplierProductCriteria.distinct();
    }

    private static Condition<SupplierProductCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getSupplierProductId()) &&
                condition.apply(criteria.getMinOrderQty()) &&
                condition.apply(criteria.getLastPrice()) &&
                condition.apply(criteria.getShippingPrice()) &&
                condition.apply(criteria.getCgst()) &&
                condition.apply(criteria.getIgst()) &&
                condition.apply(criteria.getSgst()) &&
                condition.apply(criteria.getTotalPrice()) &&
                condition.apply(criteria.getComments()) &&
                condition.apply(criteria.getLeadTimeDays()) &&
                condition.apply(criteria.getUnitsIncluded()) &&
                condition.apply(criteria.getCanDropShip()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getQuantityUomId()) &&
                condition.apply(criteria.getCurrencyUomId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SupplierProductCriteria> copyFiltersAre(
        SupplierProductCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getSupplierProductId(), copy.getSupplierProductId()) &&
                condition.apply(criteria.getMinOrderQty(), copy.getMinOrderQty()) &&
                condition.apply(criteria.getLastPrice(), copy.getLastPrice()) &&
                condition.apply(criteria.getShippingPrice(), copy.getShippingPrice()) &&
                condition.apply(criteria.getCgst(), copy.getCgst()) &&
                condition.apply(criteria.getIgst(), copy.getIgst()) &&
                condition.apply(criteria.getSgst(), copy.getSgst()) &&
                condition.apply(criteria.getTotalPrice(), copy.getTotalPrice()) &&
                condition.apply(criteria.getComments(), copy.getComments()) &&
                condition.apply(criteria.getLeadTimeDays(), copy.getLeadTimeDays()) &&
                condition.apply(criteria.getUnitsIncluded(), copy.getUnitsIncluded()) &&
                condition.apply(criteria.getCanDropShip(), copy.getCanDropShip()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getQuantityUomId(), copy.getQuantityUomId()) &&
                condition.apply(criteria.getCurrencyUomId(), copy.getCurrencyUomId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
