package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CustRequestItem.
 */
@Entity
@Table(name = "cust_request_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustRequestItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @Min(value = 1)
    @Max(value = 20)
    @Column(name = "priority")
    private Integer priority;

    @Column(name = "required_by_date")
    private Instant requiredByDate;

    @NotNull
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @Column(name = "selected_amount", precision = 21, scale = 2)
    private BigDecimal selectedAmount;

    @Column(name = "max_amount", precision = 21, scale = 2)
    private BigDecimal maxAmount;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Size(max = 2000)
    @Column(name = "story", length = 2000)
    private String story;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "custRequestType", "custRequestCategory", "status", "fromParty", "productStore", "salesChannel", "currencyUom" },
        allowSetters = true
    )
    private CustRequest custRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "productType",
            "primaryProductCategory",
            "virtualProduct",
            "inventoryItemType",
            "taxSlab",
            "uom",
            "quantityUom",
            "weightUom",
            "sizeUom",
        },
        allowSetters = true
    )
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom uom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "shoppingList", "product", "status" }, allowSetters = true)
    private ShoppingListItem shoppingListItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustRequestItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public CustRequestItem sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public CustRequestItem priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Instant getRequiredByDate() {
        return this.requiredByDate;
    }

    public CustRequestItem requiredByDate(Instant requiredByDate) {
        this.setRequiredByDate(requiredByDate);
        return this;
    }

    public void setRequiredByDate(Instant requiredByDate) {
        this.requiredByDate = requiredByDate;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public CustRequestItem quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSelectedAmount() {
        return this.selectedAmount;
    }

    public CustRequestItem selectedAmount(BigDecimal selectedAmount) {
        this.setSelectedAmount(selectedAmount);
        return this;
    }

    public void setSelectedAmount(BigDecimal selectedAmount) {
        this.selectedAmount = selectedAmount;
    }

    public BigDecimal getMaxAmount() {
        return this.maxAmount;
    }

    public CustRequestItem maxAmount(BigDecimal maxAmount) {
        this.setMaxAmount(maxAmount);
        return this;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getDescription() {
        return this.description;
    }

    public CustRequestItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStory() {
        return this.story;
    }

    public CustRequestItem story(String story) {
        this.setStory(story);
        return this;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public CustRequest getCustRequest() {
        return this.custRequest;
    }

    public void setCustRequest(CustRequest custRequest) {
        this.custRequest = custRequest;
    }

    public CustRequestItem custRequest(CustRequest custRequest) {
        this.setCustRequest(custRequest);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CustRequestItem status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CustRequestItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Uom getUom() {
        return this.uom;
    }

    public void setUom(Uom uom) {
        this.uom = uom;
    }

    public CustRequestItem uom(Uom uom) {
        this.setUom(uom);
        return this;
    }

    public ShoppingListItem getShoppingListItem() {
        return this.shoppingListItem;
    }

    public void setShoppingListItem(ShoppingListItem shoppingListItem) {
        this.shoppingListItem = shoppingListItem;
    }

    public CustRequestItem shoppingListItem(ShoppingListItem shoppingListItem) {
        this.setShoppingListItem(shoppingListItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustRequestItem)) {
            return false;
        }
        return getId() != null && getId().equals(((CustRequestItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustRequestItem{" +
            "id=" + getId() +
            ", sequenceNo=" + getSequenceNo() +
            ", priority=" + getPriority() +
            ", requiredByDate='" + getRequiredByDate() + "'" +
            ", quantity=" + getQuantity() +
            ", selectedAmount=" + getSelectedAmount() +
            ", maxAmount=" + getMaxAmount() +
            ", description='" + getDescription() + "'" +
            ", story='" + getStory() + "'" +
            "}";
    }
}
