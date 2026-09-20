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
 * A ReturnItem.
 */
@Entity
@Table(name = "return_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReturnItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @Column(name = "return_quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal returnQuantity;

    @Column(name = "received_quantity", precision = 21, scale = 2)
    private BigDecimal receivedQuantity;

    @Column(name = "return_price", precision = 21, scale = 2)
    private BigDecimal returnPrice;

    @Size(max = 60)
    @Column(name = "return_item_purpose", length = 60)
    private String returnItemPurpose;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "returnType",
            "status",
            "fromParty",
            "toParty",
            "paymentMethod",
            "originContactMech",
            "destinationFacility",
            "currencyUom",
        },
        allowSetters = true
    )
    private ReturnHeader returnHeader;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReturnHeaderType returnType;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReturnItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public ReturnItem sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getDescription() {
        return this.description;
    }

    public ReturnItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getReturnQuantity() {
        return this.returnQuantity;
    }

    public ReturnItem returnQuantity(BigDecimal returnQuantity) {
        this.setReturnQuantity(returnQuantity);
        return this;
    }

    public void setReturnQuantity(BigDecimal returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public BigDecimal getReceivedQuantity() {
        return this.receivedQuantity;
    }

    public ReturnItem receivedQuantity(BigDecimal receivedQuantity) {
        this.setReceivedQuantity(receivedQuantity);
        return this;
    }

    public void setReceivedQuantity(BigDecimal receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public BigDecimal getReturnPrice() {
        return this.returnPrice;
    }

    public ReturnItem returnPrice(BigDecimal returnPrice) {
        this.setReturnPrice(returnPrice);
        return this;
    }

    public void setReturnPrice(BigDecimal returnPrice) {
        this.returnPrice = returnPrice;
    }

    public String getReturnItemPurpose() {
        return this.returnItemPurpose;
    }

    public ReturnItem returnItemPurpose(String returnItemPurpose) {
        this.setReturnItemPurpose(returnItemPurpose);
        return this;
    }

    public void setReturnItemPurpose(String returnItemPurpose) {
        this.returnItemPurpose = returnItemPurpose;
    }

    public ReturnHeader getReturnHeader() {
        return this.returnHeader;
    }

    public void setReturnHeader(ReturnHeader returnHeader) {
        this.returnHeader = returnHeader;
    }

    public ReturnItem returnHeader(ReturnHeader returnHeader) {
        this.setReturnHeader(returnHeader);
        return this;
    }

    public ReturnHeaderType getReturnType() {
        return this.returnType;
    }

    public void setReturnType(ReturnHeaderType returnHeaderType) {
        this.returnType = returnHeaderType;
    }

    public ReturnItem returnType(ReturnHeaderType returnHeaderType) {
        this.setReturnType(returnHeaderType);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ReturnItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public ReturnItem orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ReturnItem status(Status status) {
        this.setStatus(status);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReturnItem)) {
            return false;
        }
        return getId() != null && getId().equals(((ReturnItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReturnItem{" +
            "id=" + getId() +
            ", sequenceNo=" + getSequenceNo() +
            ", description='" + getDescription() + "'" +
            ", returnQuantity=" + getReturnQuantity() +
            ", receivedQuantity=" + getReceivedQuantity() +
            ", returnPrice=" + getReturnPrice() +
            ", returnItemPurpose='" + getReturnItemPurpose() + "'" +
            "}";
    }
}
