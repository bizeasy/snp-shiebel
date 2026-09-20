package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ShipmentReceipt} entity. This class is used
 * in {@link com.snp.web.rest.ShipmentReceiptResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shipment-receipts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShipmentReceiptCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter sequenceNo;

    private BigDecimalFilter quantity;

    private BigDecimalFilter quantityAccepted;

    private BigDecimalFilter quantityRejected;

    private InstantFilter receivedDate;

    private StringFilter message;

    private LongFilter shipmentId;

    private LongFilter shipmentItemId;

    private LongFilter orderId;

    private LongFilter orderItemId;

    private LongFilter inventoryItemId;

    private LongFilter productId;

    private LongFilter receivedById;

    private Boolean distinct;

    public ShipmentReceiptCriteria() {}

    public ShipmentReceiptCriteria(ShipmentReceiptCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.quantityAccepted = other.optionalQuantityAccepted().map(BigDecimalFilter::copy).orElse(null);
        this.quantityRejected = other.optionalQuantityRejected().map(BigDecimalFilter::copy).orElse(null);
        this.receivedDate = other.optionalReceivedDate().map(InstantFilter::copy).orElse(null);
        this.message = other.optionalMessage().map(StringFilter::copy).orElse(null);
        this.shipmentId = other.optionalShipmentId().map(LongFilter::copy).orElse(null);
        this.shipmentItemId = other.optionalShipmentItemId().map(LongFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(LongFilter::copy).orElse(null);
        this.orderItemId = other.optionalOrderItemId().map(LongFilter::copy).orElse(null);
        this.inventoryItemId = other.optionalInventoryItemId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.receivedById = other.optionalReceivedById().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ShipmentReceiptCriteria copy() {
        return new ShipmentReceiptCriteria(this);
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

    public IntegerFilter getSequenceNo() {
        return sequenceNo;
    }

    public Optional<IntegerFilter> optionalSequenceNo() {
        return Optional.ofNullable(sequenceNo);
    }

    public IntegerFilter sequenceNo() {
        if (sequenceNo == null) {
            setSequenceNo(new IntegerFilter());
        }
        return sequenceNo;
    }

    public void setSequenceNo(IntegerFilter sequenceNo) {
        this.sequenceNo = sequenceNo;
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

    public BigDecimalFilter getQuantityAccepted() {
        return quantityAccepted;
    }

    public Optional<BigDecimalFilter> optionalQuantityAccepted() {
        return Optional.ofNullable(quantityAccepted);
    }

    public BigDecimalFilter quantityAccepted() {
        if (quantityAccepted == null) {
            setQuantityAccepted(new BigDecimalFilter());
        }
        return quantityAccepted;
    }

    public void setQuantityAccepted(BigDecimalFilter quantityAccepted) {
        this.quantityAccepted = quantityAccepted;
    }

    public BigDecimalFilter getQuantityRejected() {
        return quantityRejected;
    }

    public Optional<BigDecimalFilter> optionalQuantityRejected() {
        return Optional.ofNullable(quantityRejected);
    }

    public BigDecimalFilter quantityRejected() {
        if (quantityRejected == null) {
            setQuantityRejected(new BigDecimalFilter());
        }
        return quantityRejected;
    }

    public void setQuantityRejected(BigDecimalFilter quantityRejected) {
        this.quantityRejected = quantityRejected;
    }

    public InstantFilter getReceivedDate() {
        return receivedDate;
    }

    public Optional<InstantFilter> optionalReceivedDate() {
        return Optional.ofNullable(receivedDate);
    }

    public InstantFilter receivedDate() {
        if (receivedDate == null) {
            setReceivedDate(new InstantFilter());
        }
        return receivedDate;
    }

    public void setReceivedDate(InstantFilter receivedDate) {
        this.receivedDate = receivedDate;
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

    public LongFilter getReceivedById() {
        return receivedById;
    }

    public Optional<LongFilter> optionalReceivedById() {
        return Optional.ofNullable(receivedById);
    }

    public LongFilter receivedById() {
        if (receivedById == null) {
            setReceivedById(new LongFilter());
        }
        return receivedById;
    }

    public void setReceivedById(LongFilter receivedById) {
        this.receivedById = receivedById;
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
        final ShipmentReceiptCriteria that = (ShipmentReceiptCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(quantityAccepted, that.quantityAccepted) &&
            Objects.equals(quantityRejected, that.quantityRejected) &&
            Objects.equals(receivedDate, that.receivedDate) &&
            Objects.equals(message, that.message) &&
            Objects.equals(shipmentId, that.shipmentId) &&
            Objects.equals(shipmentItemId, that.shipmentItemId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(inventoryItemId, that.inventoryItemId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(receivedById, that.receivedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sequenceNo,
            quantity,
            quantityAccepted,
            quantityRejected,
            receivedDate,
            message,
            shipmentId,
            shipmentItemId,
            orderId,
            orderItemId,
            inventoryItemId,
            productId,
            receivedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShipmentReceiptCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalQuantityAccepted().map(f -> "quantityAccepted=" + f + ", ").orElse("") +
            optionalQuantityRejected().map(f -> "quantityRejected=" + f + ", ").orElse("") +
            optionalReceivedDate().map(f -> "receivedDate=" + f + ", ").orElse("") +
            optionalMessage().map(f -> "message=" + f + ", ").orElse("") +
            optionalShipmentId().map(f -> "shipmentId=" + f + ", ").orElse("") +
            optionalShipmentItemId().map(f -> "shipmentItemId=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalOrderItemId().map(f -> "orderItemId=" + f + ", ").orElse("") +
            optionalInventoryItemId().map(f -> "inventoryItemId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalReceivedById().map(f -> "receivedById=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
