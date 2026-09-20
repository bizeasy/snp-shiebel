package com.snp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PartyRelationshipType.
 */
@Entity
@Table(name = "party_relationship_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartyRelationshipType implements Serializable {

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

    @Size(max = 60)
    @Column(name = "description", length = 60)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private RoleType roleTypeFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    private RoleType roleTypeTo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PartyRelationshipType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PartyRelationshipType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public PartyRelationshipType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoleType getRoleTypeFrom() {
        return this.roleTypeFrom;
    }

    public void setRoleTypeFrom(RoleType roleType) {
        this.roleTypeFrom = roleType;
    }

    public PartyRelationshipType roleTypeFrom(RoleType roleType) {
        this.setRoleTypeFrom(roleType);
        return this;
    }

    public RoleType getRoleTypeTo() {
        return this.roleTypeTo;
    }

    public void setRoleTypeTo(RoleType roleType) {
        this.roleTypeTo = roleType;
    }

    public PartyRelationshipType roleTypeTo(RoleType roleType) {
        this.setRoleTypeTo(roleType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartyRelationshipType)) {
            return false;
        }
        return getId() != null && getId().equals(((PartyRelationshipType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyRelationshipType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
