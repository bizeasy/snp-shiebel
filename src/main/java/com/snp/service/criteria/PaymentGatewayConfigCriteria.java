package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.PaymentGatewayConfig} entity. This class is used
 * in {@link com.snp.web.rest.PaymentGatewayConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-gateway-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentGatewayConfigCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter authUrl;

    private StringFilter releaseUrl;

    private StringFilter refundUrl;

    private Boolean distinct;

    public PaymentGatewayConfigCriteria() {}

    public PaymentGatewayConfigCriteria(PaymentGatewayConfigCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.authUrl = other.optionalAuthUrl().map(StringFilter::copy).orElse(null);
        this.releaseUrl = other.optionalReleaseUrl().map(StringFilter::copy).orElse(null);
        this.refundUrl = other.optionalRefundUrl().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaymentGatewayConfigCriteria copy() {
        return new PaymentGatewayConfigCriteria(this);
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

    public StringFilter getAuthUrl() {
        return authUrl;
    }

    public Optional<StringFilter> optionalAuthUrl() {
        return Optional.ofNullable(authUrl);
    }

    public StringFilter authUrl() {
        if (authUrl == null) {
            setAuthUrl(new StringFilter());
        }
        return authUrl;
    }

    public void setAuthUrl(StringFilter authUrl) {
        this.authUrl = authUrl;
    }

    public StringFilter getReleaseUrl() {
        return releaseUrl;
    }

    public Optional<StringFilter> optionalReleaseUrl() {
        return Optional.ofNullable(releaseUrl);
    }

    public StringFilter releaseUrl() {
        if (releaseUrl == null) {
            setReleaseUrl(new StringFilter());
        }
        return releaseUrl;
    }

    public void setReleaseUrl(StringFilter releaseUrl) {
        this.releaseUrl = releaseUrl;
    }

    public StringFilter getRefundUrl() {
        return refundUrl;
    }

    public Optional<StringFilter> optionalRefundUrl() {
        return Optional.ofNullable(refundUrl);
    }

    public StringFilter refundUrl() {
        if (refundUrl == null) {
            setRefundUrl(new StringFilter());
        }
        return refundUrl;
    }

    public void setRefundUrl(StringFilter refundUrl) {
        this.refundUrl = refundUrl;
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
        final PaymentGatewayConfigCriteria that = (PaymentGatewayConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(authUrl, that.authUrl) &&
            Objects.equals(releaseUrl, that.releaseUrl) &&
            Objects.equals(refundUrl, that.refundUrl) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authUrl, releaseUrl, refundUrl, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentGatewayConfigCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalAuthUrl().map(f -> "authUrl=" + f + ", ").orElse("") +
            optionalReleaseUrl().map(f -> "releaseUrl=" + f + ", ").orElse("") +
            optionalRefundUrl().map(f -> "refundUrl=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
