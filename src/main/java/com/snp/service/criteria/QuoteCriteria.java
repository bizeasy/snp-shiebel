package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.Quote} entity. This class is used
 * in {@link com.snp.web.rest.QuoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quotes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuoteCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter offerNo;

    private InstantFilter issueDate;

    private InstantFilter validFromDate;

    private InstantFilter validThruDate;

    private StringFilter description;

    private BigDecimalFilter offerValue;

    private StringFilter remarks;

    private LongFilter quoteTypeId;

    private LongFilter custRequestId;

    private LongFilter partyId;

    private LongFilter statusId;

    private LongFilter productStoreId;

    private LongFilter salesChannelId;

    private LongFilter currencyUomId;

    private LongFilter currencyConversionId;

    private Boolean distinct;

    public QuoteCriteria() {}

    public QuoteCriteria(QuoteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.offerNo = other.optionalOfferNo().map(StringFilter::copy).orElse(null);
        this.issueDate = other.optionalIssueDate().map(InstantFilter::copy).orElse(null);
        this.validFromDate = other.optionalValidFromDate().map(InstantFilter::copy).orElse(null);
        this.validThruDate = other.optionalValidThruDate().map(InstantFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.offerValue = other.optionalOfferValue().map(BigDecimalFilter::copy).orElse(null);
        this.remarks = other.optionalRemarks().map(StringFilter::copy).orElse(null);
        this.quoteTypeId = other.optionalQuoteTypeId().map(LongFilter::copy).orElse(null);
        this.custRequestId = other.optionalCustRequestId().map(LongFilter::copy).orElse(null);
        this.partyId = other.optionalPartyId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.productStoreId = other.optionalProductStoreId().map(LongFilter::copy).orElse(null);
        this.salesChannelId = other.optionalSalesChannelId().map(LongFilter::copy).orElse(null);
        this.currencyUomId = other.optionalCurrencyUomId().map(LongFilter::copy).orElse(null);
        this.currencyConversionId = other.optionalCurrencyConversionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public QuoteCriteria copy() {
        return new QuoteCriteria(this);
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

    public StringFilter getOfferNo() {
        return offerNo;
    }

    public Optional<StringFilter> optionalOfferNo() {
        return Optional.ofNullable(offerNo);
    }

    public StringFilter offerNo() {
        if (offerNo == null) {
            setOfferNo(new StringFilter());
        }
        return offerNo;
    }

    public void setOfferNo(StringFilter offerNo) {
        this.offerNo = offerNo;
    }

    public InstantFilter getIssueDate() {
        return issueDate;
    }

    public Optional<InstantFilter> optionalIssueDate() {
        return Optional.ofNullable(issueDate);
    }

    public InstantFilter issueDate() {
        if (issueDate == null) {
            setIssueDate(new InstantFilter());
        }
        return issueDate;
    }

    public void setIssueDate(InstantFilter issueDate) {
        this.issueDate = issueDate;
    }

    public InstantFilter getValidFromDate() {
        return validFromDate;
    }

    public Optional<InstantFilter> optionalValidFromDate() {
        return Optional.ofNullable(validFromDate);
    }

    public InstantFilter validFromDate() {
        if (validFromDate == null) {
            setValidFromDate(new InstantFilter());
        }
        return validFromDate;
    }

    public void setValidFromDate(InstantFilter validFromDate) {
        this.validFromDate = validFromDate;
    }

    public InstantFilter getValidThruDate() {
        return validThruDate;
    }

    public Optional<InstantFilter> optionalValidThruDate() {
        return Optional.ofNullable(validThruDate);
    }

    public InstantFilter validThruDate() {
        if (validThruDate == null) {
            setValidThruDate(new InstantFilter());
        }
        return validThruDate;
    }

    public void setValidThruDate(InstantFilter validThruDate) {
        this.validThruDate = validThruDate;
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

    public BigDecimalFilter getOfferValue() {
        return offerValue;
    }

    public Optional<BigDecimalFilter> optionalOfferValue() {
        return Optional.ofNullable(offerValue);
    }

    public BigDecimalFilter offerValue() {
        if (offerValue == null) {
            setOfferValue(new BigDecimalFilter());
        }
        return offerValue;
    }

    public void setOfferValue(BigDecimalFilter offerValue) {
        this.offerValue = offerValue;
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

    public LongFilter getQuoteTypeId() {
        return quoteTypeId;
    }

    public Optional<LongFilter> optionalQuoteTypeId() {
        return Optional.ofNullable(quoteTypeId);
    }

    public LongFilter quoteTypeId() {
        if (quoteTypeId == null) {
            setQuoteTypeId(new LongFilter());
        }
        return quoteTypeId;
    }

    public void setQuoteTypeId(LongFilter quoteTypeId) {
        this.quoteTypeId = quoteTypeId;
    }

    public LongFilter getCustRequestId() {
        return custRequestId;
    }

    public Optional<LongFilter> optionalCustRequestId() {
        return Optional.ofNullable(custRequestId);
    }

    public LongFilter custRequestId() {
        if (custRequestId == null) {
            setCustRequestId(new LongFilter());
        }
        return custRequestId;
    }

    public void setCustRequestId(LongFilter custRequestId) {
        this.custRequestId = custRequestId;
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
        final QuoteCriteria that = (QuoteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(offerNo, that.offerNo) &&
            Objects.equals(issueDate, that.issueDate) &&
            Objects.equals(validFromDate, that.validFromDate) &&
            Objects.equals(validThruDate, that.validThruDate) &&
            Objects.equals(description, that.description) &&
            Objects.equals(offerValue, that.offerValue) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(quoteTypeId, that.quoteTypeId) &&
            Objects.equals(custRequestId, that.custRequestId) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(productStoreId, that.productStoreId) &&
            Objects.equals(salesChannelId, that.salesChannelId) &&
            Objects.equals(currencyUomId, that.currencyUomId) &&
            Objects.equals(currencyConversionId, that.currencyConversionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            offerNo,
            issueDate,
            validFromDate,
            validThruDate,
            description,
            offerValue,
            remarks,
            quoteTypeId,
            custRequestId,
            partyId,
            statusId,
            productStoreId,
            salesChannelId,
            currencyUomId,
            currencyConversionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuoteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalOfferNo().map(f -> "offerNo=" + f + ", ").orElse("") +
            optionalIssueDate().map(f -> "issueDate=" + f + ", ").orElse("") +
            optionalValidFromDate().map(f -> "validFromDate=" + f + ", ").orElse("") +
            optionalValidThruDate().map(f -> "validThruDate=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalOfferValue().map(f -> "offerValue=" + f + ", ").orElse("") +
            optionalRemarks().map(f -> "remarks=" + f + ", ").orElse("") +
            optionalQuoteTypeId().map(f -> "quoteTypeId=" + f + ", ").orElse("") +
            optionalCustRequestId().map(f -> "custRequestId=" + f + ", ").orElse("") +
            optionalPartyId().map(f -> "partyId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalProductStoreId().map(f -> "productStoreId=" + f + ", ").orElse("") +
            optionalSalesChannelId().map(f -> "salesChannelId=" + f + ", ").orElse("") +
            optionalCurrencyUomId().map(f -> "currencyUomId=" + f + ", ").orElse("") +
            optionalCurrencyConversionId().map(f -> "currencyConversionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
