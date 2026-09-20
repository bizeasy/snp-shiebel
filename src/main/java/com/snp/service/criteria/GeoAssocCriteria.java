package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.GeoAssoc} entity. This class is used
 * in {@link com.snp.web.rest.GeoAssocResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /geo-assocs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GeoAssocCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter geoId;

    private LongFilter geoToId;

    private LongFilter geoAssocTypeId;

    private Boolean distinct;

    public GeoAssocCriteria() {}

    public GeoAssocCriteria(GeoAssocCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.geoId = other.optionalGeoId().map(LongFilter::copy).orElse(null);
        this.geoToId = other.optionalGeoToId().map(LongFilter::copy).orElse(null);
        this.geoAssocTypeId = other.optionalGeoAssocTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public GeoAssocCriteria copy() {
        return new GeoAssocCriteria(this);
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

    public LongFilter getGeoId() {
        return geoId;
    }

    public Optional<LongFilter> optionalGeoId() {
        return Optional.ofNullable(geoId);
    }

    public LongFilter geoId() {
        if (geoId == null) {
            setGeoId(new LongFilter());
        }
        return geoId;
    }

    public void setGeoId(LongFilter geoId) {
        this.geoId = geoId;
    }

    public LongFilter getGeoToId() {
        return geoToId;
    }

    public Optional<LongFilter> optionalGeoToId() {
        return Optional.ofNullable(geoToId);
    }

    public LongFilter geoToId() {
        if (geoToId == null) {
            setGeoToId(new LongFilter());
        }
        return geoToId;
    }

    public void setGeoToId(LongFilter geoToId) {
        this.geoToId = geoToId;
    }

    public LongFilter getGeoAssocTypeId() {
        return geoAssocTypeId;
    }

    public Optional<LongFilter> optionalGeoAssocTypeId() {
        return Optional.ofNullable(geoAssocTypeId);
    }

    public LongFilter geoAssocTypeId() {
        if (geoAssocTypeId == null) {
            setGeoAssocTypeId(new LongFilter());
        }
        return geoAssocTypeId;
    }

    public void setGeoAssocTypeId(LongFilter geoAssocTypeId) {
        this.geoAssocTypeId = geoAssocTypeId;
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
        final GeoAssocCriteria that = (GeoAssocCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(geoId, that.geoId) &&
            Objects.equals(geoToId, that.geoToId) &&
            Objects.equals(geoAssocTypeId, that.geoAssocTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, geoId, geoToId, geoAssocTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GeoAssocCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalGeoId().map(f -> "geoId=" + f + ", ").orElse("") +
            optionalGeoToId().map(f -> "geoToId=" + f + ", ").orElse("") +
            optionalGeoAssocTypeId().map(f -> "geoAssocTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
