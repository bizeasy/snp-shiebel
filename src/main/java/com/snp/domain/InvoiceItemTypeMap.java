package com.snp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InvoiceItemTypeMap.
 */
@Entity
@Table(name = "invoice_item_type_map")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceItemTypeMap implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "invoice_item_map_key", length = 100, nullable = false, unique = true)
    private String invoiceItemMapKey;

    @ManyToOne(optional = false)
    @NotNull
    private InvoiceType invoiceType;

    @ManyToOne(optional = false)
    @NotNull
    private InvoiceItemType invoiceItemType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InvoiceItemTypeMap id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceItemMapKey() {
        return this.invoiceItemMapKey;
    }

    public InvoiceItemTypeMap invoiceItemMapKey(String invoiceItemMapKey) {
        this.setInvoiceItemMapKey(invoiceItemMapKey);
        return this;
    }

    public void setInvoiceItemMapKey(String invoiceItemMapKey) {
        this.invoiceItemMapKey = invoiceItemMapKey;
    }

    public InvoiceType getInvoiceType() {
        return this.invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public InvoiceItemTypeMap invoiceType(InvoiceType invoiceType) {
        this.setInvoiceType(invoiceType);
        return this;
    }

    public InvoiceItemType getInvoiceItemType() {
        return this.invoiceItemType;
    }

    public void setInvoiceItemType(InvoiceItemType invoiceItemType) {
        this.invoiceItemType = invoiceItemType;
    }

    public InvoiceItemTypeMap invoiceItemType(InvoiceItemType invoiceItemType) {
        this.setInvoiceItemType(invoiceItemType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceItemTypeMap)) {
            return false;
        }
        return getId() != null && getId().equals(((InvoiceItemTypeMap) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceItemTypeMap{" +
            "id=" + getId() +
            ", invoiceItemMapKey='" + getInvoiceItemMapKey() + "'" +
            "}";
    }
}
