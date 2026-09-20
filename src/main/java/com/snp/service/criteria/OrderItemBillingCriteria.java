package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.OrderItemBilling} entity. This class is used
 * in {@link com.snp.web.rest.OrderItemBillingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /order-item-billings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderItemBillingCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter quantity;

    private BigDecimalFilter amount;

    private LongFilter orderItemId;

    private LongFilter invoiceItemId;

    private LongFilter shipmentReceiptId;

    private LongFilter itemIssuanceId;

    private Boolean distinct;

    public OrderItemBillingCriteria() {}

    public OrderItemBillingCriteria(OrderItemBillingCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.orderItemId = other.optionalOrderItemId().map(LongFilter::copy).orElse(null);
        this.invoiceItemId = other.optionalInvoiceItemId().map(LongFilter::copy).orElse(null);
        this.shipmentReceiptId = other.optionalShipmentReceiptId().map(LongFilter::copy).orElse(null);
        this.itemIssuanceId = other.optionalItemIssuanceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OrderItemBillingCriteria copy() {
        return new OrderItemBillingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getQuantity() {
        return quantity;
    }

    public Optional<BigDecimalFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public BigDecimalFilter quantity() {
        if (quantity == null) {
            setQuantity(new BigDecimalFilter());
        }
        return quantity;
    }

    public void setQuantity(BigDecimalFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public Optional<BigDecimalFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            setAmount(new BigDecimalFilter());
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public LongFilter getOrderItemId() {
        return orderItemId;
    }

    public Optional<LongFilter> optionalOrderItemId() {
        return Optional.ofNullable(orderItemId);
    }

    public LongFilter orderItemId() {
        if (orderItemId == null) {
            setOrderItemId(new LongFilter());
        }
        return orderItemId;
    }

    public void setOrderItemId(LongFilter orderItemId) {
        this.orderItemId = orderItemId;
    }

    public LongFilter getInvoiceItemId() {
        return invoiceItemId;
    }

    public Optional<LongFilter> optionalInvoiceItemId() {
        return Optional.ofNullable(invoiceItemId);
    }

    public LongFilter invoiceItemId() {
        if (invoiceItemId == null) {
            setInvoiceItemId(new LongFilter());
        }
        return invoiceItemId;
    }

    public void setInvoiceItemId(LongFilter invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public LongFilter getShipmentReceiptId() {
        return shipmentReceiptId;
    }

    public Optional<LongFilter> optionalShipmentReceiptId() {
        return Optional.ofNullable(shipmentReceiptId);
    }

    public LongFilter shipmentReceiptId() {
        if (shipmentReceiptId == null) {
            setShipmentReceiptId(new LongFilter());
        }
        return shipmentReceiptId;
    }

    public void setShipmentReceiptId(LongFilter shipmentReceiptId) {
        this.shipmentReceiptId = shipmentReceiptId;
    }

    public LongFilter getItemIssuanceId() {
        return itemIssuanceId;
    }

    public Optional<LongFilter> optionalItemIssuanceId() {
        return Optional.ofNullable(itemIssuanceId);
    }

    public LongFilter itemIssuanceId() {
        if (itemIssuanceId == null) {
            setItemIssuanceId(new LongFilter());
        }
        return itemIssuanceId;
    }

    public void setItemIssuanceId(LongFilter itemIssuanceId) {
        this.itemIssuanceId = itemIssuanceId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderItemBillingCriteria that = (OrderItemBillingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(invoiceItemId, that.invoiceItemId) &&
            Objects.equals(shipmentReceiptId, that.shipmentReceiptId) &&
            Objects.equals(itemIssuanceId, that.itemIssuanceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, amount, orderItemId, invoiceItemId, shipmentReceiptId, itemIssuanceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemBillingCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalOrderItemId().map(f -> "orderItemId=" + f + ", ").orElse("") +
            optionalInvoiceItemId().map(f -> "invoiceItemId=" + f + ", ").orElse("") +
            optionalShipmentReceiptId().map(f -> "shipmentReceiptId=" + f + ", ").orElse("") +
            optionalItemIssuanceId().map(f -> "itemIssuanceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
