package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PartyIdentification.
 */
@Entity
@Table(name = "party_identification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartyIdentification implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "id_value", length = 60, nullable = false)
    private String idValue;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party party;

    @ManyToOne(optional = false)
    @NotNull
    private PartyIdentificationType type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PartyIdentification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdValue() {
        return this.idValue;
    }

    public PartyIdentification idValue(String idValue) {
        this.setIdValue(idValue);
        return this;
    }

    public void setIdValue(String idValue) {
        this.idValue = idValue;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public PartyIdentification party(Party party) {
        this.setParty(party);
        return this;
    }

    public PartyIdentificationType getType() {
        return this.type;
    }

    public void setType(PartyIdentificationType partyIdentificationType) {
        this.type = partyIdentificationType;
    }

    public PartyIdentification type(PartyIdentificationType partyIdentificationType) {
        this.setType(partyIdentificationType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartyIdentification)) {
            return false;
        }
        return getId() != null && getId().equals(((PartyIdentification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyIdentification{" +
            "id=" + getId() +
            ", idValue='" + getIdValue() + "'" +
            "}";
    }
}
