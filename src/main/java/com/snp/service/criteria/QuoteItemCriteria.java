package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.QuoteItem} entity. This class is used
 * in {@link com.snp.web.rest.QuoteItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quote-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuoteItemCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter sequenceNo;

    private BigDecimalFilter quantity;

    private BigDecimalFilter selectedAmount;

    private BigDecimalFilter quoteUnitPrice;

    private BigDecimalFilter totalAmount;

    private InstantFilter estimatedDeliveryDate;

    private IntegerFilter leadTimeDays;

    private StringFilter comments;

    private LongFilter quoteId;

    private LongFilter productId;

    private LongFilter productFeatureId;

    private LongFilter uomId;

    private LongFilter statusId;

    private LongFilter custRequestId;

    private LongFilter custRequestItemId;

    private Boolean distinct;

    public QuoteItemCriteria() {}

    public QuoteItemCriteria(QuoteItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.selectedAmount = other.optionalSelectedAmount().map(BigDecimalFilter::copy).orElse(null);
        this.quoteUnitPrice = other.optionalQuoteUnitPrice().map(BigDecimalFilter::copy).orElse(null);
        this.totalAmount = other.optionalTotalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.estimatedDeliveryDate = other.optionalEstimatedDeliveryDate().map(InstantFilter::copy).orElse(null);
        this.leadTimeDays = other.optionalLeadTimeDays().map(IntegerFilter::copy).orElse(null);
        this.comments = other.optionalComments().map(StringFilter::copy).orElse(null);
        this.quoteId = other.optionalQuoteId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.productFeatureId = other.optionalProductFeatureId().map(LongFilter::copy).orElse(null);
        this.uomId = other.optionalUomId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.custRequestId = other.optionalCustRequestId().map(LongFilter::copy).orElse(null);
        this.custRequestItemId = other.optionalCustRequestItemId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public QuoteItemCriteria copy() {
        return new QuoteItemCriteria(this);
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

    public BigDecimalFilter getQuoteUnitPrice() {
        return quoteUnitPrice;
    }

    public Optional<BigDecimalFilter> optionalQuoteUnitPrice() {
        return Optional.ofNullable(quoteUnitPrice);
    }

    public BigDecimalFilter quoteUnitPrice() {
        if (quoteUnitPrice == null) {
            setQuoteUnitPrice(new BigDecimalFilter());
        }
        return quoteUnitPrice;
    }

    public void setQuoteUnitPrice(BigDecimalFilter quoteUnitPrice) {
        this.quoteUnitPrice = quoteUnitPrice;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public Optional<BigDecimalFilter> optionalTotalAmount() {
        return Optional.ofNullable(totalAmount);
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            setTotalAmount(new BigDecimalFilter());
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
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

    public IntegerFilter getLeadTimeDays() {
        return leadTimeDays;
    }

    public Optional<IntegerFilter> optionalLeadTimeDays() {
        return Optional.ofNullable(leadTimeDays);
    }

    public IntegerFilter leadTimeDays() {
        if (leadTimeDays == null) {
            setLeadTimeDays(new IntegerFilter());
        }
        return leadTimeDays;
    }

    public void setLeadTimeDays(IntegerFilter leadTimeDays) {
        this.leadTimeDays = leadTimeDays;
    }

    public StringFilter getComments() {
        return comments;
    }

    public Optional<StringFilter> optionalComments() {
        return Optional.ofNullable(comments);
    }

    public StringFilter comments() {
        if (comments == null) {
            setComments(new StringFilter());
        }
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
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

    public LongFilter getCustRequestItemId() {
        return custRequestItemId;
    }

    public Optional<LongFilter> optionalCustRequestItemId() {
        return Optional.ofNullable(custRequestItemId);
    }

    public LongFilter custRequestItemId() {
        if (custRequestItemId == null) {
            setCustRequestItemId(new LongFilter());
        }
        return custRequestItemId;
    }

    public void setCustRequestItemId(LongFilter custRequestItemId) {
        this.custRequestItemId = custRequestItemId;
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
        final QuoteItemCriteria that = (QuoteItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(selectedAmount, that.selectedAmount) &&
            Objects.equals(quoteUnitPrice, that.quoteUnitPrice) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(estimatedDeliveryDate, that.estimatedDeliveryDate) &&
            Objects.equals(leadTimeDays, that.leadTimeDays) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(quoteId, that.quoteId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(productFeatureId, that.productFeatureId) &&
            Objects.equals(uomId, that.uomId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(custRequestId, that.custRequestId) &&
            Objects.equals(custRequestItemId, that.custRequestItemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sequenceNo,
            quantity,
            selectedAmount,
            quoteUnitPrice,
            totalAmount,
            estimatedDeliveryDate,
            leadTimeDays,
            comments,
            quoteId,
            productId,
            productFeatureId,
            uomId,
            statusId,
            custRequestId,
            custRequestItemId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuoteItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalSelectedAmount().map(f -> "selectedAmount=" + f + ", ").orElse("") +
            optionalQuoteUnitPrice().map(f -> "quoteUnitPrice=" + f + ", ").orElse("") +
            optionalTotalAmount().map(f -> "totalAmount=" + f + ", ").orElse("") +
            optionalEstimatedDeliveryDate().map(f -> "estimatedDeliveryDate=" + f + ", ").orElse("") +
            optionalLeadTimeDays().map(f -> "leadTimeDays=" + f + ", ").orElse("") +
            optionalComments().map(f -> "comments=" + f + ", ").orElse("") +
            optionalQuoteId().map(f -> "quoteId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalProductFeatureId().map(f -> "productFeatureId=" + f + ", ").orElse("") +
            optionalUomId().map(f -> "uomId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalCustRequestId().map(f -> "custRequestId=" + f + ", ").orElse("") +
            optionalCustRequestItemId().map(f -> "custRequestItemId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
