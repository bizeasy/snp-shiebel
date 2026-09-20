package com.snp.domain;

import static com.snp.domain.CustomTimePeriodTestSamples.*;
import static com.snp.domain.DepartmentTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.OrderTypeTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductStoreTestSamples.*;
import static com.snp.domain.QuoteTestSamples.*;
import static com.snp.domain.SalesChannelTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static com.snp.domain.UomConversionTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void orderTypeTest() {
        Order order = getOrderRandomSampleGenerator();
        OrderType orderTypeBack = getOrderTypeRandomSampleGenerator();

        order.setOrderType(orderTypeBack);
        assertThat(order.getOrderType()).isEqualTo(orderTypeBack);

        order.orderType(null);
        assertThat(order.getOrderType()).isNull();
    }

    @Test
    void salesChannelTest() {
        Order order = getOrderRandomSampleGenerator();
        SalesChannel salesChannelBack = getSalesChannelRandomSampleGenerator();

        order.setSalesChannel(salesChannelBack);
        assertThat(order.getSalesChannel()).isEqualTo(salesChannelBack);

        order.salesChannel(null);
        assertThat(order.getSalesChannel()).isNull();
    }

    @Test
    void partyTest() {
        Order order = getOrderRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        order.setParty(partyBack);
        assertThat(order.getParty()).isEqualTo(partyBack);

        order.party(null);
        assertThat(order.getParty()).isNull();
    }

    @Test
    void supplierTest() {
        Order order = getOrderRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        order.setSupplier(partyBack);
        assertThat(order.getSupplier()).isEqualTo(partyBack);

        order.supplier(null);
        assertThat(order.getSupplier()).isNull();
    }

    @Test
    void statusTest() {
        Order order = getOrderRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        order.setStatus(statusBack);
        assertThat(order.getStatus()).isEqualTo(statusBack);

        order.status(null);
        assertThat(order.getStatus()).isNull();
    }

    @Test
    void currencyUomTest() {
        Order order = getOrderRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        order.setCurrencyUom(uomBack);
        assertThat(order.getCurrencyUom()).isEqualTo(uomBack);

        order.currencyUom(null);
        assertThat(order.getCurrencyUom()).isNull();
    }

    @Test
    void currencyConversionTest() {
        Order order = getOrderRandomSampleGenerator();
        UomConversion uomConversionBack = getUomConversionRandomSampleGenerator();

        order.setCurrencyConversion(uomConversionBack);
        assertThat(order.getCurrencyConversion()).isEqualTo(uomConversionBack);

        order.currencyConversion(null);
        assertThat(order.getCurrencyConversion()).isNull();
    }

    @Test
    void quoteTest() {
        Order order = getOrderRandomSampleGenerator();
        Quote quoteBack = getQuoteRandomSampleGenerator();

        order.setQuote(quoteBack);
        assertThat(order.getQuote()).isEqualTo(quoteBack);

        order.quote(null);
        assertThat(order.getQuote()).isNull();
    }

    @Test
    void facilityTest() {
        Order order = getOrderRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        order.setFacility(facilityBack);
        assertThat(order.getFacility()).isEqualTo(facilityBack);

        order.facility(null);
        assertThat(order.getFacility()).isNull();
    }

    @Test
    void productStoreTest() {
        Order order = getOrderRandomSampleGenerator();
        ProductStore productStoreBack = getProductStoreRandomSampleGenerator();

        order.setProductStore(productStoreBack);
        assertThat(order.getProductStore()).isEqualTo(productStoreBack);

        order.productStore(null);
        assertThat(order.getProductStore()).isNull();
    }

    @Test
    void departmentTest() {
        Order order = getOrderRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        order.setDepartment(departmentBack);
        assertThat(order.getDepartment()).isEqualTo(departmentBack);

        order.department(null);
        assertThat(order.getDepartment()).isNull();
    }

    @Test
    void customTimePeriodTest() {
        Order order = getOrderRandomSampleGenerator();
        CustomTimePeriod customTimePeriodBack = getCustomTimePeriodRandomSampleGenerator();

        order.setCustomTimePeriod(customTimePeriodBack);
        assertThat(order.getCustomTimePeriod()).isEqualTo(customTimePeriodBack);

        order.customTimePeriod(null);
        assertThat(order.getCustomTimePeriod()).isNull();
    }
}
