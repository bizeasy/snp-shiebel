package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.CustRequest} entity. This class is used
 * in {@link com.snp.web.rest.CustRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cust-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustRequestCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private IntegerFilter priority;

    private InstantFilter custRequestDate;

    private InstantFilter responseRequiredDate;

    private InstantFilter openDateTime;

    private InstantFilter closedDateTime;

    private StringFilter internalComment;

    private BooleanFilter billed;

    private IntegerFilter custEstimatedDurationDays;

    private LongFilter custRequestTypeId;

    private LongFilter custRequestCategoryId;

    private LongFilter statusId;

    private LongFilter fromPartyId;

    private LongFilter productStoreId;

    private LongFilter salesChannelId;

    private LongFilter currencyUomId;

    private Boolean distinct;

    public CustRequestCriteria() {}

    public CustRequestCriteria(CustRequestCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(IntegerFilter::copy).orElse(null);
        this.custRequestDate = other.optionalCustRequestDate().map(InstantFilter::copy).orElse(null);
        this.responseRequiredDate = other.optionalResponseRequiredDate().map(InstantFilter::copy).orElse(null);
        this.openDateTime = other.optionalOpenDateTime().map(InstantFilter::copy).orElse(null);
        this.closedDateTime = other.optionalClosedDateTime().map(InstantFilter::copy).orElse(null);
        this.internalComment = other.optionalInternalComment().map(StringFilter::copy).orElse(null);
        this.billed = other.optionalBilled().map(BooleanFilter::copy).orElse(null);
        this.custEstimatedDurationDays = other.optionalCustEstimatedDurationDays().map(IntegerFilter::copy).orElse(null);
        this.custRequestTypeId = other.optionalCustRequestTypeId().map(LongFilter::copy).orElse(null);
        this.custRequestCategoryId = other.optionalCustRequestCategoryId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.fromPartyId = other.optionalFromPartyId().map(LongFilter::copy).orElse(null);
        this.productStoreId = other.optionalProductStoreId().map(LongFilter::copy).orElse(null);
        this.salesChannelId = other.optionalSalesChannelId().map(LongFilter::copy).orElse(null);
        this.currencyUomId = other.optionalCurrencyUomId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CustRequestCriteria copy() {
        return new CustRequestCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public InstantFilter getCustRequestDate() {
        return custRequestDate;
    }

    public Optional<InstantFilter> optionalCustRequestDate() {
        return Optional.ofNullable(custRequestDate);
    }

    public InstantFilter custRequestDate() {
        if (custRequestDate == null) {
            setCustRequestDate(new InstantFilter());
        }
        return custRequestDate;
    }

    public void setCustRequestDate(InstantFilter custRequestDate) {
        this.custRequestDate = custRequestDate;
    }

    public InstantFilter getResponseRequiredDate() {
        return responseRequiredDate;
    }

    public Optional<InstantFilter> optionalResponseRequiredDate() {
        return Optional.ofNullable(responseRequiredDate);
    }

    public InstantFilter responseRequiredDate() {
        if (responseRequiredDate == null) {
            setResponseRequiredDate(new InstantFilter());
        }
        return responseRequiredDate;
    }

    public void setResponseRequiredDate(InstantFilter responseRequiredDate) {
        this.responseRequiredDate = responseRequiredDate;
    }

    public InstantFilter getOpenDateTime() {
        return openDateTime;
    }

    public Optional<InstantFilter> optionalOpenDateTime() {
        return Optional.ofNullable(openDateTime);
    }

    public InstantFilter openDateTime() {
        if (openDateTime == null) {
            setOpenDateTime(new InstantFilter());
        }
        return openDateTime;
    }

    public void setOpenDateTime(InstantFilter openDateTime) {
        this.openDateTime = openDateTime;
    }

    public InstantFilter getClosedDateTime() {
        return closedDateTime;
    }

    public Optional<InstantFilter> optionalClosedDateTime() {
        return Optional.ofNullable(closedDateTime);
    }

    public InstantFilter closedDateTime() {
        if (closedDateTime == null) {
            setClosedDateTime(new InstantFilter());
        }
        return closedDateTime;
    }

    public void setClosedDateTime(InstantFilter closedDateTime) {
        this.closedDateTime = closedDateTime;
    }

    public StringFilter getInternalComment() {
        return internalComment;
    }

    public Optional<StringFilter> optionalInternalComment() {
        return Optional.ofNullable(internalComment);
    }

    public StringFilter internalComment() {
        if (internalComment == null) {
            setInternalComment(new StringFilter());
        }
        return internalComment;
    }

    public void setInternalComment(StringFilter internalComment) {
        this.internalComment = internalComment;
    }

    public BooleanFilter getBilled() {
        return billed;
    }

    public Optional<BooleanFilter> optionalBilled() {
        return Optional.ofNullable(billed);
    }

    public BooleanFilter billed() {
        if (billed == null) {
            setBilled(new BooleanFilter());
        }
        return billed;
    }

    public void setBilled(BooleanFilter billed) {
        this.billed = billed;
    }

    public IntegerFilter getCustEstimatedDurationDays() {
        return custEstimatedDurationDays;
    }

    public Optional<IntegerFilter> optionalCustEstimatedDurationDays() {
        return Optional.ofNullable(custEstimatedDurationDays);
    }

    public IntegerFilter custEstimatedDurationDays() {
        if (custEstimatedDurationDays == null) {
            setCustEstimatedDurationDays(new IntegerFilter());
        }
        return custEstimatedDurationDays;
    }

    public void setCustEstimatedDurationDays(IntegerFilter custEstimatedDurationDays) {
        this.custEstimatedDurationDays = custEstimatedDurationDays;
    }

    public LongFilter getCustRequestTypeId() {
        return custRequestTypeId;
    }

    public Optional<LongFilter> optionalCustRequestTypeId() {
        return Optional.ofNullable(custRequestTypeId);
    }

    public LongFilter custRequestTypeId() {
        if (custRequestTypeId == null) {
            setCustRequestTypeId(new LongFilter());
        }
        return custRequestTypeId;
    }

    public void setCustRequestTypeId(LongFilter custRequestTypeId) {
        this.custRequestTypeId = custRequestTypeId;
    }

    public LongFilter getCustRequestCategoryId() {
        return custRequestCategoryId;
    }

    public Optional<LongFilter> optionalCustRequestCategoryId() {
        return Optional.ofNullable(custRequestCategoryId);
    }

    public LongFilter custRequestCategoryId() {
        if (custRequestCategoryId == null) {
            setCustRequestCategoryId(new LongFilter());
        }
        return custRequestCategoryId;
    }

    public void setCustRequestCategoryId(LongFilter custRequestCategoryId) {
        this.custRequestCategoryId = custRequestCategoryId;
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

    public LongFilter getFromPartyId() {
        return fromPartyId;
    }

    public Optional<LongFilter> optionalFromPartyId() {
        return Optional.ofNullable(fromPartyId);
    }

    public LongFilter fromPartyId() {
        if (fromPartyId == null) {
            setFromPartyId(new LongFilter());
        }
        return fromPartyId;
    }

    public void setFromPartyId(LongFilter fromPartyId) {
        this.fromPartyId = fromPartyId;
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
        final CustRequestCriteria that = (CustRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(custRequestDate, that.custRequestDate) &&
            Objects.equals(responseRequiredDate, that.responseRequiredDate) &&
            Objects.equals(openDateTime, that.openDateTime) &&
            Objects.equals(closedDateTime, that.closedDateTime) &&
            Objects.equals(internalComment, that.internalComment) &&
            Objects.equals(billed, that.billed) &&
            Objects.equals(custEstimatedDurationDays, that.custEstimatedDurationDays) &&
            Objects.equals(custRequestTypeId, that.custRequestTypeId) &&
            Objects.equals(custRequestCategoryId, that.custRequestCategoryId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(fromPartyId, that.fromPartyId) &&
            Objects.equals(productStoreId, that.productStoreId) &&
            Objects.equals(salesChannelId, that.salesChannelId) &&
            Objects.equals(currencyUomId, that.currencyUomId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            priority,
            custRequestDate,
            responseRequiredDate,
            openDateTime,
            closedDateTime,
            internalComment,
            billed,
            custEstimatedDurationDays,
            custRequestTypeId,
            custRequestCategoryId,
            statusId,
            fromPartyId,
            productStoreId,
            salesChannelId,
            currencyUomId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustRequestCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalCustRequestDate().map(f -> "custRequestDate=" + f + ", ").orElse("") +
            optionalResponseRequiredDate().map(f -> "responseRequiredDate=" + f + ", ").orElse("") +
            optionalOpenDateTime().map(f -> "openDateTime=" + f + ", ").orElse("") +
            optionalClosedDateTime().map(f -> "closedDateTime=" + f + ", ").orElse("") +
            optionalInternalComment().map(f -> "internalComment=" + f + ", ").orElse("") +
            optionalBilled().map(f -> "billed=" + f + ", ").orElse("") +
            optionalCustEstimatedDurationDays().map(f -> "custEstimatedDurationDays=" + f + ", ").orElse("") +
            optionalCustRequestTypeId().map(f -> "custRequestTypeId=" + f + ", ").orElse("") +
            optionalCustRequestCategoryId().map(f -> "custRequestCategoryId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalFromPartyId().map(f -> "fromPartyId=" + f + ", ").orElse("") +
            optionalProductStoreId().map(f -> "productStoreId=" + f + ", ").orElse("") +
            optionalSalesChannelId().map(f -> "salesChannelId=" + f + ", ").orElse("") +
            optionalCurrencyUomId().map(f -> "currencyUomId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
