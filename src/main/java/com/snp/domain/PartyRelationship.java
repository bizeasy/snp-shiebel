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
 * A PartyRelationship.
 */
@Entity
@Table(name = "party_relationship")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartyRelationship implements Serializable {

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

    @Size(max = 100)
    @Column(name = "relationship_name", length = 100)
    private String relationshipName;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party partyFrom;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party partyTo;

    @ManyToOne(fetch = FetchType.LAZY)
    private RoleType roleTypeFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    private RoleType roleTypeTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "roleTypeFrom", "roleTypeTo" }, allowSetters = true)
    private PartyRelationshipType relationshipType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PartyRelationship id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFromDate() {
        return this.fromDate;
    }

    public PartyRelationship fromDate(Instant fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getThruDate() {
        return this.thruDate;
    }

    public PartyRelationship thruDate(Instant thruDate) {
        this.setThruDate(thruDate);
        return this;
    }

    public void setThruDate(Instant thruDate) {
        this.thruDate = thruDate;
    }

    public String getRelationshipName() {
        return this.relationshipName;
    }

    public PartyRelationship relationshipName(String relationshipName) {
        this.setRelationshipName(relationshipName);
        return this;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public Party getPartyFrom() {
        return this.partyFrom;
    }

    public void setPartyFrom(Party party) {
        this.partyFrom = party;
    }

    public PartyRelationship partyFrom(Party party) {
        this.setPartyFrom(party);
        return this;
    }

    public Party getPartyTo() {
        return this.partyTo;
    }

    public void setPartyTo(Party party) {
        this.partyTo = party;
    }

    public PartyRelationship partyTo(Party party) {
        this.setPartyTo(party);
        return this;
    }

    public RoleType getRoleTypeFrom() {
        return this.roleTypeFrom;
    }

    public void setRoleTypeFrom(RoleType roleType) {
        this.roleTypeFrom = roleType;
    }

    public PartyRelationship roleTypeFrom(RoleType roleType) {
        this.setRoleTypeFrom(roleType);
        return this;
    }

    public RoleType getRoleTypeTo() {
        return this.roleTypeTo;
    }

    public void setRoleTypeTo(RoleType roleType) {
        this.roleTypeTo = roleType;
    }

    public PartyRelationship roleTypeTo(RoleType roleType) {
        this.setRoleTypeTo(roleType);
        return this;
    }

    public PartyRelationshipType getRelationshipType() {
        return this.relationshipType;
    }

    public void setRelationshipType(PartyRelationshipType partyRelationshipType) {
        this.relationshipType = partyRelationshipType;
    }

    public PartyRelationship relationshipType(PartyRelationshipType partyRelationshipType) {
        this.setRelationshipType(partyRelationshipType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartyRelationship)) {
            return false;
        }
        return getId() != null && getId().equals(((PartyRelationship) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyRelationship{" +
            "id=" + getId() +
            ", fromDate='" + getFromDate() + "'" +
            ", thruDate='" + getThruDate() + "'" +
            ", relationshipName='" + getRelationshipName() + "'" +
            "}";
    }
}
