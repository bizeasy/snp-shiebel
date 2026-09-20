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
 * A OrderStatus.
 */
@Entity
@Table(name = "order_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "status_date_time", nullable = false)
    private Instant statusDateTime;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

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
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStatusDateTime() {
        return this.statusDateTime;
    }

    public OrderStatus statusDateTime(Instant statusDateTime) {
        this.setStatusDateTime(statusDateTime);
        return this;
    }

    public void setStatusDateTime(Instant statusDateTime) {
        this.statusDateTime = statusDateTime;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public OrderStatus sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderStatus order(Order order) {
        this.setOrder(order);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OrderStatus status(Status status) {
        this.setStatus(status);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderStatus)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderStatus) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderStatus{" +
            "id=" + getId() +
            ", statusDateTime='" + getStatusDateTime() + "'" +
            ", sequenceNo=" + getSequenceNo() +
            "}";
    }
}
