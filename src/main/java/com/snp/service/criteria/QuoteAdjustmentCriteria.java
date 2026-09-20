package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.QuoteAdjustment} entity. This class is used
 * in {@link com.snp.web.rest.QuoteAdjustmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quote-adjustments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuoteAdjustmentCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter comments;

    private BigDecimalFilter amount;

    private BigDecimalFilter sourcePercentage;

    private StringFilter sourceRefId;

    private BigDecimalFilter exemptAmount;

    private BooleanFilter includeInTax;

    private BooleanFilter includeInShipping;

    private BooleanFilter includedInPrice;

    private InstantFilter entryDate;

    private LongFilter quoteId;

    private LongFilter quoteItemId;

    private LongFilter adjustmentTypeId;

    private LongFilter productId;

    private LongFilter productFeatureId;

    private LongFilter taxAuthorityId;

    private Boolean distinct;

    public QuoteAdjustmentCriteria() {}

    public QuoteAdjustmentCriteria(QuoteAdjustmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.comments = other.optionalComments().map(StringFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.sourcePercentage = other.optionalSourcePercentage().map(BigDecimalFilter::copy).orElse(null);
        this.sourceRefId = other.optionalSourceRefId().map(StringFilter::copy).orElse(null);
        this.exemptAmount = other.optionalExemptAmount().map(BigDecimalFilter::copy).orElse(null);
        this.includeInTax = other.optionalIncludeInTax().map(BooleanFilter::copy).orElse(null);
        this.includeInShipping = other.optionalIncludeInShipping().map(BooleanFilter::copy).orElse(null);
        this.includedInPrice = other.optionalIncludedInPrice().map(BooleanFilter::copy).orElse(null);
        this.entryDate = other.optionalEntryDate().map(InstantFilter::copy).orElse(null);
        this.quoteId = other.optionalQuoteId().map(LongFilter::copy).orElse(null);
        this.quoteItemId = other.optionalQuoteItemId().map(LongFilter::copy).orElse(null);
        this.adjustmentTypeId = other.optionalAdjustmentTypeId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.productFeatureId = other.optionalProductFeatureId().map(LongFilter::copy).orElse(null);
        this.taxAuthorityId = other.optionalTaxAuthorityId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public QuoteAdjustmentCriteria copy() {
        return new QuoteAdjustmentCriteria(this);
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

    public BigDecimalFilter getSourcePercentage() {
        return sourcePercentage;
    }

    public Optional<BigDecimalFilter> optionalSourcePercentage() {
        return Optional.ofNullable(sourcePercentage);
    }

    public BigDecimalFilter sourcePercentage() {
        if (sourcePercentage == null) {
            setSourcePercentage(new BigDecimalFilter());
        }
        return sourcePercentage;
    }

    public void setSourcePercentage(BigDecimalFilter sourcePercentage) {
        this.sourcePercentage = sourcePercentage;
    }

    public StringFilter getSourceRefId() {
        return sourceRefId;
    }

    public Optional<StringFilter> optionalSourceRefId() {
        return Optional.ofNullable(sourceRefId);
    }

    public StringFilter sourceRefId() {
        if (sourceRefId == null) {
            setSourceRefId(new StringFilter());
        }
        return sourceRefId;
    }

    public void setSourceRefId(StringFilter sourceRefId) {
        this.sourceRefId = sourceRefId;
    }

    public BigDecimalFilter getExemptAmount() {
        return exemptAmount;
    }

    public Optional<BigDecimalFilter> optionalExemptAmount() {
        return Optional.ofNullable(exemptAmount);
    }

    public BigDecimalFilter exemptAmount() {
        if (exemptAmount == null) {
            setExemptAmount(new BigDecimalFilter());
        }
        return exemptAmount;
    }

    public void setExemptAmount(BigDecimalFilter exemptAmount) {
        this.exemptAmount = exemptAmount;
    }

    public BooleanFilter getIncludeInTax() {
        return includeInTax;
    }

    public Optional<BooleanFilter> optionalIncludeInTax() {
        return Optional.ofNullable(includeInTax);
    }

    public BooleanFilter includeInTax() {
        if (includeInTax == null) {
            setIncludeInTax(new BooleanFilter());
        }
        return includeInTax;
    }

    public void setIncludeInTax(BooleanFilter includeInTax) {
        this.includeInTax = includeInTax;
    }

    public BooleanFilter getIncludeInShipping() {
        return includeInShipping;
    }

    public Optional<BooleanFilter> optionalIncludeInShipping() {
        return Optional.ofNullable(includeInShipping);
    }

    public BooleanFilter includeInShipping() {
        if (includeInShipping == null) {
            setIncludeInShipping(new BooleanFilter());
        }
        return includeInShipping;
    }

    public void setIncludeInShipping(BooleanFilter includeInShipping) {
        this.includeInShipping = includeInShipping;
    }

    public BooleanFilter getIncludedInPrice() {
        return includedInPrice;
    }

    public Optional<BooleanFilter> optionalIncludedInPrice() {
        return Optional.ofNullable(includedInPrice);
    }

    public BooleanFilter includedInPrice() {
        if (includedInPrice == null) {
            setIncludedInPrice(new BooleanFilter());
        }
        return includedInPrice;
    }

    public void setIncludedInPrice(BooleanFilter includedInPrice) {
        this.includedInPrice = includedInPrice;
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

    public LongFilter getQuoteItemId() {
        return quoteItemId;
    }

    public Optional<LongFilter> optionalQuoteItemId() {
        return Optional.ofNullable(quoteItemId);
    }

    public LongFilter quoteItemId() {
        if (quoteItemId == null) {
            setQuoteItemId(new LongFilter());
        }
        return quoteItemId;
    }

    public void setQuoteItemId(LongFilter quoteItemId) {
        this.quoteItemId = quoteItemId;
    }

    public LongFilter getAdjustmentTypeId() {
        return adjustmentTypeId;
    }

    public Optional<LongFilter> optionalAdjustmentTypeId() {
        return Optional.ofNullable(adjustmentTypeId);
    }

    public LongFilter adjustmentTypeId() {
        if (adjustmentTypeId == null) {
            setAdjustmentTypeId(new LongFilter());
        }
        return adjustmentTypeId;
    }

    public void setAdjustmentTypeId(LongFilter adjustmentTypeId) {
        this.adjustmentTypeId = adjustmentTypeId;
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

    public LongFilter getTaxAuthorityId() {
        return taxAuthorityId;
    }

    public Optional<LongFilter> optionalTaxAuthorityId() {
        return Optional.ofNullable(taxAuthorityId);
    }

    public LongFilter taxAuthorityId() {
        if (taxAuthorityId == null) {
            setTaxAuthorityId(new LongFilter());
        }
        return taxAuthorityId;
    }

    public void setTaxAuthorityId(LongFilter taxAuthorityId) {
        this.taxAuthorityId = taxAuthorityId;
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
        final QuoteAdjustmentCriteria that = (QuoteAdjustmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(sourcePercentage, that.sourcePercentage) &&
            Objects.equals(sourceRefId, that.sourceRefId) &&
            Objects.equals(exemptAmount, that.exemptAmount) &&
            Objects.equals(includeInTax, that.includeInTax) &&
            Objects.equals(includeInShipping, that.includeInShipping) &&
            Objects.equals(includedInPrice, that.includedInPrice) &&
            Objects.equals(entryDate, that.entryDate) &&
            Objects.equals(quoteId, that.quoteId) &&
            Objects.equals(quoteItemId, that.quoteItemId) &&
            Objects.equals(adjustmentTypeId, that.adjustmentTypeId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(productFeatureId, that.productFeatureId) &&
            Objects.equals(taxAuthorityId, that.taxAuthorityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            comments,
            amount,
            sourcePercentage,
            sourceRefId,
            exemptAmount,
            includeInTax,
            includeInShipping,
            includedInPrice,
            entryDate,
            quoteId,
            quoteItemId,
            adjustmentTypeId,
            productId,
            productFeatureId,
            taxAuthorityId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuoteAdjustmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalComments().map(f -> "comments=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalSourcePercentage().map(f -> "sourcePercentage=" + f + ", ").orElse("") +
            optionalSourceRefId().map(f -> "sourceRefId=" + f + ", ").orElse("") +
            optionalExemptAmount().map(f -> "exemptAmount=" + f + ", ").orElse("") +
            optionalIncludeInTax().map(f -> "includeInTax=" + f + ", ").orElse("") +
            optionalIncludeInShipping().map(f -> "includeInShipping=" + f + ", ").orElse("") +
            optionalIncludedInPrice().map(f -> "includedInPrice=" + f + ", ").orElse("") +
            optionalEntryDate().map(f -> "entryDate=" + f + ", ").orElse("") +
            optionalQuoteId().map(f -> "quoteId=" + f + ", ").orElse("") +
            optionalQuoteItemId().map(f -> "quoteItemId=" + f + ", ").orElse("") +
            optionalAdjustmentTypeId().map(f -> "adjustmentTypeId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalProductFeatureId().map(f -> "productFeatureId=" + f + ", ").orElse("") +
            optionalTaxAuthorityId().map(f -> "taxAuthorityId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
