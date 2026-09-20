package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CustRequestAttachment.
 */
@Entity
@Table(name = "cust_request_attachment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustRequestAttachment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "custRequestType", "custRequestCategory", "status", "fromParty", "productStore", "salesChannel", "currencyUom" },
        allowSetters = true
    )
    private CustRequest custRequest;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "contentType", "folder", "owner" }, allowSetters = true)
    private Attachment attachment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustRequestAttachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CustRequestAttachment name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustRequest getCustRequest() {
        return this.custRequest;
    }

    public void setCustRequest(CustRequest custRequest) {
        this.custRequest = custRequest;
    }

    public CustRequestAttachment custRequest(CustRequest custRequest) {
        this.setCustRequest(custRequest);
        return this;
    }

    public Attachment getAttachment() {
        return this.attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public CustRequestAttachment attachment(Attachment attachment) {
        this.setAttachment(attachment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustRequestAttachment)) {
            return false;
        }
        return getId() != null && getId().equals(((CustRequestAttachment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustRequestAttachment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
