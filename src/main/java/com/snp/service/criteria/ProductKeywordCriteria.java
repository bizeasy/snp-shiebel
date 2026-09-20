package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ProductKeyword} entity. This class is used
 * in {@link com.snp.web.rest.ProductKeywordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-keywords?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductKeywordCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter keyword;

    private IntegerFilter relevancyWeight;

    private LongFilter productId;

    private LongFilter keywordTypeId;

    private Boolean distinct;

    public ProductKeywordCriteria() {}

    public ProductKeywordCriteria(ProductKeywordCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.keyword = other.optionalKeyword().map(StringFilter::copy).orElse(null);
        this.relevancyWeight = other.optionalRelevancyWeight().map(IntegerFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.keywordTypeId = other.optionalKeywordTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductKeywordCriteria copy() {
        return new ProductKeywordCriteria(this);
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

    public StringFilter getKeyword() {
        return keyword;
    }

    public Optional<StringFilter> optionalKeyword() {
        return Optional.ofNullable(keyword);
    }

    public StringFilter keyword() {
        if (keyword == null) {
            setKeyword(new StringFilter());
        }
        return keyword;
    }

    public void setKeyword(StringFilter keyword) {
        this.keyword = keyword;
    }

    public IntegerFilter getRelevancyWeight() {
        return relevancyWeight;
    }

    public Optional<IntegerFilter> optionalRelevancyWeight() {
        return Optional.ofNullable(relevancyWeight);
    }

    public IntegerFilter relevancyWeight() {
        if (relevancyWeight == null) {
            setRelevancyWeight(new IntegerFilter());
        }
        return relevancyWeight;
    }

    public void setRelevancyWeight(IntegerFilter relevancyWeight) {
        this.relevancyWeight = relevancyWeight;
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

    public LongFilter getKeywordTypeId() {
        return keywordTypeId;
    }

    public Optional<LongFilter> optionalKeywordTypeId() {
        return Optional.ofNullable(keywordTypeId);
    }

    public LongFilter keywordTypeId() {
        if (keywordTypeId == null) {
            setKeywordTypeId(new LongFilter());
        }
        return keywordTypeId;
    }

    public void setKeywordTypeId(LongFilter keywordTypeId) {
        this.keywordTypeId = keywordTypeId;
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
        final ProductKeywordCriteria that = (ProductKeywordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(keyword, that.keyword) &&
            Objects.equals(relevancyWeight, that.relevancyWeight) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(keywordTypeId, that.keywordTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keyword, relevancyWeight, productId, keywordTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductKeywordCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalKeyword().map(f -> "keyword=" + f + ", ").orElse("") +
            optionalRelevancyWeight().map(f -> "relevancyWeight=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalKeywordTypeId().map(f -> "keywordTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
