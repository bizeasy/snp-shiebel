package com.snp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContactMech.
 */
@Entity
@Table(name = "contact_mech")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactMech implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "info_string", length = 255)
    private String infoString;

    @ManyToOne(fetch = FetchType.LAZY)
    private ContactMechType contactMechType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContactMech id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfoString() {
        return this.infoString;
    }

    public ContactMech infoString(String infoString) {
        this.setInfoString(infoString);
        return this;
    }

    public void setInfoString(String infoString) {
        this.infoString = infoString;
    }

    public ContactMechType getContactMechType() {
        return this.contactMechType;
    }

    public void setContactMechType(ContactMechType contactMechType) {
        this.contactMechType = contactMechType;
    }

    public ContactMech contactMechType(ContactMechType contactMechType) {
        this.setContactMechType(contactMechType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactMech)) {
            return false;
        }
        return getId() != null && getId().equals(((ContactMech) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactMech{" +
            "id=" + getId() +
            ", infoString='" + getInfoString() + "'" +
            "}";
    }
}
