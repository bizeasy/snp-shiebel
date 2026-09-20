package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.StatusValidChange} entity. This class is used
 * in {@link com.snp.web.rest.StatusValidChangeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /status-valid-changes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StatusValidChangeCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter transitionName;

    private LongFilter statusId;

    private LongFilter statusToId;

    private Boolean distinct;

    public StatusValidChangeCriteria() {}

    public StatusValidChangeCriteria(StatusValidChangeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.transitionName = other.optionalTransitionName().map(StringFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.statusToId = other.optionalStatusToId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public StatusValidChangeCriteria copy() {
        return new StatusValidChangeCriteria(this);
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

    public StringFilter getTransitionName() {
        return transitionName;
    }

    public Optional<StringFilter> optionalTransitionName() {
        return Optional.ofNullable(transitionName);
    }

    public StringFilter transitionName() {
        if (transitionName == null) {
            setTransitionName(new StringFilter());
        }
        return transitionName;
    }

    public void setTransitionName(StringFilter transitionName) {
        this.transitionName = transitionName;
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

    public LongFilter getStatusToId() {
        return statusToId;
    }

    public Optional<LongFilter> optionalStatusToId() {
        return Optional.ofNullable(statusToId);
    }

    public LongFilter statusToId() {
        if (statusToId == null) {
            setStatusToId(new LongFilter());
        }
        return statusToId;
    }

    public void setStatusToId(LongFilter statusToId) {
        this.statusToId = statusToId;
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
        final StatusValidChangeCriteria that = (StatusValidChangeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(transitionName, that.transitionName) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(statusToId, that.statusToId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transitionName, statusId, statusToId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatusValidChangeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTransitionName().map(f -> "transitionName=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalStatusToId().map(f -> "statusToId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
