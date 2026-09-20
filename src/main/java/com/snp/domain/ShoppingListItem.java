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
 * A ShoppingListItem.
 */
@Entity
@Table(name = "shopping_list_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShoppingListItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @NotNull
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @Column(name = "required_by_date")
    private Instant requiredByDate;

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
    @JsonIgnoreProperties(value = { "shoppingListType", "party", "status" }, allowSetters = true)
    private ShoppingList shoppingList;

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
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShoppingListItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public ShoppingListItem sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public ShoppingListItem quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Instant getRequiredByDate() {
        return this.requiredByDate;
    }

    public ShoppingListItem requiredByDate(Instant requiredByDate) {
        this.setRequiredByDate(requiredByDate);
        return this;
    }

    public void setRequiredByDate(Instant requiredByDate) {
        this.requiredByDate = requiredByDate;
    }

    public BigDecimal getSelectedAmount() {
        return this.selectedAmount;
    }

    public ShoppingListItem selectedAmount(BigDecimal selectedAmount) {
        this.setSelectedAmount(selectedAmount);
        return this;
    }

    public void setSelectedAmount(BigDecimal selectedAmount) {
        this.selectedAmount = selectedAmount;
    }

    public BigDecimal getMaxAmount() {
        return this.maxAmount;
    }

    public ShoppingListItem maxAmount(BigDecimal maxAmount) {
        this.setMaxAmount(maxAmount);
        return this;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getDescription() {
        return this.description;
    }

    public ShoppingListItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStory() {
        return this.story;
    }

    public ShoppingListItem story(String story) {
        this.setStory(story);
        return this;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public ShoppingList getShoppingList() {
        return this.shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public ShoppingListItem shoppingList(ShoppingList shoppingList) {
        this.setShoppingList(shoppingList);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ShoppingListItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ShoppingListItem status(Status status) {
        this.setStatus(status);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShoppingListItem)) {
            return false;
        }
        return getId() != null && getId().equals(((ShoppingListItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShoppingListItem{" +
            "id=" + getId() +
            ", sequenceNo=" + getSequenceNo() +
            ", quantity=" + getQuantity() +
            ", requiredByDate='" + getRequiredByDate() + "'" +
            ", selectedAmount=" + getSelectedAmount() +
            ", maxAmount=" + getMaxAmount() +
            ", description='" + getDescription() + "'" +
            ", story='" + getStory() + "'" +
            "}";
    }
}
