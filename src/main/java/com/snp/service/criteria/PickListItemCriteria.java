package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.PickListItem} entity. This class is used
 * in {@link com.snp.web.rest.PickListItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pick-list-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PickListItemCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter quantity;

    private LongFilter pickListId;

    private LongFilter orderId;

    private LongFilter orderItemId;

    private LongFilter inventoryItemId;

    private LongFilter productId;

    private LongFilter facilityLocationId;

    private LongFilter statusId;

    private Boolean distinct;

    public PickListItemCriteria() {}

    public PickListItemCriteria(PickListItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.pickListId = other.optionalPickListId().map(LongFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(LongFilter::copy).orElse(null);
        this.orderItemId = other.optionalOrderItemId().map(LongFilter::copy).orElse(null);
        this.inventoryItemId = other.optionalInventoryItemId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.facilityLocationId = other.optionalFacilityLocationId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PickListItemCriteria copy() {
        return new PickListItemCriteria(this);
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

    public LongFilter getPickListId() {
        return pickListId;
    }

    public Optional<LongFilter> optionalPickListId() {
        return Optional.ofNullable(pickListId);
    }

    public LongFilter pickListId() {
        if (pickListId == null) {
            setPickListId(new LongFilter());
        }
        return pickListId;
    }

    public void setPickListId(LongFilter pickListId) {
        this.pickListId = pickListId;
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

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getFacilityLocationId() {
        return facilityLocationId;
    }

    public Optional<LongFilter> optionalFacilityLocationId() {
        return Optional.ofNullable(facilityLocationId);
    }

    public LongFilter facilityLocationId() {
        if (facilityLocationId == null) {
            setFacilityLocationId(new LongFilter());
        }
        return facilityLocationId;
    }

    public void setFacilityLocationId(LongFilter facilityLocationId) {
        this.facilityLocationId = facilityLocationId;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public Optional<LongFilter> optionalStatusId() {
        return Optional.ofNullable(statusId);
    }

    public LongFilter statusId() {
        if (statusId == null) {
            setStatusId(new LongFilter());
        }
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
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
        final PickListItemCriteria that = (PickListItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(pickListId, that.pickListId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(inventoryItemId, that.inventoryItemId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(facilityLocationId, that.facilityLocationId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            quantity,
            pickListId,
            orderId,
            orderItemId,
            inventoryItemId,
            productId,
            facilityLocationId,
            statusId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PickListItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalPickListId().map(f -> "pickListId=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalOrderItemId().map(f -> "orderItemId=" + f + ", ").orElse("") +
            optionalInventoryItemId().map(f -> "inventoryItemId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalFacilityLocationId().map(f -> "facilityLocationId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
