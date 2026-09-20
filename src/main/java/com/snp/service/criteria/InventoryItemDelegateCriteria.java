package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.InventoryItemDelegate} entity. This class is used
 * in {@link com.snp.web.rest.InventoryItemDelegateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventory-item-delegates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryItemDelegateCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter sequenceNo;

    private InstantFilter effectiveDate;

    private BigDecimalFilter quantityOnHandDiff;

    private BigDecimalFilter availableToPromiseDiff;

    private BigDecimalFilter accountingQuantityDiff;

    private BigDecimalFilter unitCost;

    private StringFilter remarks;

    private LongFilter inventoryItemId;

    private LongFilter orderId;

    private LongFilter orderItemId;

    private LongFilter invoiceId;

    private LongFilter invoiceItemId;

    private LongFilter shipmentId;

    private LongFilter shipmentItemId;

    private LongFilter itemIssuanceId;

    private LongFilter receiptId;

    private Boolean distinct;

    public InventoryItemDelegateCriteria() {}

    public InventoryItemDelegateCriteria(InventoryItemDelegateCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.effectiveDate = other.optionalEffectiveDate().map(InstantFilter::copy).orElse(null);
        this.quantityOnHandDiff = other.optionalQuantityOnHandDiff().map(BigDecimalFilter::copy).orElse(null);
        this.availableToPromiseDiff = other.optionalAvailableToPromiseDiff().map(BigDecimalFilter::copy).orElse(null);
        this.accountingQuantityDiff = other.optionalAccountingQuantityDiff().map(BigDecimalFilter::copy).orElse(null);
        this.unitCost = other.optionalUnitCost().map(BigDecimalFilter::copy).orElse(null);
        this.remarks = other.optionalRemarks().map(StringFilter::copy).orElse(null);
        this.inventoryItemId = other.optionalInventoryItemId().map(LongFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(LongFilter::copy).orElse(null);
        this.orderItemId = other.optionalOrderItemId().map(LongFilter::copy).orElse(null);
        this.invoiceId = other.optionalInvoiceId().map(LongFilter::copy).orElse(null);
        this.invoiceItemId = other.optionalInvoiceItemId().map(LongFilter::copy).orElse(null);
        this.shipmentId = other.optionalShipmentId().map(LongFilter::copy).orElse(null);
        this.shipmentItemId = other.optionalShipmentItemId().map(LongFilter::copy).orElse(null);
        this.itemIssuanceId = other.optionalItemIssuanceId().map(LongFilter::copy).orElse(null);
        this.receiptId = other.optionalReceiptId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InventoryItemDelegateCriteria copy() {
        return new InventoryItemDelegateCriteria(this);
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

    public InstantFilter getEffectiveDate() {
        return effectiveDate;
    }

    public Optional<InstantFilter> optionalEffectiveDate() {
        return Optional.ofNullable(effectiveDate);
    }

    public InstantFilter effectiveDate() {
        if (effectiveDate == null) {
            setEffectiveDate(new InstantFilter());
        }
        return effectiveDate;
    }

    public void setEffectiveDate(InstantFilter effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimalFilter getQuantityOnHandDiff() {
        return quantityOnHandDiff;
    }

    public Optional<BigDecimalFilter> optionalQuantityOnHandDiff() {
        return Optional.ofNullable(quantityOnHandDiff);
    }

    public BigDecimalFilter quantityOnHandDiff() {
        if (quantityOnHandDiff == null) {
            setQuantityOnHandDiff(new BigDecimalFilter());
        }
        return quantityOnHandDiff;
    }

    public void setQuantityOnHandDiff(BigDecimalFilter quantityOnHandDiff) {
        this.quantityOnHandDiff = quantityOnHandDiff;
    }

    public BigDecimalFilter getAvailableToPromiseDiff() {
        return availableToPromiseDiff;
    }

    public Optional<BigDecimalFilter> optionalAvailableToPromiseDiff() {
        return Optional.ofNullable(availableToPromiseDiff);
    }

    public BigDecimalFilter availableToPromiseDiff() {
        if (availableToPromiseDiff == null) {
            setAvailableToPromiseDiff(new BigDecimalFilter());
        }
        return availableToPromiseDiff;
    }

    public void setAvailableToPromiseDiff(BigDecimalFilter availableToPromiseDiff) {
        this.availableToPromiseDiff = availableToPromiseDiff;
    }

    public BigDecimalFilter getAccountingQuantityDiff() {
        return accountingQuantityDiff;
    }

    public Optional<BigDecimalFilter> optionalAccountingQuantityDiff() {
        return Optional.ofNullable(accountingQuantityDiff);
    }

    public BigDecimalFilter accountingQuantityDiff() {
        if (accountingQuantityDiff == null) {
            setAccountingQuantityDiff(new BigDecimalFilter());
        }
        return accountingQuantityDiff;
    }

    public void setAccountingQuantityDiff(BigDecimalFilter accountingQuantityDiff) {
        this.accountingQuantityDiff = accountingQuantityDiff;
    }

    public BigDecimalFilter getUnitCost() {
        return unitCost;
    }

    public Optional<BigDecimalFilter> optionalUnitCost() {
        return Optional.ofNullable(unitCost);
    }

    public BigDecimalFilter unitCost() {
        if (unitCost == null) {
            setUnitCost(new BigDecimalFilter());
        }
        return unitCost;
    }

    public void setUnitCost(BigDecimalFilter unitCost) {
        this.unitCost = unitCost;
    }

    public StringFilter getRemarks() {
        return remarks;
    }

    public Optional<StringFilter> optionalRemarks() {
        return Optional.ofNullable(remarks);
    }

    public StringFilter remarks() {
        if (remarks == null) {
            setRemarks(new StringFilter());
        }
        return remarks;
    }

    public void setRemarks(StringFilter remarks) {
        this.remarks = remarks;
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

    public LongFilter getInvoiceId() {
        return invoiceId;
    }

    public Optional<LongFilter> optionalInvoiceId() {
        return Optional.ofNullable(invoiceId);
    }

    public LongFilter invoiceId() {
        if (invoiceId == null) {
            setInvoiceId(new LongFilter());
        }
        return invoiceId;
    }

    public void setInvoiceId(LongFilter invoiceId) {
        this.invoiceId = invoiceId;
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

    public LongFilter getReceiptId() {
        return receiptId;
    }

    public Optional<LongFilter> optionalReceiptId() {
        return Optional.ofNullable(receiptId);
    }

    public LongFilter receiptId() {
        if (receiptId == null) {
            setReceiptId(new LongFilter());
        }
        return receiptId;
    }

    public void setReceiptId(LongFilter receiptId) {
        this.receiptId = receiptId;
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
        final InventoryItemDelegateCriteria that = (InventoryItemDelegateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(effectiveDate, that.effectiveDate) &&
            Objects.equals(quantityOnHandDiff, that.quantityOnHandDiff) &&
            Objects.equals(availableToPromiseDiff, that.availableToPromiseDiff) &&
            Objects.equals(accountingQuantityDiff, that.accountingQuantityDiff) &&
            Objects.equals(unitCost, that.unitCost) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(inventoryItemId, that.inventoryItemId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(invoiceId, that.invoiceId) &&
            Objects.equals(invoiceItemId, that.invoiceItemId) &&
            Objects.equals(shipmentId, that.shipmentId) &&
            Objects.equals(shipmentItemId, that.shipmentItemId) &&
            Objects.equals(itemIssuanceId, that.itemIssuanceId) &&
            Objects.equals(receiptId, that.receiptId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sequenceNo,
            effectiveDate,
            quantityOnHandDiff,
            availableToPromiseDiff,
            accountingQuantityDiff,
            unitCost,
            remarks,
            inventoryItemId,
            orderId,
            orderItemId,
            invoiceId,
            invoiceItemId,
            shipmentId,
            shipmentItemId,
            itemIssuanceId,
            receiptId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryItemDelegateCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalEffectiveDate().map(f -> "effectiveDate=" + f + ", ").orElse("") +
            optionalQuantityOnHandDiff().map(f -> "quantityOnHandDiff=" + f + ", ").orElse("") +
            optionalAvailableToPromiseDiff().map(f -> "availableToPromiseDiff=" + f + ", ").orElse("") +
            optionalAccountingQuantityDiff().map(f -> "accountingQuantityDiff=" + f + ", ").orElse("") +
            optionalUnitCost().map(f -> "unitCost=" + f + ", ").orElse("") +
            optionalRemarks().map(f -> "remarks=" + f + ", ").orElse("") +
            optionalInventoryItemId().map(f -> "inventoryItemId=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalOrderItemId().map(f -> "orderItemId=" + f + ", ").orElse("") +
            optionalInvoiceId().map(f -> "invoiceId=" + f + ", ").orElse("") +
            optionalInvoiceItemId().map(f -> "invoiceItemId=" + f + ", ").orElse("") +
            optionalShipmentId().map(f -> "shipmentId=" + f + ", ").orElse("") +
            optionalShipmentItemId().map(f -> "shipmentItemId=" + f + ", ").orElse("") +
            optionalItemIssuanceId().map(f -> "itemIssuanceId=" + f + ", ").orElse("") +
            optionalReceiptId().map(f -> "receiptId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
