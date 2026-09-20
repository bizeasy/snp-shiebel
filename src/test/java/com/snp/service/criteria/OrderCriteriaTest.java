package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderCriteriaTest {

    @Test
    void newOrderCriteriaHasAllFiltersNullTest() {
        var orderCriteria = new OrderCriteria();
        assertThat(orderCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void orderCriteriaFluentMethodsCreatesFiltersTest() {
        var orderCriteria = new OrderCriteria();

        setAllFilters(orderCriteria);

        assertThat(orderCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void orderCriteriaCopyCreatesNullFilterTest() {
        var orderCriteria = new OrderCriteria();
        var copy = orderCriteria.copy();

        assertThat(orderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(orderCriteria)
        );
    }

    @Test
    void orderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderCriteria = new OrderCriteria();
        setAllFilters(orderCriteria);

        var copy = orderCriteria.copy();

        assertThat(orderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(orderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderCriteria = new OrderCriteria();

        assertThat(orderCriteria).hasToString("OrderCriteria{}");
    }

    private static void setAllFilters(OrderCriteria orderCriteria) {
        orderCriteria.id();
        orderCriteria.name();
        orderCriteria.orderRefNo();
        orderCriteria.externalId();
        orderCriteria.orderDate();
        orderCriteria.entryDate();
        orderCriteria.deliveryDate();
        orderCriteria.priority();
        orderCriteria.isRushOrder();
        orderCriteria.needsInventoryIssuance();
        orderCriteria.remainingSubTotal();
        orderCriteria.grandTotal();
        orderCriteria.projectName();
        orderCriteria.version();
        orderCriteria.versionRef();
        orderCriteria.remarks();
        orderCriteria.attr1();
        orderCriteria.attr2();
        orderCriteria.attr3();
        orderCriteria.orderTypeId();
        orderCriteria.salesChannelId();
        orderCriteria.partyId();
        orderCriteria.supplierId();
        orderCriteria.statusId();
        orderCriteria.currencyUomId();
        orderCriteria.currencyConversionId();
        orderCriteria.quoteId();
        orderCriteria.facilityId();
        orderCriteria.productStoreId();
        orderCriteria.departmentId();
        orderCriteria.customTimePeriodId();
        orderCriteria.distinct();
    }

    private static Condition<OrderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getOrderRefNo()) &&
                condition.apply(criteria.getExternalId()) &&
                condition.apply(criteria.getOrderDate()) &&
                condition.apply(criteria.getEntryDate()) &&
                condition.apply(criteria.getDeliveryDate()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getIsRushOrder()) &&
                condition.apply(criteria.getNeedsInventoryIssuance()) &&
                condition.apply(criteria.getRemainingSubTotal()) &&
                condition.apply(criteria.getGrandTotal()) &&
                condition.apply(criteria.getProjectName()) &&
                condition.apply(criteria.getVersion()) &&
                condition.apply(criteria.getVersionRef()) &&
                condition.apply(criteria.getRemarks()) &&
                condition.apply(criteria.getAttr1()) &&
                condition.apply(criteria.getAttr2()) &&
                condition.apply(criteria.getAttr3()) &&
                condition.apply(criteria.getOrderTypeId()) &&
                condition.apply(criteria.getSalesChannelId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getSupplierId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getCurrencyUomId()) &&
                condition.apply(criteria.getCurrencyConversionId()) &&
                condition.apply(criteria.getQuoteId()) &&
                condition.apply(criteria.getFacilityId()) &&
                condition.apply(criteria.getProductStoreId()) &&
                condition.apply(criteria.getDepartmentId()) &&
                condition.apply(criteria.getCustomTimePeriodId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrderCriteria> copyFiltersAre(OrderCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getOrderRefNo(), copy.getOrderRefNo()) &&
                condition.apply(criteria.getExternalId(), copy.getExternalId()) &&
                condition.apply(criteria.getOrderDate(), copy.getOrderDate()) &&
                condition.apply(criteria.getEntryDate(), copy.getEntryDate()) &&
                condition.apply(criteria.getDeliveryDate(), copy.getDeliveryDate()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getIsRushOrder(), copy.getIsRushOrder()) &&
                condition.apply(criteria.getNeedsInventoryIssuance(), copy.getNeedsInventoryIssuance()) &&
                condition.apply(criteria.getRemainingSubTotal(), copy.getRemainingSubTotal()) &&
                condition.apply(criteria.getGrandTotal(), copy.getGrandTotal()) &&
                condition.apply(criteria.getProjectName(), copy.getProjectName()) &&
                condition.apply(criteria.getVersion(), copy.getVersion()) &&
                condition.apply(criteria.getVersionRef(), copy.getVersionRef()) &&
                condition.apply(criteria.getRemarks(), copy.getRemarks()) &&
                condition.apply(criteria.getAttr1(), copy.getAttr1()) &&
                condition.apply(criteria.getAttr2(), copy.getAttr2()) &&
                condition.apply(criteria.getAttr3(), copy.getAttr3()) &&
                condition.apply(criteria.getOrderTypeId(), copy.getOrderTypeId()) &&
                condition.apply(criteria.getSalesChannelId(), copy.getSalesChannelId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getSupplierId(), copy.getSupplierId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getCurrencyUomId(), copy.getCurrencyUomId()) &&
                condition.apply(criteria.getCurrencyConversionId(), copy.getCurrencyConversionId()) &&
                condition.apply(criteria.getQuoteId(), copy.getQuoteId()) &&
                condition.apply(criteria.getFacilityId(), copy.getFacilityId()) &&
                condition.apply(criteria.getProductStoreId(), copy.getProductStoreId()) &&
                condition.apply(criteria.getDepartmentId(), copy.getDepartmentId()) &&
                condition.apply(criteria.getCustomTimePeriodId(), copy.getCustomTimePeriodId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
