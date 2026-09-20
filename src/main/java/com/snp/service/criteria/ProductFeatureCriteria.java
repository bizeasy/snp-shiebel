package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ProductFeature} entity. This class is used
 * in {@link com.snp.web.rest.ProductFeatureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-features?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductFeatureCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private BigDecimalFilter amount;

    private IntegerFilter sequenceNo;

    private StringFilter abbrev;

    private LongFilter productFeatureTypeId;

    private LongFilter productFeatureCategoryId;

    private Boolean distinct;

    public ProductFeatureCriteria() {}

    public ProductFeatureCriteria(ProductFeatureCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.abbrev = other.optionalAbbrev().map(StringFilter::copy).orElse(null);
        this.productFeatureTypeId = other.optionalProductFeatureTypeId().map(LongFilter::copy).orElse(null);
        this.productFeatureCategoryId = other.optionalProductFeatureCategoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductFeatureCriteria copy() {
        return new ProductFeatureCriteria(this);
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

    public StringFilter getAbbrev() {
        return abbrev;
    }

    public Optional<StringFilter> optionalAbbrev() {
        return Optional.ofNullable(abbrev);
    }

    public StringFilter abbrev() {
        if (abbrev == null) {
            setAbbrev(new StringFilter());
        }
        return abbrev;
    }

    public void setAbbrev(StringFilter abbrev) {
        this.abbrev = abbrev;
    }

    public LongFilter getProductFeatureTypeId() {
        return productFeatureTypeId;
    }

    public Optional<LongFilter> optionalProductFeatureTypeId() {
        return Optional.ofNullable(productFeatureTypeId);
    }

    public LongFilter productFeatureTypeId() {
        if (productFeatureTypeId == null) {
            setProductFeatureTypeId(new LongFilter());
        }
        return productFeatureTypeId;
    }

    public void setProductFeatureTypeId(LongFilter productFeatureTypeId) {
        this.productFeatureTypeId = productFeatureTypeId;
    }

    public LongFilter getProductFeatureCategoryId() {
        return productFeatureCategoryId;
    }

    public Optional<LongFilter> optionalProductFeatureCategoryId() {
        return Optional.ofNullable(productFeatureCategoryId);
    }

    public LongFilter productFeatureCategoryId() {
        if (productFeatureCategoryId == null) {
            setProductFeatureCategoryId(new LongFilter());
        }
        return productFeatureCategoryId;
    }

    public void setProductFeatureCategoryId(LongFilter productFeatureCategoryId) {
        this.productFeatureCategoryId = productFeatureCategoryId;
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
        final ProductFeatureCriteria that = (ProductFeatureCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(abbrev, that.abbrev) &&
            Objects.equals(productFeatureTypeId, that.productFeatureTypeId) &&
            Objects.equals(productFeatureCategoryId, that.productFeatureCategoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, amount, sequenceNo, abbrev, productFeatureTypeId, productFeatureCategoryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductFeatureCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalAbbrev().map(f -> "abbrev=" + f + ", ").orElse("") +
            optionalProductFeatureTypeId().map(f -> "productFeatureTypeId=" + f + ", ").orElse("") +
            optionalProductFeatureCategoryId().map(f -> "productFeatureCategoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
