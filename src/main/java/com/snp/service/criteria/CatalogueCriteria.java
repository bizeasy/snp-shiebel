package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.Catalogue} entity. This class is used
 * in {@link com.snp.web.rest.CatalogueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /catalogues?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CatalogueCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private IntegerFilter sequenceNo;

    private StringFilter imagePath;

    private StringFilter mobileImagePath;

    private StringFilter altImage1;

    private StringFilter altImage2;

    private StringFilter altImage3;

    private StringFilter bannerUrl;

    private BooleanFilter isBanner;

    private Boolean distinct;

    public CatalogueCriteria() {}

    public CatalogueCriteria(CatalogueCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.imagePath = other.optionalImagePath().map(StringFilter::copy).orElse(null);
        this.mobileImagePath = other.optionalMobileImagePath().map(StringFilter::copy).orElse(null);
        this.altImage1 = other.optionalAltImage1().map(StringFilter::copy).orElse(null);
        this.altImage2 = other.optionalAltImage2().map(StringFilter::copy).orElse(null);
        this.altImage3 = other.optionalAltImage3().map(StringFilter::copy).orElse(null);
        this.bannerUrl = other.optionalBannerUrl().map(StringFilter::copy).orElse(null);
        this.isBanner = other.optionalIsBanner().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CatalogueCriteria copy() {
        return new CatalogueCriteria(this);
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

    public StringFilter getImagePath() {
        return imagePath;
    }

    public Optional<StringFilter> optionalImagePath() {
        return Optional.ofNullable(imagePath);
    }

    public StringFilter imagePath() {
        if (imagePath == null) {
            setImagePath(new StringFilter());
        }
        return imagePath;
    }

    public void setImagePath(StringFilter imagePath) {
        this.imagePath = imagePath;
    }

    public StringFilter getMobileImagePath() {
        return mobileImagePath;
    }

    public Optional<StringFilter> optionalMobileImagePath() {
        return Optional.ofNullable(mobileImagePath);
    }

    public StringFilter mobileImagePath() {
        if (mobileImagePath == null) {
            setMobileImagePath(new StringFilter());
        }
        return mobileImagePath;
    }

    public void setMobileImagePath(StringFilter mobileImagePath) {
        this.mobileImagePath = mobileImagePath;
    }

    public StringFilter getAltImage1() {
        return altImage1;
    }

    public Optional<StringFilter> optionalAltImage1() {
        return Optional.ofNullable(altImage1);
    }

    public StringFilter altImage1() {
        if (altImage1 == null) {
            setAltImage1(new StringFilter());
        }
        return altImage1;
    }

    public void setAltImage1(StringFilter altImage1) {
        this.altImage1 = altImage1;
    }

    public StringFilter getAltImage2() {
        return altImage2;
    }

    public Optional<StringFilter> optionalAltImage2() {
        return Optional.ofNullable(altImage2);
    }

    public StringFilter altImage2() {
        if (altImage2 == null) {
            setAltImage2(new StringFilter());
        }
        return altImage2;
    }

    public void setAltImage2(StringFilter altImage2) {
        this.altImage2 = altImage2;
    }

    public StringFilter getAltImage3() {
        return altImage3;
    }

    public Optional<StringFilter> optionalAltImage3() {
        return Optional.ofNullable(altImage3);
    }

    public StringFilter altImage3() {
        if (altImage3 == null) {
            setAltImage3(new StringFilter());
        }
        return altImage3;
    }

    public void setAltImage3(StringFilter altImage3) {
        this.altImage3 = altImage3;
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
        final CatalogueCriteria that = (CatalogueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(imagePath, that.imagePath) &&
            Objects.equals(mobileImagePath, that.mobileImagePath) &&
            Objects.equals(altImage1, that.altImage1) &&
            Objects.equals(altImage2, that.altImage2) &&
            Objects.equals(altImage3, that.altImage3) &&
            Objects.equals(bannerUrl, that.bannerUrl) &&
            Objects.equals(isBanner, that.isBanner) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            sequenceNo,
            imagePath,
            mobileImagePath,
            altImage1,
            altImage2,
            altImage3,
            bannerUrl,
            isBanner,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogueCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalImagePath().map(f -> "imagePath=" + f + ", ").orElse("") +
            optionalMobileImagePath().map(f -> "mobileImagePath=" + f + ", ").orElse("") +
            optionalAltImage1().map(f -> "altImage1=" + f + ", ").orElse("") +
            optionalAltImage2().map(f -> "altImage2=" + f + ", ").orElse("") +
            optionalAltImage3().map(f -> "altImage3=" + f + ", ").orElse("") +
            optionalBannerUrl().map(f -> "bannerUrl=" + f + ", ").orElse("") +
            optionalIsBanner().map(f -> "isBanner=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
