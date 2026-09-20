package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.InvoiceItem} entity. This class is used
 * in {@link com.snp.web.rest.InvoiceItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoice-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceItemCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter sequenceNo;

    private BigDecimalFilter quantity;

    private BigDecimalFilter amount;

    private BigDecimalFilter cancelQuantity;

    private BigDecimalFilter selectedAmount;

    private BigDecimalFilter unitPrice;

    private BigDecimalFilter unitListPrice;

    private BooleanFilter isModifiedPrice;

    private InstantFilter estimatedShipDate;

    private InstantFilter estimatedDeliveryDate;

    private InstantFilter shipBeforeDate;

    private InstantFilter shipAfterDate;

    private InstantFilter entryDate;

    private StringFilter attr1;

    private StringFilter attr2;

    private StringFilter attr3;

    private LongFilter invoiceId;

    private LongFilter invoiceItemTypeId;

    private LongFilter productId;

    private LongFilter uomId;

    private LongFilter productFeatureId;

    private LongFilter productFacilityId;

    private LongFilter fromInventoryItemId;

    private LongFilter taxAuthRateId;

    private LongFilter parentInvoiceId;

    private LongFilter parentInvoiceItemId;

    private Boolean distinct;

    public InvoiceItemCriteria() {}

    public InvoiceItemCriteria(InvoiceItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.cancelQuantity = other.optionalCancelQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.selectedAmount = other.optionalSelectedAmount().map(BigDecimalFilter::copy).orElse(null);
        this.unitPrice = other.optionalUnitPrice().map(BigDecimalFilter::copy).orElse(null);
        this.unitListPrice = other.optionalUnitListPrice().map(BigDecimalFilter::copy).orElse(null);
        this.isModifiedPrice = other.optionalIsModifiedPrice().map(BooleanFilter::copy).orElse(null);
        this.estimatedShipDate = other.optionalEstimatedShipDate().map(InstantFilter::copy).orElse(null);
        this.estimatedDeliveryDate = other.optionalEstimatedDeliveryDate().map(InstantFilter::copy).orElse(null);
        this.shipBeforeDate = other.optionalShipBeforeDate().map(InstantFilter::copy).orElse(null);
        this.shipAfterDate = other.optionalShipAfterDate().map(InstantFilter::copy).orElse(null);
        this.entryDate = other.optionalEntryDate().map(InstantFilter::copy).orElse(null);
        this.attr1 = other.optionalAttr1().map(StringFilter::copy).orElse(null);
        this.attr2 = other.optionalAttr2().map(StringFilter::copy).orElse(null);
        this.attr3 = other.optionalAttr3().map(StringFilter::copy).orElse(null);
        this.invoiceId = other.optionalInvoiceId().map(LongFilter::copy).orElse(null);
        this.invoiceItemTypeId = other.optionalInvoiceItemTypeId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.uomId = other.optionalUomId().map(LongFilter::copy).orElse(null);
        this.productFeatureId = other.optionalProductFeatureId().map(LongFilter::copy).orElse(null);
        this.productFacilityId = other.optionalProductFacilityId().map(LongFilter::copy).orElse(null);
        this.fromInventoryItemId = other.optionalFromInventoryItemId().map(LongFilter::copy).orElse(null);
        this.taxAuthRateId = other.optionalTaxAuthRateId().map(LongFilter::copy).orElse(null);
        this.parentInvoiceId = other.optionalParentInvoiceId().map(LongFilter::copy).orElse(null);
        this.parentInvoiceItemId = other.optionalParentInvoiceItemId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InvoiceItemCriteria copy() {
        return new InvoiceItemCriteria(this);
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

    public BigDecimalFilter getSelectedAmount() {
        return selectedAmount;
    }

    public Optional<BigDecimalFilter> optionalSelectedAmount() {
        return Optional.ofNullable(selectedAmount);
    }

    public BigDecimalFilter selectedAmount() {
        if (selectedAmount == null) {
            setSelectedAmount(new BigDecimalFilter());
        }
        return selectedAmount;
    }

    public void setSelectedAmount(BigDecimalFilter selectedAmount) {
        this.selectedAmount = selectedAmount;
    }

    public BigDecimalFilter getUnitPrice() {
        return unitPrice;
    }

    public Optional<BigDecimalFilter> optionalUnitPrice() {
        return Optional.ofNullable(unitPrice);
    }

    public BigDecimalFilter unitPrice() {
        if (unitPrice == null) {
            setUnitPrice(new BigDecimalFilter());
        }
        return unitPrice;
    }

    public void setUnitPrice(BigDecimalFilter unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimalFilter getUnitListPrice() {
        return unitListPrice;
    }

    public Optional<BigDecimalFilter> optionalUnitListPrice() {
        return Optional.ofNullable(unitListPrice);
    }

    public BigDecimalFilter unitListPrice() {
        if (unitListPrice == null) {
            setUnitListPrice(new BigDecimalFilter());
        }
        return unitListPrice;
    }

    public void setUnitListPrice(BigDecimalFilter unitListPrice) {
        this.unitListPrice = unitListPrice;
    }

    public BooleanFilter getIsModifiedPrice() {
        return isModifiedPrice;
    }

    public Optional<BooleanFilter> optionalIsModifiedPrice() {
        return Optional.ofNullable(isModifiedPrice);
    }

    public BooleanFilter isModifiedPrice() {
        if (isModifiedPrice == null) {
            setIsModifiedPrice(new BooleanFilter());
        }
        return isModifiedPrice;
    }

    public void setIsModifiedPrice(BooleanFilter isModifiedPrice) {
        this.isModifiedPrice = isModifiedPrice;
    }

    public InstantFilter getEstimatedShipDate() {
        return estimatedShipDate;
    }

    public Optional<InstantFilter> optionalEstimatedShipDate() {
        return Optional.ofNullable(estimatedShipDate);
    }

    public InstantFilter estimatedShipDate() {
        if (estimatedShipDate == null) {
            setEstimatedShipDate(new InstantFilter());
        }
        return estimatedShipDate;
    }

    public void setEstimatedShipDate(InstantFilter estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
    }

    public InstantFilter getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public Optional<InstantFilter> optionalEstimatedDeliveryDate() {
        return Optional.ofNullable(estimatedDeliveryDate);
    }

    public InstantFilter estimatedDeliveryDate() {
        if (estimatedDeliveryDate == null) {
            setEstimatedDeliveryDate(new InstantFilter());
        }
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(InstantFilter estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public InstantFilter getShipBeforeDate() {
        return shipBeforeDate;
    }

    public Optional<InstantFilter> optionalShipBeforeDate() {
        return Optional.ofNullable(shipBeforeDate);
    }

    public InstantFilter shipBeforeDate() {
        if (shipBeforeDate == null) {
            setShipBeforeDate(new InstantFilter());
        }
        return shipBeforeDate;
    }

    public void setShipBeforeDate(InstantFilter shipBeforeDate) {
        this.shipBeforeDate = shipBeforeDate;
    }

    public InstantFilter getShipAfterDate() {
        return shipAfterDate;
    }

    public Optional<InstantFilter> optionalShipAfterDate() {
        return Optional.ofNullable(shipAfterDate);
    }

    public InstantFilter shipAfterDate() {
        if (shipAfterDate == null) {
            setShipAfterDate(new InstantFilter());
        }
        return shipAfterDate;
    }

    public void setShipAfterDate(InstantFilter shipAfterDate) {
        this.shipAfterDate = shipAfterDate;
    }

    public InstantFilter getEntryDate() {
        return entryDate;
    }

    public Optional<InstantFilter> optionalEntryDate() {
        return Optional.ofNullable(entryDate);
    }

    public InstantFilter entryDate() {
        if (entryDate == null) {
            setEntryDate(new InstantFilter());
        }
        return entryDate;
    }

    public void setEntryDate(InstantFilter entryDate) {
        this.entryDate = entryDate;
    }

    public StringFilter getAttr1() {
        return attr1;
    }

    public Optional<StringFilter> optionalAttr1() {
        return Optional.ofNullable(attr1);
    }

    public StringFilter attr1() {
        if (attr1 == null) {
            setAttr1(new StringFilter());
        }
        return attr1;
    }

    public void setAttr1(StringFilter attr1) {
        this.attr1 = attr1;
    }

    public StringFilter getAttr2() {
        return attr2;
    }

    public Optional<StringFilter> optionalAttr2() {
        return Optional.ofNullable(attr2);
    }

    public StringFilter attr2() {
        if (attr2 == null) {
            setAttr2(new StringFilter());
        }
        return attr2;
    }

    public void setAttr2(StringFilter attr2) {
        this.attr2 = attr2;
    }

    public StringFilter getAttr3() {
        return attr3;
    }

    public Optional<StringFilter> optionalAttr3() {
        return Optional.ofNullable(attr3);
    }

    public StringFilter attr3() {
        if (attr3 == null) {
            setAttr3(new StringFilter());
        }
        return attr3;
    }

    public void setAttr3(StringFilter attr3) {
        this.attr3 = attr3;
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

    public LongFilter getInvoiceItemTypeId() {
        return invoiceItemTypeId;
    }

    public Optional<LongFilter> optionalInvoiceItemTypeId() {
        return Optional.ofNullable(invoiceItemTypeId);
    }

    public LongFilter invoiceItemTypeId() {
        if (invoiceItemTypeId == null) {
            setInvoiceItemTypeId(new LongFilter());
        }
        return invoiceItemTypeId;
    }

    public void setInvoiceItemTypeId(LongFilter invoiceItemTypeId) {
        this.invoiceItemTypeId = invoiceItemTypeId;
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

    public LongFilter getUomId() {
        return uomId;
    }

    public Optional<LongFilter> optionalUomId() {
        return Optional.ofNullable(uomId);
    }

    public LongFilter uomId() {
        if (uomId == null) {
            setUomId(new LongFilter());
        }
        return uomId;
    }

    public void setUomId(LongFilter uomId) {
        this.uomId = uomId;
    }

    public LongFilter getProductFeatureId() {
        return productFeatureId;
    }

    public Optional<LongFilter> optionalProductFeatureId() {
        return Optional.ofNullable(productFeatureId);
    }

    public LongFilter productFeatureId() {
        if (productFeatureId == null) {
            setProductFeatureId(new LongFilter());
        }
        return productFeatureId;
    }

    public void setProductFeatureId(LongFilter productFeatureId) {
        this.productFeatureId = productFeatureId;
    }

    public LongFilter getProductFacilityId() {
        return productFacilityId;
    }

    public Optional<LongFilter> optionalProductFacilityId() {
        return Optional.ofNullable(productFacilityId);
    }

    public LongFilter productFacilityId() {
        if (productFacilityId == null) {
            setProductFacilityId(new LongFilter());
        }
        return productFacilityId;
    }

    public void setProductFacilityId(LongFilter productFacilityId) {
        this.productFacilityId = productFacilityId;
    }

    public LongFilter getFromInventoryItemId() {
        return fromInventoryItemId;
    }

    public Optional<LongFilter> optionalFromInventoryItemId() {
        return Optional.ofNullable(fromInventoryItemId);
    }

    public LongFilter fromInventoryItemId() {
        if (fromInventoryItemId == null) {
            setFromInventoryItemId(new LongFilter());
        }
        return fromInventoryItemId;
    }

    public void setFromInventoryItemId(LongFilter fromInventoryItemId) {
        this.fromInventoryItemId = fromInventoryItemId;
    }

    public LongFilter getTaxAuthRateId() {
        return taxAuthRateId;
    }

    public Optional<LongFilter> optionalTaxAuthRateId() {
        return Optional.ofNullable(taxAuthRateId);
    }

    public LongFilter taxAuthRateId() {
        if (taxAuthRateId == null) {
            setTaxAuthRateId(new LongFilter());
        }
        return taxAuthRateId;
    }

    public void setTaxAuthRateId(LongFilter taxAuthRateId) {
        this.taxAuthRateId = taxAuthRateId;
    }

    public LongFilter getParentInvoiceId() {
        return parentInvoiceId;
    }

    public Optional<LongFilter> optionalParentInvoiceId() {
        return Optional.ofNullable(parentInvoiceId);
    }

    public LongFilter parentInvoiceId() {
        if (parentInvoiceId == null) {
            setParentInvoiceId(new LongFilter());
        }
        return parentInvoiceId;
    }

    public void setParentInvoiceId(LongFilter parentInvoiceId) {
        this.parentInvoiceId = parentInvoiceId;
    }

    public LongFilter getParentInvoiceItemId() {
        return parentInvoiceItemId;
    }

    public Optional<LongFilter> optionalParentInvoiceItemId() {
        return Optional.ofNullable(parentInvoiceItemId);
    }

    public LongFilter parentInvoiceItemId() {
        if (parentInvoiceItemId == null) {
            setParentInvoiceItemId(new LongFilter());
        }
        return parentInvoiceItemId;
    }

    public void setParentInvoiceItemId(LongFilter parentInvoiceItemId) {
        this.parentInvoiceItemId = parentInvoiceItemId;
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
        final InvoiceItemCriteria that = (InvoiceItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(cancelQuantity, that.cancelQuantity) &&
            Objects.equals(selectedAmount, that.selectedAmount) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(unitListPrice, that.unitListPrice) &&
            Objects.equals(isModifiedPrice, that.isModifiedPrice) &&
            Objects.equals(estimatedShipDate, that.estimatedShipDate) &&
            Objects.equals(estimatedDeliveryDate, that.estimatedDeliveryDate) &&
            Objects.equals(shipBeforeDate, that.shipBeforeDate) &&
            Objects.equals(shipAfterDate, that.shipAfterDate) &&
            Objects.equals(entryDate, that.entryDate) &&
            Objects.equals(attr1, that.attr1) &&
            Objects.equals(attr2, that.attr2) &&
            Objects.equals(attr3, that.attr3) &&
            Objects.equals(invoiceId, that.invoiceId) &&
            Objects.equals(invoiceItemTypeId, that.invoiceItemTypeId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(uomId, that.uomId) &&
            Objects.equals(productFeatureId, that.productFeatureId) &&
            Objects.equals(productFacilityId, that.productFacilityId) &&
            Objects.equals(fromInventoryItemId, that.fromInventoryItemId) &&
            Objects.equals(taxAuthRateId, that.taxAuthRateId) &&
            Objects.equals(parentInvoiceId, that.parentInvoiceId) &&
            Objects.equals(parentInvoiceItemId, that.parentInvoiceItemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sequenceNo,
            quantity,
            amount,
            cancelQuantity,
            selectedAmount,
            unitPrice,
            unitListPrice,
            isModifiedPrice,
            estimatedShipDate,
            estimatedDeliveryDate,
            shipBeforeDate,
            shipAfterDate,
            entryDate,
            attr1,
            attr2,
            attr3,
            invoiceId,
            invoiceItemTypeId,
            productId,
            uomId,
            productFeatureId,
            productFacilityId,
            fromInventoryItemId,
            taxAuthRateId,
            parentInvoiceId,
            parentInvoiceItemId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalCancelQuantity().map(f -> "cancelQuantity=" + f + ", ").orElse("") +
            optionalSelectedAmount().map(f -> "selectedAmount=" + f + ", ").orElse("") +
            optionalUnitPrice().map(f -> "unitPrice=" + f + ", ").orElse("") +
            optionalUnitListPrice().map(f -> "unitListPrice=" + f + ", ").orElse("") +
            optionalIsModifiedPrice().map(f -> "isModifiedPrice=" + f + ", ").orElse("") +
            optionalEstimatedShipDate().map(f -> "estimatedShipDate=" + f + ", ").orElse("") +
            optionalEstimatedDeliveryDate().map(f -> "estimatedDeliveryDate=" + f + ", ").orElse("") +
            optionalShipBeforeDate().map(f -> "shipBeforeDate=" + f + ", ").orElse("") +
            optionalShipAfterDate().map(f -> "shipAfterDate=" + f + ", ").orElse("") +
            optionalEntryDate().map(f -> "entryDate=" + f + ", ").orElse("") +
            optionalAttr1().map(f -> "attr1=" + f + ", ").orElse("") +
            optionalAttr2().map(f -> "attr2=" + f + ", ").orElse("") +
            optionalAttr3().map(f -> "attr3=" + f + ", ").orElse("") +
            optionalInvoiceId().map(f -> "invoiceId=" + f + ", ").orElse("") +
            optionalInvoiceItemTypeId().map(f -> "invoiceItemTypeId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalUomId().map(f -> "uomId=" + f + ", ").orElse("") +
            optionalProductFeatureId().map(f -> "productFeatureId=" + f + ", ").orElse("") +
            optionalProductFacilityId().map(f -> "productFacilityId=" + f + ", ").orElse("") +
            optionalFromInventoryItemId().map(f -> "fromInventoryItemId=" + f + ", ").orElse("") +
            optionalTaxAuthRateId().map(f -> "taxAuthRateId=" + f + ", ").orElse("") +
            optionalParentInvoiceId().map(f -> "parentInvoiceId=" + f + ", ").orElse("") +
            optionalParentInvoiceItemId().map(f -> "parentInvoiceItemId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
