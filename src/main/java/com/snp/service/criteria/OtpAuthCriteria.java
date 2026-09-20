package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.OtpAuth} entity. This class is used
 * in {@link com.snp.web.rest.OtpAuthResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /otp-auths?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OtpAuthCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter otp;

    private BooleanFilter otpSent;

    private BooleanFilter verificationSuccess;

    private BooleanFilter otpExpired;

    private InstantFilter otpSentTime;

    private IntegerFilter sentCounter;

    private IntegerFilter failCounter;

    private IntegerFilter otpResetCounter;

    private IntegerFilter maxResend;

    private IntegerFilter maxReset;

    private IntegerFilter maxFailures;

    private IntegerFilter otpActiveTime;

    private LongFilter partyId;

    private Boolean distinct;

    public OtpAuthCriteria() {}

    public OtpAuthCriteria(OtpAuthCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.otp = other.optionalOtp().map(StringFilter::copy).orElse(null);
        this.otpSent = other.optionalOtpSent().map(BooleanFilter::copy).orElse(null);
        this.verificationSuccess = other.optionalVerificationSuccess().map(BooleanFilter::copy).orElse(null);
        this.otpExpired = other.optionalOtpExpired().map(BooleanFilter::copy).orElse(null);
        this.otpSentTime = other.optionalOtpSentTime().map(InstantFilter::copy).orElse(null);
        this.sentCounter = other.optionalSentCounter().map(IntegerFilter::copy).orElse(null);
        this.failCounter = other.optionalFailCounter().map(IntegerFilter::copy).orElse(null);
        this.otpResetCounter = other.optionalOtpResetCounter().map(IntegerFilter::copy).orElse(null);
        this.maxResend = other.optionalMaxResend().map(IntegerFilter::copy).orElse(null);
        this.maxReset = other.optionalMaxReset().map(IntegerFilter::copy).orElse(null);
        this.maxFailures = other.optionalMaxFailures().map(IntegerFilter::copy).orElse(null);
        this.otpActiveTime = other.optionalOtpActiveTime().map(IntegerFilter::copy).orElse(null);
        this.partyId = other.optionalPartyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OtpAuthCriteria copy() {
        return new OtpAuthCriteria(this);
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

    public StringFilter getOtp() {
        return otp;
    }

    public Optional<StringFilter> optionalOtp() {
        return Optional.ofNullable(otp);
    }

    public StringFilter otp() {
        if (otp == null) {
            setOtp(new StringFilter());
        }
        return otp;
    }

    public void setOtp(StringFilter otp) {
        this.otp = otp;
    }

    public BooleanFilter getOtpSent() {
        return otpSent;
    }

    public Optional<BooleanFilter> optionalOtpSent() {
        return Optional.ofNullable(otpSent);
    }

    public BooleanFilter otpSent() {
        if (otpSent == null) {
            setOtpSent(new BooleanFilter());
        }
        return otpSent;
    }

    public void setOtpSent(BooleanFilter otpSent) {
        this.otpSent = otpSent;
    }

    public BooleanFilter getVerificationSuccess() {
        return verificationSuccess;
    }

    public Optional<BooleanFilter> optionalVerificationSuccess() {
        return Optional.ofNullable(verificationSuccess);
    }

    public BooleanFilter verificationSuccess() {
        if (verificationSuccess == null) {
            setVerificationSuccess(new BooleanFilter());
        }
        return verificationSuccess;
    }

    public void setVerificationSuccess(BooleanFilter verificationSuccess) {
        this.verificationSuccess = verificationSuccess;
    }

    public BooleanFilter getOtpExpired() {
        return otpExpired;
    }

    public Optional<BooleanFilter> optionalOtpExpired() {
        return Optional.ofNullable(otpExpired);
    }

    public BooleanFilter otpExpired() {
        if (otpExpired == null) {
            setOtpExpired(new BooleanFilter());
        }
        return otpExpired;
    }

    public void setOtpExpired(BooleanFilter otpExpired) {
        this.otpExpired = otpExpired;
    }

    public InstantFilter getOtpSentTime() {
        return otpSentTime;
    }

    public Optional<InstantFilter> optionalOtpSentTime() {
        return Optional.ofNullable(otpSentTime);
    }

    public InstantFilter otpSentTime() {
        if (otpSentTime == null) {
            setOtpSentTime(new InstantFilter());
        }
        return otpSentTime;
    }

    public void setOtpSentTime(InstantFilter otpSentTime) {
        this.otpSentTime = otpSentTime;
    }

    public IntegerFilter getSentCounter() {
        return sentCounter;
    }

    public Optional<IntegerFilter> optionalSentCounter() {
        return Optional.ofNullable(sentCounter);
    }

    public IntegerFilter sentCounter() {
        if (sentCounter == null) {
            setSentCounter(new IntegerFilter());
        }
        return sentCounter;
    }

    public void setSentCounter(IntegerFilter sentCounter) {
        this.sentCounter = sentCounter;
    }

    public IntegerFilter getFailCounter() {
        return failCounter;
    }

    public Optional<IntegerFilter> optionalFailCounter() {
        return Optional.ofNullable(failCounter);
    }

    public IntegerFilter failCounter() {
        if (failCounter == null) {
            setFailCounter(new IntegerFilter());
        }
        return failCounter;
    }

    public void setFailCounter(IntegerFilter failCounter) {
        this.failCounter = failCounter;
    }

    public IntegerFilter getOtpResetCounter() {
        return otpResetCounter;
    }

    public Optional<IntegerFilter> optionalOtpResetCounter() {
        return Optional.ofNullable(otpResetCounter);
    }

    public IntegerFilter otpResetCounter() {
        if (otpResetCounter == null) {
            setOtpResetCounter(new IntegerFilter());
        }
        return otpResetCounter;
    }

    public void setOtpResetCounter(IntegerFilter otpResetCounter) {
        this.otpResetCounter = otpResetCounter;
    }

    public IntegerFilter getMaxResend() {
        return maxResend;
    }

    public Optional<IntegerFilter> optionalMaxResend() {
        return Optional.ofNullable(maxResend);
    }

    public IntegerFilter maxResend() {
        if (maxResend == null) {
            setMaxResend(new IntegerFilter());
        }
        return maxResend;
    }

    public void setMaxResend(IntegerFilter maxResend) {
        this.maxResend = maxResend;
    }

    public IntegerFilter getMaxReset() {
        return maxReset;
    }

    public Optional<IntegerFilter> optionalMaxReset() {
        return Optional.ofNullable(maxReset);
    }

    public IntegerFilter maxReset() {
        if (maxReset == null) {
            setMaxReset(new IntegerFilter());
        }
        return maxReset;
    }

    public void setMaxReset(IntegerFilter maxReset) {
        this.maxReset = maxReset;
    }

    public IntegerFilter getMaxFailures() {
        return maxFailures;
    }

    public Optional<IntegerFilter> optionalMaxFailures() {
        return Optional.ofNullable(maxFailures);
    }

    public IntegerFilter maxFailures() {
        if (maxFailures == null) {
            setMaxFailures(new IntegerFilter());
        }
        return maxFailures;
    }

    public void setMaxFailures(IntegerFilter maxFailures) {
        this.maxFailures = maxFailures;
    }

    public IntegerFilter getOtpActiveTime() {
        return otpActiveTime;
    }

    public Optional<IntegerFilter> optionalOtpActiveTime() {
        return Optional.ofNullable(otpActiveTime);
    }

    public IntegerFilter otpActiveTime() {
        if (otpActiveTime == null) {
            setOtpActiveTime(new IntegerFilter());
        }
        return otpActiveTime;
    }

    public void setOtpActiveTime(IntegerFilter otpActiveTime) {
        this.otpActiveTime = otpActiveTime;
    }

    public LongFilter getPartyId() {
        return partyId;
    }

    public Optional<LongFilter> optionalPartyId() {
        return Optional.ofNullable(partyId);
    }

    public LongFilter partyId() {
        if (partyId == null) {
            setPartyId(new LongFilter());
        }
        return partyId;
    }

    public void setPartyId(LongFilter partyId) {
        this.partyId = partyId;
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
        final OtpAuthCriteria that = (OtpAuthCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(otp, that.otp) &&
            Objects.equals(otpSent, that.otpSent) &&
            Objects.equals(verificationSuccess, that.verificationSuccess) &&
            Objects.equals(otpExpired, that.otpExpired) &&
            Objects.equals(otpSentTime, that.otpSentTime) &&
            Objects.equals(sentCounter, that.sentCounter) &&
            Objects.equals(failCounter, that.failCounter) &&
            Objects.equals(otpResetCounter, that.otpResetCounter) &&
            Objects.equals(maxResend, that.maxResend) &&
            Objects.equals(maxReset, that.maxReset) &&
            Objects.equals(maxFailures, that.maxFailures) &&
            Objects.equals(otpActiveTime, that.otpActiveTime) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            otp,
            otpSent,
            verificationSuccess,
            otpExpired,
            otpSentTime,
            sentCounter,
            failCounter,
            otpResetCounter,
            maxResend,
            maxReset,
            maxFailures,
            otpActiveTime,
            partyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OtpAuthCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalOtp().map(f -> "otp=" + f + ", ").orElse("") +
            optionalOtpSent().map(f -> "otpSent=" + f + ", ").orElse("") +
            optionalVerificationSuccess().map(f -> "verificationSuccess=" + f + ", ").orElse("") +
            optionalOtpExpired().map(f -> "otpExpired=" + f + ", ").orElse("") +
            optionalOtpSentTime().map(f -> "otpSentTime=" + f + ", ").orElse("") +
            optionalSentCounter().map(f -> "sentCounter=" + f + ", ").orElse("") +
            optionalFailCounter().map(f -> "failCounter=" + f + ", ").orElse("") +
            optionalOtpResetCounter().map(f -> "otpResetCounter=" + f + ", ").orElse("") +
            optionalMaxResend().map(f -> "maxResend=" + f + ", ").orElse("") +
            optionalMaxReset().map(f -> "maxReset=" + f + ", ").orElse("") +
            optionalMaxFailures().map(f -> "maxFailures=" + f + ", ").orElse("") +
            optionalOtpActiveTime().map(f -> "otpActiveTime=" + f + ", ").orElse("") +
            optionalPartyId().map(f -> "partyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
