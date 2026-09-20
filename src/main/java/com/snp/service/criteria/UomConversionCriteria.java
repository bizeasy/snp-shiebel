package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.UomConversion} entity. This class is used
 * in {@link com.snp.web.rest.UomConversionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /uom-conversions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UomConversionCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BigDecimalFilter conversionFactor;

    private InstantFilter fromDate;

    private InstantFilter toDate;

    private LongFilter fromUomId;

    private LongFilter toUomId;

    private Boolean distinct;

    public UomConversionCriteria() {}

    public UomConversionCriteria(UomConversionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.conversionFactor = other.optionalConversionFactor().map(BigDecimalFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.toDate = other.optionalToDate().map(InstantFilter::copy).orElse(null);
        this.fromUomId = other.optionalFromUomId().map(LongFilter::copy).orElse(null);
        this.toUomId = other.optionalToUomId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UomConversionCriteria copy() {
        return new UomConversionCriteria(this);
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

    public BigDecimalFilter getConversionFactor() {
        return conversionFactor;
    }

    public Optional<BigDecimalFilter> optionalConversionFactor() {
        return Optional.ofNullable(conversionFactor);
    }

    public BigDecimalFilter conversionFactor() {
        if (conversionFactor == null) {
            setConversionFactor(new BigDecimalFilter());
        }
        return conversionFactor;
    }

    public void setConversionFactor(BigDecimalFilter conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public InstantFilter getFromDate() {
        return fromDate;
    }

    public Optional<InstantFilter> optionalFromDate() {
        return Optional.ofNullable(fromDate);
    }

    public InstantFilter fromDate() {
        if (fromDate == null) {
            setFromDate(new InstantFilter());
        }
        return fromDate;
    }

    public void setFromDate(InstantFilter fromDate) {
        this.fromDate = fromDate;
    }

    public InstantFilter getToDate() {
        return toDate;
    }

    public Optional<InstantFilter> optionalToDate() {
        return Optional.ofNullable(toDate);
    }

    public InstantFilter toDate() {
        if (toDate == null) {
            setToDate(new InstantFilter());
        }
        return toDate;
    }

    public void setToDate(InstantFilter toDate) {
        this.toDate = toDate;
    }

    public LongFilter getFromUomId() {
        return fromUomId;
    }

    public Optional<LongFilter> optionalFromUomId() {
        return Optional.ofNullable(fromUomId);
    }

    public LongFilter fromUomId() {
        if (fromUomId == null) {
            setFromUomId(new LongFilter());
        }
        return fromUomId;
    }

    public void setFromUomId(LongFilter fromUomId) {
        this.fromUomId = fromUomId;
    }

    public LongFilter getToUomId() {
        return toUomId;
    }

    public Optional<LongFilter> optionalToUomId() {
        return Optional.ofNullable(toUomId);
    }

    public LongFilter toUomId() {
        if (toUomId == null) {
            setToUomId(new LongFilter());
        }
        return toUomId;
    }

    public void setToUomId(LongFilter toUomId) {
        this.toUomId = toUomId;
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
        final UomConversionCriteria that = (UomConversionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(conversionFactor, that.conversionFactor) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(toDate, that.toDate) &&
            Objects.equals(fromUomId, that.fromUomId) &&
            Objects.equals(toUomId, that.toUomId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, conversionFactor, fromDate, toDate, fromUomId, toUomId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UomConversionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalConversionFactor().map(f -> "conversionFactor=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalToDate().map(f -> "toDate=" + f + ", ").orElse("") +
            optionalFromUomId().map(f -> "fromUomId=" + f + ", ").orElse("") +
            optionalToUomId().map(f -> "toUomId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
