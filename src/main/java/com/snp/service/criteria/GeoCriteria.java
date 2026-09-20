package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.Geo} entity. This class is used
 * in {@link com.snp.web.rest.GeoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /geos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GeoCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private StringFilter abbreviation;

    private LongFilter geoTypeId;

    private Boolean distinct;

    public GeoCriteria() {}

    public GeoCriteria(GeoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.abbreviation = other.optionalAbbreviation().map(StringFilter::copy).orElse(null);
        this.geoTypeId = other.optionalGeoTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public GeoCriteria copy() {
        return new GeoCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getAbbreviation() {
        return abbreviation;
    }

    public Optional<StringFilter> optionalAbbreviation() {
        return Optional.ofNullable(abbreviation);
    }

    public StringFilter abbreviation() {
        if (abbreviation == null) {
            setAbbreviation(new StringFilter());
        }
        return abbreviation;
    }

    public void setAbbreviation(StringFilter abbreviation) {
        this.abbreviation = abbreviation;
    }

    public LongFilter getGeoTypeId() {
        return geoTypeId;
    }

    public Optional<LongFilter> optionalGeoTypeId() {
        return Optional.ofNullable(geoTypeId);
    }

    public LongFilter geoTypeId() {
        if (geoTypeId == null) {
            setGeoTypeId(new LongFilter());
        }
        return geoTypeId;
    }

    public void setGeoTypeId(LongFilter geoTypeId) {
        this.geoTypeId = geoTypeId;
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
        final GeoCriteria that = (GeoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(abbreviation, that.abbreviation) &&
            Objects.equals(geoTypeId, that.geoTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, abbreviation, geoTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GeoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalAbbreviation().map(f -> "abbreviation=" + f + ", ").orElse("") +
            optionalGeoTypeId().map(f -> "geoTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
