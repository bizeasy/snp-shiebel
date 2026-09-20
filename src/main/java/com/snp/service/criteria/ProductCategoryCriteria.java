package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ProductCategory} entity. This class is used
 * in {@link com.snp.web.rest.ProductCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCategoryCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter longDescription;

    private StringFilter attribute;

    private IntegerFilter sequenceNo;

    private StringFilter imageUrl;

    private StringFilter altImageUrl;

    private StringFilter bannerUrl;

    private StringFilter largeImageUrl;

    private StringFilter altLargeImageUrl;

    private BooleanFilter isBanner;

    private LongFilter productCategoryTypeId;

    private LongFilter parentId;

    private Boolean distinct;

    public ProductCategoryCriteria() {}

    public ProductCategoryCriteria(ProductCategoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.longDescription = other.optionalLongDescription().map(StringFilter::copy).orElse(null);
        this.attribute = other.optionalAttribute().map(StringFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.imageUrl = other.optionalImageUrl().map(StringFilter::copy).orElse(null);
        this.altImageUrl = other.optionalAltImageUrl().map(StringFilter::copy).orElse(null);
        this.bannerUrl = other.optionalBannerUrl().map(StringFilter::copy).orElse(null);
        this.largeImageUrl = other.optionalLargeImageUrl().map(StringFilter::copy).orElse(null);
        this.altLargeImageUrl = other.optionalAltLargeImageUrl().map(StringFilter::copy).orElse(null);
        this.isBanner = other.optionalIsBanner().map(BooleanFilter::copy).orElse(null);
        this.productCategoryTypeId = other.optionalProductCategoryTypeId().map(LongFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductCategoryCriteria copy() {
        return new ProductCategoryCriteria(this);
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

    public StringFilter getLongDescription() {
        return longDescription;
    }

    public Optional<StringFilter> optionalLongDescription() {
        return Optional.ofNullable(longDescription);
    }

    public StringFilter longDescription() {
        if (longDescription == null) {
            setLongDescription(new StringFilter());
        }
        return longDescription;
    }

    public void setLongDescription(StringFilter longDescription) {
        this.longDescription = longDescription;
    }

    public StringFilter getAttribute() {
        return attribute;
    }

    public Optional<StringFilter> optionalAttribute() {
        return Optional.ofNullable(attribute);
    }

    public StringFilter attribute() {
        if (attribute == null) {
            setAttribute(new StringFilter());
        }
        return attribute;
    }

    public void setAttribute(StringFilter attribute) {
        this.attribute = attribute;
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

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public Optional<StringFilter> optionalImageUrl() {
        return Optional.ofNullable(imageUrl);
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            setImageUrl(new StringFilter());
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StringFilter getAltImageUrl() {
        return altImageUrl;
    }

    public Optional<StringFilter> optionalAltImageUrl() {
        return Optional.ofNullable(altImageUrl);
    }

    public StringFilter altImageUrl() {
        if (altImageUrl == null) {
            setAltImageUrl(new StringFilter());
        }
        return altImageUrl;
    }

    public void setAltImageUrl(StringFilter altImageUrl) {
        this.altImageUrl = altImageUrl;
    }

    public StringFilter getBannerUrl() {
        return bannerUrl;
    }

    public Optional<StringFilter> optionalBannerUrl() {
        return Optional.ofNullable(bannerUrl);
    }

    public StringFilter bannerUrl() {
        if (bannerUrl == null) {
            setBannerUrl(new StringFilter());
        }
        return bannerUrl;
    }

    public void setBannerUrl(StringFilter bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public StringFilter getLargeImageUrl() {
        return largeImageUrl;
    }

    public Optional<StringFilter> optionalLargeImageUrl() {
        return Optional.ofNullable(largeImageUrl);
    }

    public StringFilter largeImageUrl() {
        if (largeImageUrl == null) {
            setLargeImageUrl(new StringFilter());
        }
        return largeImageUrl;
    }

    public void setLargeImageUrl(StringFilter largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public StringFilter getAltLargeImageUrl() {
        return altLargeImageUrl;
    }

    public Optional<StringFilter> optionalAltLargeImageUrl() {
        return Optional.ofNullable(altLargeImageUrl);
    }

    public StringFilter altLargeImageUrl() {
        if (altLargeImageUrl == null) {
            setAltLargeImageUrl(new StringFilter());
        }
        return altLargeImageUrl;
    }

    public void setAltLargeImageUrl(StringFilter altLargeImageUrl) {
        this.altLargeImageUrl = altLargeImageUrl;
    }

    public BooleanFilter getIsBanner() {
        return isBanner;
    }

    public Optional<BooleanFilter> optionalIsBanner() {
        return Optional.ofNullable(isBanner);
    }

    public BooleanFilter isBanner() {
        if (isBanner == null) {
            setIsBanner(new BooleanFilter());
        }
        return isBanner;
    }

    public void setIsBanner(BooleanFilter isBanner) {
        this.isBanner = isBanner;
    }

    public LongFilter getProductCategoryTypeId() {
        return productCategoryTypeId;
    }

    public Optional<LongFilter> optionalProductCategoryTypeId() {
        return Optional.ofNullable(productCategoryTypeId);
    }

    public LongFilter productCategoryTypeId() {
        if (productCategoryTypeId == null) {
            setProductCategoryTypeId(new LongFilter());
        }
        return productCategoryTypeId;
    }

    public void setProductCategoryTypeId(LongFilter productCategoryTypeId) {
        this.productCategoryTypeId = productCategoryTypeId;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public Optional<LongFilter> optionalParentId() {
        return Optional.ofNullable(parentId);
    }

    public LongFilter parentId() {
        if (parentId == null) {
            setParentId(new LongFilter());
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
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
        final ProductCategoryCriteria that = (ProductCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(longDescription, that.longDescription) &&
            Objects.equals(attribute, that.attribute) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(altImageUrl, that.altImageUrl) &&
            Objects.equals(bannerUrl, that.bannerUrl) &&
            Objects.equals(largeImageUrl, that.largeImageUrl) &&
            Objects.equals(altLargeImageUrl, that.altLargeImageUrl) &&
            Objects.equals(isBanner, that.isBanner) &&
            Objects.equals(productCategoryTypeId, that.productCategoryTypeId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            longDescription,
            attribute,
            sequenceNo,
            imageUrl,
            altImageUrl,
            bannerUrl,
            largeImageUrl,
            altLargeImageUrl,
            isBanner,
            productCategoryTypeId,
            parentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCategoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalLongDescription().map(f -> "longDescription=" + f + ", ").orElse("") +
            optionalAttribute().map(f -> "attribute=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalImageUrl().map(f -> "imageUrl=" + f + ", ").orElse("") +
            optionalAltImageUrl().map(f -> "altImageUrl=" + f + ", ").orElse("") +
            optionalBannerUrl().map(f -> "bannerUrl=" + f + ", ").orElse("") +
            optionalLargeImageUrl().map(f -> "largeImageUrl=" + f + ", ").orElse("") +
            optionalAltLargeImageUrl().map(f -> "altLargeImageUrl=" + f + ", ").orElse("") +
            optionalIsBanner().map(f -> "isBanner=" + f + ", ").orElse("") +
            optionalProductCategoryTypeId().map(f -> "productCategoryTypeId=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
