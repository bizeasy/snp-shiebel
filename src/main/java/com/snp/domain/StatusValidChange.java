package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StatusValidChange.
 */
@Entity
@Table(name = "status_valid_change")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StatusValidChange implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 60)
    @Column(name = "transition_name", length = 60)
    private String transitionName;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status statusTo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StatusValidChange id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransitionName() {
        return this.transitionName;
    }

    public StatusValidChange transitionName(String transitionName) {
        this.setTransitionName(transitionName);
        return this;
    }

    public void setTransitionName(String transitionName) {
        this.transitionName = transitionName;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public StatusValidChange status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Status getStatusTo() {
        return this.statusTo;
    }

    public void setStatusTo(Status status) {
        this.statusTo = status;
    }

    public StatusValidChange statusTo(Status status) {
        this.setStatusTo(status);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatusValidChange)) {
            return false;
        }
        return getId() != null && getId().equals(((StatusValidChange) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatusValidChange{" +
            "id=" + getId() +
            ", transitionName='" + getTransitionName() + "'" +
            "}";
    }
}
