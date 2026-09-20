package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.Order} entity. This class is used
 * in {@link com.snp.web.rest.OrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter orderRefNo;

    private StringFilter externalId;

    private InstantFilter orderDate;

    private InstantFilter entryDate;

    private LocalDateFilter deliveryDate;

    private IntegerFilter priority;

    private BooleanFilter isRushOrder;

    private BooleanFilter needsInventoryIssuance;

    private BigDecimalFilter remainingSubTotal;

    private BigDecimalFilter grandTotal;

    private StringFilter projectName;

    private IntegerFilter version;

    private StringFilter versionRef;

    private StringFilter remarks;

    private StringFilter attr1;

    private StringFilter attr2;

    private StringFilter attr3;

    private LongFilter orderTypeId;

    private LongFilter salesChannelId;

    private LongFilter partyId;

    private LongFilter supplierId;

    private LongFilter statusId;

    private LongFilter currencyUomId;

    private LongFilter currencyConversionId;

    private LongFilter quoteId;

    private LongFilter facilityId;

    private LongFilter productStoreId;

    private LongFilter departmentId;

    private LongFilter customTimePeriodId;

    private Boolean distinct;

    public OrderCriteria() {}

    public OrderCriteria(OrderCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.orderRefNo = other.optionalOrderRefNo().map(StringFilter::copy).orElse(null);
        this.externalId = other.optionalExternalId().map(StringFilter::copy).orElse(null);
        this.orderDate = other.optionalOrderDate().map(InstantFilter::copy).orElse(null);
        this.entryDate = other.optionalEntryDate().map(InstantFilter::copy).orElse(null);
        this.deliveryDate = other.optionalDeliveryDate().map(LocalDateFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(IntegerFilter::copy).orElse(null);
        this.isRushOrder = other.optionalIsRushOrder().map(BooleanFilter::copy).orElse(null);
        this.needsInventoryIssuance = other.optionalNeedsInventoryIssuance().map(BooleanFilter::copy).orElse(null);
        this.remainingSubTotal = other.optionalRemainingSubTotal().map(BigDecimalFilter::copy).orElse(null);
        this.grandTotal = other.optionalGrandTotal().map(BigDecimalFilter::copy).orElse(null);
        this.projectName = other.optionalProjectName().map(StringFilter::copy).orElse(null);
        this.version = other.optionalVersion().map(IntegerFilter::copy).orElse(null);
        this.versionRef = other.optionalVersionRef().map(StringFilter::copy).orElse(null);
        this.remarks = other.optionalRemarks().map(StringFilter::copy).orElse(null);
        this.attr1 = other.optionalAttr1().map(StringFilter::copy).orElse(null);
        this.attr2 = other.optionalAttr2().map(StringFilter::copy).orElse(null);
        this.attr3 = other.optionalAttr3().map(StringFilter::copy).orElse(null);
        this.orderTypeId = other.optionalOrderTypeId().map(LongFilter::copy).orElse(null);
        this.salesChannelId = other.optionalSalesChannelId().map(LongFilter::copy).orElse(null);
        this.partyId = other.optionalPartyId().map(LongFilter::copy).orElse(null);
        this.supplierId = other.optionalSupplierId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.currencyUomId = other.optionalCurrencyUomId().map(LongFilter::copy).orElse(null);
        this.currencyConversionId = other.optionalCurrencyConversionId().map(LongFilter::copy).orElse(null);
        this.quoteId = other.optionalQuoteId().map(LongFilter::copy).orElse(null);
        this.facilityId = other.optionalFacilityId().map(LongFilter::copy).orElse(null);
        this.productStoreId = other.optionalProductStoreId().map(LongFilter::copy).orElse(null);
        this.departmentId = other.optionalDepartmentId().map(LongFilter::copy).orElse(null);
        this.customTimePeriodId = other.optionalCustomTimePeriodId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OrderCriteria copy() {
        return new OrderCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getOrderRefNo() {
        return orderRefNo;
    }

    public Optional<StringFilter> optionalOrderRefNo() {
        return Optional.ofNullable(orderRefNo);
    }

    public StringFilter orderRefNo() {
        if (orderRefNo == null) {
            setOrderRefNo(new StringFilter());
        }
        return orderRefNo;
    }

    public void setOrderRefNo(StringFilter orderRefNo) {
        this.orderRefNo = orderRefNo;
    }

    public StringFilter getExternalId() {
        return externalId;
    }

    public Optional<StringFilter> optionalExternalId() {
        return Optional.ofNullable(externalId);
    }

    public StringFilter externalId() {
        if (externalId == null) {
            setExternalId(new StringFilter());
        }
        return externalId;
    }

    public void setExternalId(StringFilter externalId) {
        this.externalId = externalId;
    }

    public InstantFilter getOrderDate() {
        return orderDate;
    }

    public Optional<InstantFilter> optionalOrderDate() {
        return Optional.ofNullable(orderDate);
    }

    public InstantFilter orderDate() {
        if (orderDate == null) {
            setOrderDate(new InstantFilter());
        }
        return orderDate;
    }

    public void setOrderDate(InstantFilter orderDate) {
        this.orderDate = orderDate;
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

    public LocalDateFilter getDeliveryDate() {
        return deliveryDate;
    }

    public Optional<LocalDateFilter> optionalDeliveryDate() {
        return Optional.ofNullable(deliveryDate);
    }

    public LocalDateFilter deliveryDate() {
        if (deliveryDate == null) {
            setDeliveryDate(new LocalDateFilter());
        }
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateFilter deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public IntegerFilter getPriority() {
        return priority;
    }

    public Optional<IntegerFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public IntegerFilter priority() {
        if (priority == null) {
            setPriority(new IntegerFilter());
        }
        return priority;
    }

    public void setPriority(IntegerFilter priority) {
        this.priority = priority;
    }

    public BooleanFilter getIsRushOrder() {
        return isRushOrder;
    }

    public Optional<BooleanFilter> optionalIsRushOrder() {
        return Optional.ofNullable(isRushOrder);
    }

    public BooleanFilter isRushOrder() {
        if (isRushOrder == null) {
            setIsRushOrder(new BooleanFilter());
        }
        return isRushOrder;
    }

    public void setIsRushOrder(BooleanFilter isRushOrder) {
        this.isRushOrder = isRushOrder;
    }

    public BooleanFilter getNeedsInventoryIssuance() {
        return needsInventoryIssuance;
    }

    public Optional<BooleanFilter> optionalNeedsInventoryIssuance() {
        return Optional.ofNullable(needsInventoryIssuance);
    }

    public BooleanFilter needsInventoryIssuance() {
        if (needsInventoryIssuance == null) {
            setNeedsInventoryIssuance(new BooleanFilter());
        }
        return needsInventoryIssuance;
    }

    public void setNeedsInventoryIssuance(BooleanFilter needsInventoryIssuance) {
        this.needsInventoryIssuance = needsInventoryIssuance;
    }

    public BigDecimalFilter getRemainingSubTotal() {
        return remainingSubTotal;
    }

    public Optional<BigDecimalFilter> optionalRemainingSubTotal() {
        return Optional.ofNullable(remainingSubTotal);
    }

    public BigDecimalFilter remainingSubTotal() {
        if (remainingSubTotal == null) {
            setRemainingSubTotal(new BigDecimalFilter());
        }
        return remainingSubTotal;
    }

    public void setRemainingSubTotal(BigDecimalFilter remainingSubTotal) {
        this.remainingSubTotal = remainingSubTotal;
    }

    public BigDecimalFilter getGrandTotal() {
        return grandTotal;
    }

    public Optional<BigDecimalFilter> optionalGrandTotal() {
        return Optional.ofNullable(grandTotal);
    }

    public BigDecimalFilter grandTotal() {
        if (grandTotal == null) {
            setGrandTotal(new BigDecimalFilter());
        }
        return grandTotal;
    }

    public void setGrandTotal(BigDecimalFilter grandTotal) {
        this.grandTotal = grandTotal;
    }

    public StringFilter getProjectName() {
        return projectName;
    }

    public Optional<StringFilter> optionalProjectName() {
        return Optional.ofNullable(projectName);
    }

    public StringFilter projectName() {
        if (projectName == null) {
            setProjectName(new StringFilter());
        }
        return projectName;
    }

    public void setProjectName(StringFilter projectName) {
        this.projectName = projectName;
    }

    public IntegerFilter getVersion() {
        return version;
    }

    public Optional<IntegerFilter> optionalVersion() {
        return Optional.ofNullable(version);
    }

    public IntegerFilter version() {
        if (version == null) {
            setVersion(new IntegerFilter());
        }
        return version;
    }

    public void setVersion(IntegerFilter version) {
        this.version = version;
    }

    public StringFilter getVersionRef() {
        return versionRef;
    }

    public Optional<StringFilter> optionalVersionRef() {
        return Optional.ofNullable(versionRef);
    }

    public StringFilter versionRef() {
        if (versionRef == null) {
            setVersionRef(new StringFilter());
        }
        return versionRef;
    }

    public void setVersionRef(StringFilter versionRef) {
        this.versionRef = versionRef;
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

    public LongFilter getOrderTypeId() {
        return orderTypeId;
    }

    public Optional<LongFilter> optionalOrderTypeId() {
        return Optional.ofNullable(orderTypeId);
    }

    public LongFilter orderTypeId() {
        if (orderTypeId == null) {
            setOrderTypeId(new LongFilter());
        }
        return orderTypeId;
    }

    public void setOrderTypeId(LongFilter orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public LongFilter getSalesChannelId() {
        return salesChannelId;
    }

    public Optional<LongFilter> optionalSalesChannelId() {
        return Optional.ofNullable(salesChannelId);
    }

    public LongFilter salesChannelId() {
        if (salesChannelId == null) {
            setSalesChannelId(new LongFilter());
        }
        return salesChannelId;
    }

    public void setSalesChannelId(LongFilter salesChannelId) {
        this.salesChannelId = salesChannelId;
    }

    public LongFilter getPartyId() {
        return partyId;
    }

    public Optional<LongFilter> optionalPartyId() {
        return Optional.ofNullable(partyId);
    }

    public LongFilter partyId() {
        if (partyId == null) {
            setPartyId(new LongFilter());
        }
        return partyId;
    }

    public void setPartyId(LongFilter partyId) {
        this.partyId = partyId;
    }

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public Optional<LongFilter> optionalSupplierId() {
        return Optional.ofNullable(supplierId);
    }

    public LongFilter supplierId() {
        if (supplierId == null) {
            setSupplierId(new LongFilter());
        }
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
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

    public LongFilter getCurrencyUomId() {
        return currencyUomId;
    }

    public Optional<LongFilter> optionalCurrencyUomId() {
        return Optional.ofNullable(currencyUomId);
    }

    public LongFilter currencyUomId() {
        if (currencyUomId == null) {
            setCurrencyUomId(new LongFilter());
        }
        return currencyUomId;
    }

    public void setCurrencyUomId(LongFilter currencyUomId) {
        this.currencyUomId = currencyUomId;
    }

    public LongFilter getCurrencyConversionId() {
        return currencyConversionId;
    }

    public Optional<LongFilter> optionalCurrencyConversionId() {
        return Optional.ofNullable(currencyConversionId);
    }

    public LongFilter currencyConversionId() {
        if (currencyConversionId == null) {
            setCurrencyConversionId(new LongFilter());
        }
        return currencyConversionId;
    }

    public void setCurrencyConversionId(LongFilter currencyConversionId) {
        this.currencyConversionId = currencyConversionId;
    }

    public LongFilter getQuoteId() {
        return quoteId;
    }

    public Optional<LongFilter> optionalQuoteId() {
        return Optional.ofNullable(quoteId);
    }

    public LongFilter quoteId() {
        if (quoteId == null) {
            setQuoteId(new LongFilter());
        }
        return quoteId;
    }

    public void setQuoteId(LongFilter quoteId) {
        this.quoteId = quoteId;
    }

    public LongFilter getFacilityId() {
        return facilityId;
    }

    public Optional<LongFilter> optionalFacilityId() {
        return Optional.ofNullable(facilityId);
    }

    public LongFilter facilityId() {
        if (facilityId == null) {
            setFacilityId(new LongFilter());
        }
        return facilityId;
    }

    public void setFacilityId(LongFilter facilityId) {
        this.facilityId = facilityId;
    }

    public LongFilter getProductStoreId() {
        return productStoreId;
    }

    public Optional<LongFilter> optionalProductStoreId() {
        return Optional.ofNullable(productStoreId);
    }

    public LongFilter productStoreId() {
        if (productStoreId == null) {
            setProductStoreId(new LongFilter());
        }
        return productStoreId;
    }

    public void setProductStoreId(LongFilter productStoreId) {
        this.productStoreId = productStoreId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public Optional<LongFilter> optionalDepartmentId() {
        return Optional.ofNullable(departmentId);
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            setDepartmentId(new LongFilter());
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getCustomTimePeriodId() {
        return customTimePeriodId;
    }

    public Optional<LongFilter> optionalCustomTimePeriodId() {
        return Optional.ofNullable(customTimePeriodId);
    }

    public LongFilter customTimePeriodId() {
        if (customTimePeriodId == null) {
            setCustomTimePeriodId(new LongFilter());
        }
        return customTimePeriodId;
    }

    public void setCustomTimePeriodId(LongFilter customTimePeriodId) {
        this.customTimePeriodId = customTimePeriodId;
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
        final OrderCriteria that = (OrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(orderRefNo, that.orderRefNo) &&
            Objects.equals(externalId, that.externalId) &&
            Objects.equals(orderDate, that.orderDate) &&
            Objects.equals(entryDate, that.entryDate) &&
            Objects.equals(deliveryDate, that.deliveryDate) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(isRushOrder, that.isRushOrder) &&
            Objects.equals(needsInventoryIssuance, that.needsInventoryIssuance) &&
            Objects.equals(remainingSubTotal, that.remainingSubTotal) &&
            Objects.equals(grandTotal, that.grandTotal) &&
            Objects.equals(projectName, that.projectName) &&
            Objects.equals(version, that.version) &&
            Objects.equals(versionRef, that.versionRef) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(attr1, that.attr1) &&
            Objects.equals(attr2, that.attr2) &&
            Objects.equals(attr3, that.attr3) &&
            Objects.equals(orderTypeId, that.orderTypeId) &&
            Objects.equals(salesChannelId, that.salesChannelId) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(currencyUomId, that.currencyUomId) &&
            Objects.equals(currencyConversionId, that.currencyConversionId) &&
            Objects.equals(quoteId, that.quoteId) &&
            Objects.equals(facilityId, that.facilityId) &&
            Objects.equals(productStoreId, that.productStoreId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(customTimePeriodId, that.customTimePeriodId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            orderRefNo,
            externalId,
            orderDate,
            entryDate,
            deliveryDate,
            priority,
            isRushOrder,
            needsInventoryIssuance,
            remainingSubTotal,
            grandTotal,
            projectName,
            version,
            versionRef,
            remarks,
            attr1,
            attr2,
            attr3,
            orderTypeId,
            salesChannelId,
            partyId,
            supplierId,
            statusId,
            currencyUomId,
            currencyConversionId,
            quoteId,
            facilityId,
            productStoreId,
            departmentId,
            customTimePeriodId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalOrderRefNo().map(f -> "orderRefNo=" + f + ", ").orElse("") +
            optionalExternalId().map(f -> "externalId=" + f + ", ").orElse("") +
            optionalOrderDate().map(f -> "orderDate=" + f + ", ").orElse("") +
            optionalEntryDate().map(f -> "entryDate=" + f + ", ").orElse("") +
            optionalDeliveryDate().map(f -> "deliveryDate=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalIsRushOrder().map(f -> "isRushOrder=" + f + ", ").orElse("") +
            optionalNeedsInventoryIssuance().map(f -> "needsInventoryIssuance=" + f + ", ").orElse("") +
            optionalRemainingSubTotal().map(f -> "remainingSubTotal=" + f + ", ").orElse("") +
            optionalGrandTotal().map(f -> "grandTotal=" + f + ", ").orElse("") +
            optionalProjectName().map(f -> "projectName=" + f + ", ").orElse("") +
            optionalVersion().map(f -> "version=" + f + ", ").orElse("") +
            optionalVersionRef().map(f -> "versionRef=" + f + ", ").orElse("") +
            optionalRemarks().map(f -> "remarks=" + f + ", ").orElse("") +
            optionalAttr1().map(f -> "attr1=" + f + ", ").orElse("") +
            optionalAttr2().map(f -> "attr2=" + f + ", ").orElse("") +
            optionalAttr3().map(f -> "attr3=" + f + ", ").orElse("") +
            optionalOrderTypeId().map(f -> "orderTypeId=" + f + ", ").orElse("") +
            optionalSalesChannelId().map(f -> "salesChannelId=" + f + ", ").orElse("") +
            optionalPartyId().map(f -> "partyId=" + f + ", ").orElse("") +
            optionalSupplierId().map(f -> "supplierId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalCurrencyUomId().map(f -> "currencyUomId=" + f + ", ").orElse("") +
            optionalCurrencyConversionId().map(f -> "currencyConversionId=" + f + ", ").orElse("") +
            optionalQuoteId().map(f -> "quoteId=" + f + ", ").orElse("") +
            optionalFacilityId().map(f -> "facilityId=" + f + ", ").orElse("") +
            optionalProductStoreId().map(f -> "productStoreId=" + f + ", ").orElse("") +
            optionalDepartmentId().map(f -> "departmentId=" + f + ", ").orElse("") +
            optionalCustomTimePeriodId().map(f -> "customTimePeriodId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
