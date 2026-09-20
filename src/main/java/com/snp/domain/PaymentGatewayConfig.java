package com.snp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentGatewayConfig.
 */
@Entity
@Table(name = "payment_gateway_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentGatewayConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 25)
    @Column(name = "name", length = 25, nullable = false, unique = true)
    private String name;

    @Size(max = 2000)
    @Column(name = "auth_url", length = 2000)
    private String authUrl;

    @Size(max = 2000)
    @Column(name = "release_url", length = 2000)
    private String releaseUrl;

    @Size(max = 2000)
    @Column(name = "refund_url", length = 2000)
    private String refundUrl;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentGatewayConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PaymentGatewayConfig name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthUrl() {
        return this.authUrl;
    }

    public PaymentGatewayConfig authUrl(String authUrl) {
        this.setAuthUrl(authUrl);
        return this;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getReleaseUrl() {
        return this.releaseUrl;
    }

    public PaymentGatewayConfig releaseUrl(String releaseUrl) {
        this.setReleaseUrl(releaseUrl);
        return this;
    }

    public void setReleaseUrl(String releaseUrl) {
        this.releaseUrl = releaseUrl;
    }

    public String getRefundUrl() {
        return this.refundUrl;
    }

    public PaymentGatewayConfig refundUrl(String refundUrl) {
        this.setRefundUrl(refundUrl);
        return this;
    }

    public void setRefundUrl(String refundUrl) {
        this.refundUrl = refundUrl;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentGatewayConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentGatewayConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentGatewayConfig{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", authUrl='" + getAuthUrl() + "'" +
            ", releaseUrl='" + getReleaseUrl() + "'" +
            ", refundUrl='" + getRefundUrl() + "'" +
            "}";
    }
}
