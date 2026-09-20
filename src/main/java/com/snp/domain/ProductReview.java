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
 * A ProductReview.
 */
@Entity
@Table(name = "product_review")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductReview implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "rating")
    private Integer rating;

    @Lob
    @Column(name = "rating_info")
    private String ratingInfo;

    @Size(max = 2000)
    @Column(name = "message", length = 2000)
    private String message;

    @Column(name = "review_date")
    private Instant reviewDate;

    @ManyToOne(optional = false)
    @NotNull
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
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "order",
            "orderItemType",
            "product",
            "uom",
            "supplierProduct",
            "supplier",
            "productFeature",
            "productFacility",
            "fromInventoryItem",
            "shoppingListItem",
            "quoteItem",
            "status",
            "taxAuthRate",
        },
        allowSetters = true
    )
    private OrderItem orderItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductReview id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return this.rating;
    }

    public ProductReview rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getRatingInfo() {
        return this.ratingInfo;
    }

    public ProductReview ratingInfo(String ratingInfo) {
        this.setRatingInfo(ratingInfo);
        return this;
    }

    public void setRatingInfo(String ratingInfo) {
        this.ratingInfo = ratingInfo;
    }

    public String getMessage() {
        return this.message;
    }

    public ProductReview message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getReviewDate() {
        return this.reviewDate;
    }

    public ProductReview reviewDate(Instant reviewDate) {
        this.setReviewDate(reviewDate);
        return this;
    }

    public void setReviewDate(Instant reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductReview product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public ProductReview party(Party party) {
        this.setParty(party);
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public ProductReview orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductReview)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductReview) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductReview{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", ratingInfo='" + getRatingInfo() + "'" +
            ", message='" + getMessage() + "'" +
            ", reviewDate='" + getReviewDate() + "'" +
            "}";
    }
}
