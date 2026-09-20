package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PostalAddress.
 */
@Entity
@Table(name = "postal_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostalAddress implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 100)
    @Column(name = "to_name", length = 100)
    private String toName;

    @Size(max = 200)
    @Column(name = "address_1", length = 200)
    private String address1;

    @Size(max = 200)
    @Column(name = "address_2", length = 200)
    private String address2;

    @Size(max = 60)
    @Column(name = "city", length = 60)
    private String city;

    @Size(max = 60)
    @Column(name = "landmark", length = 60)
    private String landmark;

    @Size(max = 10)
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Size(max = 25)
    @Column(name = "custom_address_type", length = 25)
    private String customAddressType;

    @Size(max = 40)
    @Column(name = "state_str", length = 40)
    private String stateStr;

    @Size(max = 40)
    @Column(name = "country_str", length = 40)
    private String countryStr;

    @Column(name = "is_indian_address")
    private Boolean isIndianAddress;

    @Size(max = 255)
    @Column(name = "note", length = 255)
    private String note;

    @Size(max = 255)
    @Column(name = "directions", length = 255)
    private String directions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contactMechType" }, allowSetters = true)
    private ContactMech contactMech;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "geoType" }, allowSetters = true)
    private Geo state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "geoType" }, allowSetters = true)
    private Geo pincode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "geoType" }, allowSetters = true)
    private Geo country;

    @ManyToOne(fetch = FetchType.LAZY)
    private GeoPoint geoPoint;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PostalAddress id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToName() {
        return this.toName;
    }

    public PostalAddress toName(String toName) {
        this.setToName(toName);
        return this;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getAddress1() {
        return this.address1;
    }

    public PostalAddress address1(String address1) {
        this.setAddress1(address1);
        return this;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public PostalAddress address2(String address2) {
        this.setAddress2(address2);
        return this;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return this.city;
    }

    public PostalAddress city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return this.landmark;
    }

    public PostalAddress landmark(String landmark) {
        this.setLandmark(landmark);
        return this;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public PostalAddress postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public PostalAddress isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getCustomAddressType() {
        return this.customAddressType;
    }

    public PostalAddress customAddressType(String customAddressType) {
        this.setCustomAddressType(customAddressType);
        return this;
    }

    public void setCustomAddressType(String customAddressType) {
        this.customAddressType = customAddressType;
    }

    public String getStateStr() {
        return this.stateStr;
    }

    public PostalAddress stateStr(String stateStr) {
        this.setStateStr(stateStr);
        return this;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public String getCountryStr() {
        return this.countryStr;
    }

    public PostalAddress countryStr(String countryStr) {
        this.setCountryStr(countryStr);
        return this;
    }

    public void setCountryStr(String countryStr) {
        this.countryStr = countryStr;
    }

    public Boolean getIsIndianAddress() {
        return this.isIndianAddress;
    }

    public PostalAddress isIndianAddress(Boolean isIndianAddress) {
        this.setIsIndianAddress(isIndianAddress);
        return this;
    }

    public void setIsIndianAddress(Boolean isIndianAddress) {
        this.isIndianAddress = isIndianAddress;
    }

    public String getNote() {
        return this.note;
    }

    public PostalAddress note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDirections() {
        return this.directions;
    }

    public PostalAddress directions(String directions) {
        this.setDirections(directions);
        return this;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public ContactMech getContactMech() {
        return this.contactMech;
    }

    public void setContactMech(ContactMech contactMech) {
        this.contactMech = contactMech;
    }

    public PostalAddress contactMech(ContactMech contactMech) {
        this.setContactMech(contactMech);
        return this;
    }

    public Geo getState() {
        return this.state;
    }

    public void setState(Geo geo) {
        this.state = geo;
    }

    public PostalAddress state(Geo geo) {
        this.setState(geo);
        return this;
    }

    public Geo getPincode() {
        return this.pincode;
    }

    public void setPincode(Geo geo) {
        this.pincode = geo;
    }

    public PostalAddress pincode(Geo geo) {
        this.setPincode(geo);
        return this;
    }

    public Geo getCountry() {
        return this.country;
    }

    public void setCountry(Geo geo) {
        this.country = geo;
    }

    public PostalAddress country(Geo geo) {
        this.setCountry(geo);
        return this;
    }

    public GeoPoint getGeoPoint() {
        return this.geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public PostalAddress geoPoint(GeoPoint geoPoint) {
        this.setGeoPoint(geoPoint);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostalAddress)) {
            return false;
        }
        return getId() != null && getId().equals(((PostalAddress) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostalAddress{" +
            "id=" + getId() +
            ", toName='" + getToName() + "'" +
            ", address1='" + getAddress1() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", city='" + getCity() + "'" +
            ", landmark='" + getLandmark() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", customAddressType='" + getCustomAddressType() + "'" +
            ", stateStr='" + getStateStr() + "'" +
            ", countryStr='" + getCountryStr() + "'" +
            ", isIndianAddress='" + getIsIndianAddress() + "'" +
            ", note='" + getNote() + "'" +
            ", directions='" + getDirections() + "'" +
            "}";
    }
}
