package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InventoryItemVariance.
 */
@Entity
@Table(name = "inventory_item_variance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryItemVariance implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "variance_reason", length = 255)
    private String varianceReason;

    @Column(name = "atp_var", precision = 21, scale = 2)
    private BigDecimal atpVar;

    @Column(name = "qoh_var", precision = 21, scale = 2)
    private BigDecimal qohVar;

    @Size(max = 255)
    @Column(name = "comments", length = 255)
    private String comments;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "inventoryItemType",
            "product",
            "supplier",
            "ownerParty",
            "status",
            "facility",
            "facilityLocation",
            "uom",
            "currencyUom",
            "lot",
        },
        allowSetters = true
    )
    private InventoryItem inventoryItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InventoryItemVariance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVarianceReason() {
        return this.varianceReason;
    }

    public InventoryItemVariance varianceReason(String varianceReason) {
        this.setVarianceReason(varianceReason);
        return this;
    }

    public void setVarianceReason(String varianceReason) {
        this.varianceReason = varianceReason;
    }

    public BigDecimal getAtpVar() {
        return this.atpVar;
    }

    public InventoryItemVariance atpVar(BigDecimal atpVar) {
        this.setAtpVar(atpVar);
        return this;
    }

    public void setAtpVar(BigDecimal atpVar) {
        this.atpVar = atpVar;
    }

    public BigDecimal getQohVar() {
        return this.qohVar;
    }

    public InventoryItemVariance qohVar(BigDecimal qohVar) {
        this.setQohVar(qohVar);
        return this;
    }

    public void setQohVar(BigDecimal qohVar) {
        this.qohVar = qohVar;
    }

    public String getComments() {
        return this.comments;
    }

    public InventoryItemVariance comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public InventoryItem getInventoryItem() {
        return this.inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public InventoryItemVariance inventoryItem(InventoryItem inventoryItem) {
        this.setInventoryItem(inventoryItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryItemVariance)) {
            return false;
        }
        return getId() != null && getId().equals(((InventoryItemVariance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryItemVariance{" +
            "id=" + getId() +
            ", varianceReason='" + getVarianceReason() + "'" +
            ", atpVar=" + getAtpVar() +
            ", qohVar=" + getQohVar() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
