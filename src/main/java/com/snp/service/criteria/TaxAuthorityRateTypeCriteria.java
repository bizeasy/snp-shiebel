package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.TaxAuthorityRateType} entity. This class is used
 * in {@link com.snp.web.rest.TaxAuthorityRateTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tax-authority-rate-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaxAuthorityRateTypeCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private LongFilter taxAuthorityId;

    private LongFilter taxSlabId;

    private Boolean distinct;

    public TaxAuthorityRateTypeCriteria() {}

    public TaxAuthorityRateTypeCriteria(TaxAuthorityRateTypeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.taxAuthorityId = other.optionalTaxAuthorityId().map(LongFilter::copy).orElse(null);
        this.taxSlabId = other.optionalTaxSlabId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TaxAuthorityRateTypeCriteria copy() {
        return new TaxAuthorityRateTypeCriteria(this);
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

    public LongFilter getTaxSlabId() {
        return taxSlabId;
    }

    public Optional<LongFilter> optionalTaxSlabId() {
        return Optional.ofNullable(taxSlabId);
    }

    public LongFilter taxSlabId() {
        if (taxSlabId == null) {
            setTaxSlabId(new LongFilter());
        }
        return taxSlabId;
    }

    public void setTaxSlabId(LongFilter taxSlabId) {
        this.taxSlabId = taxSlabId;
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
        final TaxAuthorityRateTypeCriteria that = (TaxAuthorityRateTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(taxAuthorityId, that.taxAuthorityId) &&
            Objects.equals(taxSlabId, that.taxSlabId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, taxAuthorityId, taxSlabId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxAuthorityRateTypeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalTaxAuthorityId().map(f -> "taxAuthorityId=" + f + ", ").orElse("") +
            optionalTaxSlabId().map(f -> "taxSlabId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
