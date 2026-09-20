package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OtpAuth.
 */
@Entity
@Table(name = "otp_auth")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OtpAuth implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 10)
    @Column(name = "otp", length = 10)
    private String otp;

    @Column(name = "otp_sent")
    private Boolean otpSent;

    @Column(name = "verification_success")
    private Boolean verificationSuccess;

    @Column(name = "otp_expired")
    private Boolean otpExpired;

    @Column(name = "otp_sent_time")
    private Instant otpSentTime;

    @Column(name = "sent_counter")
    private Integer sentCounter;

    @Column(name = "fail_counter")
    private Integer failCounter;

    @Column(name = "otp_reset_counter")
    private Integer otpResetCounter;

    @Column(name = "max_resend")
    private Integer maxResend;

    @Column(name = "max_reset")
    private Integer maxReset;

    @Column(name = "max_failures")
    private Integer maxFailures;

    @Column(name = "otp_active_time")
    private Integer otpActiveTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party party;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OtpAuth id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOtp() {
        return this.otp;
    }

    public OtpAuth otp(String otp) {
        this.setOtp(otp);
        return this;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Boolean getOtpSent() {
        return this.otpSent;
    }

    public OtpAuth otpSent(Boolean otpSent) {
        this.setOtpSent(otpSent);
        return this;
    }

    public void setOtpSent(Boolean otpSent) {
        this.otpSent = otpSent;
    }

    public Boolean getVerificationSuccess() {
        return this.verificationSuccess;
    }

    public OtpAuth verificationSuccess(Boolean verificationSuccess) {
        this.setVerificationSuccess(verificationSuccess);
        return this;
    }

    public void setVerificationSuccess(Boolean verificationSuccess) {
        this.verificationSuccess = verificationSuccess;
    }

    public Boolean getOtpExpired() {
        return this.otpExpired;
    }

    public OtpAuth otpExpired(Boolean otpExpired) {
        this.setOtpExpired(otpExpired);
        return this;
    }

    public void setOtpExpired(Boolean otpExpired) {
        this.otpExpired = otpExpired;
    }

    public Instant getOtpSentTime() {
        return this.otpSentTime;
    }

    public OtpAuth otpSentTime(Instant otpSentTime) {
        this.setOtpSentTime(otpSentTime);
        return this;
    }

    public void setOtpSentTime(Instant otpSentTime) {
        this.otpSentTime = otpSentTime;
    }

    public Integer getSentCounter() {
        return this.sentCounter;
    }

    public OtpAuth sentCounter(Integer sentCounter) {
        this.setSentCounter(sentCounter);
        return this;
    }

    public void setSentCounter(Integer sentCounter) {
        this.sentCounter = sentCounter;
    }

    public Integer getFailCounter() {
        return this.failCounter;
    }

    public OtpAuth failCounter(Integer failCounter) {
        this.setFailCounter(failCounter);
        return this;
    }

    public void setFailCounter(Integer failCounter) {
        this.failCounter = failCounter;
    }

    public Integer getOtpResetCounter() {
        return this.otpResetCounter;
    }

    public OtpAuth otpResetCounter(Integer otpResetCounter) {
        this.setOtpResetCounter(otpResetCounter);
        return this;
    }

    public void setOtpResetCounter(Integer otpResetCounter) {
        this.otpResetCounter = otpResetCounter;
    }

    public Integer getMaxResend() {
        return this.maxResend;
    }

    public OtpAuth maxResend(Integer maxResend) {
        this.setMaxResend(maxResend);
        return this;
    }

    public void setMaxResend(Integer maxResend) {
        this.maxResend = maxResend;
    }

    public Integer getMaxReset() {
        return this.maxReset;
    }

    public OtpAuth maxReset(Integer maxReset) {
        this.setMaxReset(maxReset);
        return this;
    }

    public void setMaxReset(Integer maxReset) {
        this.maxReset = maxReset;
    }

    public Integer getMaxFailures() {
        return this.maxFailures;
    }

    public OtpAuth maxFailures(Integer maxFailures) {
        this.setMaxFailures(maxFailures);
        return this;
    }

    public void setMaxFailures(Integer maxFailures) {
        this.maxFailures = maxFailures;
    }

    public Integer getOtpActiveTime() {
        return this.otpActiveTime;
    }

    public OtpAuth otpActiveTime(Integer otpActiveTime) {
        this.setOtpActiveTime(otpActiveTime);
        return this;
    }

    public void setOtpActiveTime(Integer otpActiveTime) {
        this.otpActiveTime = otpActiveTime;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public OtpAuth party(Party party) {
        this.setParty(party);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OtpAuth)) {
            return false;
        }
        return getId() != null && getId().equals(((OtpAuth) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OtpAuth{" +
            "id=" + getId() +
            ", otp='" + getOtp() + "'" +
            ", otpSent='" + getOtpSent() + "'" +
            ", verificationSuccess='" + getVerificationSuccess() + "'" +
            ", otpExpired='" + getOtpExpired() + "'" +
            ", otpSentTime='" + getOtpSentTime() + "'" +
            ", sentCounter=" + getSentCounter() +
            ", failCounter=" + getFailCounter() +
            ", otpResetCounter=" + getOtpResetCounter() +
            ", maxResend=" + getMaxResend() +
            ", maxReset=" + getMaxReset() +
            ", maxFailures=" + getMaxFailures() +
            ", otpActiveTime=" + getOtpActiveTime() +
            "}";
    }
}
