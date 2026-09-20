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
 * A OrderContactMech.
 */
@Entity
@Table(name = "order_contact_mech")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderContactMech implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "from_date", nullable = false)
    private Instant fromDate;

    @Column(name = "thru_date")
    private Instant thruDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "orderType",
            "salesChannel",
            "party",
            "supplier",
            "status",
            "currencyUom",
            "currencyConversion",
            "quote",
            "facility",
            "productStore",
            "department",
            "customTimePeriod",
        },
        allowSetters = true
    )
    private Order order;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "contactMechType" }, allowSetters = true)
    private ContactMech contactMech;

    @ManyToOne(fetch = FetchType.LAZY)
    private ContactMechPurpose contactMechPurpose;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderContactMech id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFromDate() {
        return this.fromDate;
    }

    public OrderContactMech fromDate(Instant fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getThruDate() {
        return this.thruDate;
    }

    public OrderContactMech thruDate(Instant thruDate) {
        this.setThruDate(thruDate);
        return this;
    }

    public void setThruDate(Instant thruDate) {
        this.thruDate = thruDate;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderContactMech order(Order order) {
        this.setOrder(order);
        return this;
    }

    public ContactMech getContactMech() {
        return this.contactMech;
    }

    public void setContactMech(ContactMech contactMech) {
        this.contactMech = contactMech;
    }

    public OrderContactMech contactMech(ContactMech contactMech) {
        this.setContactMech(contactMech);
        return this;
    }

    public ContactMechPurpose getContactMechPurpose() {
        return this.contactMechPurpose;
    }

    public void setContactMechPurpose(ContactMechPurpose contactMechPurpose) {
        this.contactMechPurpose = contactMechPurpose;
    }

    public OrderContactMech contactMechPurpose(ContactMechPurpose contactMechPurpose) {
        this.setContactMechPurpose(contactMechPurpose);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderContactMech)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderContactMech) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderContactMech{" +
            "id=" + getId() +
            ", fromDate='" + getFromDate() + "'" +
            ", thruDate='" + getThruDate() + "'" +
            "}";
    }
}
