package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuoteRole.
 */
@Entity
@Table(name = "quote_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuoteRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "quoteType", "custRequest", "party", "status", "productStore", "salesChannel", "currencyUom", "currencyConversion" },
        allowSetters = true
    )
    private Quote quote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "quote", "product", "productFeature", "uom", "status", "custRequest", "custRequestItem" },
        allowSetters = true
    )
    private QuoteItem quoteItem;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    private RoleType roleType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuoteRole id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Quote getQuote() {
        return this.quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public QuoteRole quote(Quote quote) {
        this.setQuote(quote);
        return this;
    }

    public QuoteItem getQuoteItem() {
        return this.quoteItem;
    }

    public void setQuoteItem(QuoteItem quoteItem) {
        this.quoteItem = quoteItem;
    }

    public QuoteRole quoteItem(QuoteItem quoteItem) {
        this.setQuoteItem(quoteItem);
        return this;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public QuoteRole party(Party party) {
        this.setParty(party);
        return this;
    }

    public RoleType getRoleType() {
        return this.roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public QuoteRole roleType(RoleType roleType) {
        this.setRoleType(roleType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuoteRole)) {
            return false;
        }
        return getId() != null && getId().equals(((QuoteRole) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuoteRole{" +
            "id=" + getId() +
            "}";
    }
}
