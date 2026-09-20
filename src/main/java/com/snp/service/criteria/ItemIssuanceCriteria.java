package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ItemIssuance} entity. This class is used
 * in {@link com.snp.web.rest.ItemIssuanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /item-issuances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemIssuanceCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter message;

    private InstantFilter issuedDate;

    private StringFilter issuedBy;

    private BigDecimalFilter quantity;

    private BigDecimalFilter cancelQuantity;

    private LongFilter inventoryItemId;

    private LongFilter orderId;

    private LongFilter orderItemId;

    private LongFilter shipmentId;

    private LongFilter shipmentItemId;

    private LongFilter issuedByPartyId;

    private Boolean distinct;

    public ItemIssuanceCriteria() {}

    public ItemIssuanceCriteria(ItemIssuanceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.message = other.optionalMessage().map(StringFilter::copy).orElse(null);
        this.issuedDate = other.optionalIssuedDate().map(InstantFilter::copy).orElse(null);
        this.issuedBy = other.optionalIssuedBy().map(StringFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.cancelQuantity = other.optionalCancelQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.inventoryItemId = other.optionalInventoryItemId().map(LongFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(LongFilter::copy).orElse(null);
        this.orderItemId = other.optionalOrderItemId().map(LongFilter::copy).orElse(null);
        this.shipmentId = other.optionalShipmentId().map(LongFilter::copy).orElse(null);
        this.shipmentItemId = other.optionalShipmentItemId().map(LongFilter::copy).orElse(null);
        this.issuedByPartyId = other.optionalIssuedByPartyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ItemIssuanceCriteria copy() {
        return new ItemIssuanceCriteria(this);
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

    public StringFilter getMessage() {
        return message;
    }

    public Optional<StringFilter> optionalMessage() {
        return Optional.ofNullable(message);
    }

    public StringFilter message() {
        if (message == null) {
            setMessage(new StringFilter());
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public InstantFilter getIssuedDate() {
        return issuedDate;
    }

    public Optional<InstantFilter> optionalIssuedDate() {
        return Optional.ofNullable(issuedDate);
    }

    public InstantFilter issuedDate() {
        if (issuedDate == null) {
            setIssuedDate(new InstantFilter());
        }
        return issuedDate;
    }

    public void setIssuedDate(InstantFilter issuedDate) {
        this.issuedDate = issuedDate;
    }

    public StringFilter getIssuedBy() {
        return issuedBy;
    }

    public Optional<StringFilter> optionalIssuedBy() {
        return Optional.ofNullable(issuedBy);
    }

    public StringFilter issuedBy() {
        if (issuedBy == null) {
            setIssuedBy(new StringFilter());
        }
        return issuedBy;
    }

    public void setIssuedBy(StringFilter issuedBy) {
        this.issuedBy = issuedBy;
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

    public BigDecimalFilter getCancelQuantity() {
        return cancelQuantity;
    }

    public Optional<BigDecimalFilter> optionalCancelQuantity() {
        return Optional.ofNullable(cancelQuantity);
    }

    public BigDecimalFilter cancelQuantity() {
        if (cancelQuantity == null) {
            setCancelQuantity(new BigDecimalFilter());
        }
        return cancelQuantity;
    }

    public void setCancelQuantity(BigDecimalFilter cancelQuantity) {
        this.cancelQuantity = cancelQuantity;
    }

    public LongFilter getInventoryItemId() {
        return inventoryItemId;
    }

    public Optional<LongFilter> optionalInventoryItemId() {
        return Optional.ofNullable(inventoryItemId);
    }

    public LongFilter inventoryItemId() {
        if (inventoryItemId == null) {
            setInventoryItemId(new LongFilter());
        }
        return inventoryItemId;
    }

    public void setInventoryItemId(LongFilter inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public Optional<LongFilter> optionalOrderId() {
        return Optional.ofNullable(orderId);
    }

    public LongFilter orderId() {
        if (orderId == null) {
            setOrderId(new LongFilter());
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
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

    public LongFilter getShipmentId() {
        return shipmentId;
    }

    public Optional<LongFilter> optionalShipmentId() {
        return Optional.ofNullable(shipmentId);
    }

    public LongFilter shipmentId() {
        if (shipmentId == null) {
            setShipmentId(new LongFilter());
        }
        return shipmentId;
    }

    public void setShipmentId(LongFilter shipmentId) {
        this.shipmentId = shipmentId;
    }

    public LongFilter getShipmentItemId() {
        return shipmentItemId;
    }

    public Optional<LongFilter> optionalShipmentItemId() {
        return Optional.ofNullable(shipmentItemId);
    }

    public LongFilter shipmentItemId() {
        if (shipmentItemId == null) {
            setShipmentItemId(new LongFilter());
        }
        return shipmentItemId;
    }

    public void setShipmentItemId(LongFilter shipmentItemId) {
        this.shipmentItemId = shipmentItemId;
    }

    public LongFilter getIssuedByPartyId() {
        return issuedByPartyId;
    }

    public Optional<LongFilter> optionalIssuedByPartyId() {
        return Optional.ofNullable(issuedByPartyId);
    }

    public LongFilter issuedByPartyId() {
        if (issuedByPartyId == null) {
            setIssuedByPartyId(new LongFilter());
        }
        return issuedByPartyId;
    }

    public void setIssuedByPartyId(LongFilter issuedByPartyId) {
        this.issuedByPartyId = issuedByPartyId;
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
        final ItemIssuanceCriteria that = (ItemIssuanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(message, that.message) &&
            Objects.equals(issuedDate, that.issuedDate) &&
            Objects.equals(issuedBy, that.issuedBy) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(cancelQuantity, that.cancelQuantity) &&
            Objects.equals(inventoryItemId, that.inventoryItemId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(shipmentId, that.shipmentId) &&
            Objects.equals(shipmentItemId, that.shipmentItemId) &&
            Objects.equals(issuedByPartyId, that.issuedByPartyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            message,
            issuedDate,
            issuedBy,
            quantity,
            cancelQuantity,
            inventoryItemId,
            orderId,
            orderItemId,
            shipmentId,
            shipmentItemId,
            issuedByPartyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemIssuanceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMessage().map(f -> "message=" + f + ", ").orElse("") +
            optionalIssuedDate().map(f -> "issuedDate=" + f + ", ").orElse("") +
            optionalIssuedBy().map(f -> "issuedBy=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalCancelQuantity().map(f -> "cancelQuantity=" + f + ", ").orElse("") +
            optionalInventoryItemId().map(f -> "inventoryItemId=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalOrderItemId().map(f -> "orderItemId=" + f + ", ").orElse("") +
            optionalShipmentId().map(f -> "shipmentId=" + f + ", ").orElse("") +
            optionalShipmentItemId().map(f -> "shipmentItemId=" + f + ", ").orElse("") +
            optionalIssuedByPartyId().map(f -> "issuedByPartyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
