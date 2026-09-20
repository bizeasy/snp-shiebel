package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.PostalAddress} entity. This class is used
 * in {@link com.snp.web.rest.PostalAddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /postal-addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostalAddressCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter toName;

    private StringFilter address1;

    private StringFilter address2;

    private StringFilter city;

    private StringFilter landmark;

    private StringFilter postalCode;

    private BooleanFilter isDefault;

    private StringFilter customAddressType;

    private StringFilter stateStr;

    private StringFilter countryStr;

    private BooleanFilter isIndianAddress;

    private StringFilter note;

    private StringFilter directions;

    private LongFilter contactMechId;

    private LongFilter stateId;

    private LongFilter pincodeId;

    private LongFilter countryId;

    private LongFilter geoPointId;

    private Boolean distinct;

    public PostalAddressCriteria() {}

    public PostalAddressCriteria(PostalAddressCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.toName = other.optionalToName().map(StringFilter::copy).orElse(null);
        this.address1 = other.optionalAddress1().map(StringFilter::copy).orElse(null);
        this.address2 = other.optionalAddress2().map(StringFilter::copy).orElse(null);
        this.city = other.optionalCity().map(StringFilter::copy).orElse(null);
        this.landmark = other.optionalLandmark().map(StringFilter::copy).orElse(null);
        this.postalCode = other.optionalPostalCode().map(StringFilter::copy).orElse(null);
        this.isDefault = other.optionalIsDefault().map(BooleanFilter::copy).orElse(null);
        this.customAddressType = other.optionalCustomAddressType().map(StringFilter::copy).orElse(null);
        this.stateStr = other.optionalStateStr().map(StringFilter::copy).orElse(null);
        this.countryStr = other.optionalCountryStr().map(StringFilter::copy).orElse(null);
        this.isIndianAddress = other.optionalIsIndianAddress().map(BooleanFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.directions = other.optionalDirections().map(StringFilter::copy).orElse(null);
        this.contactMechId = other.optionalContactMechId().map(LongFilter::copy).orElse(null);
        this.stateId = other.optionalStateId().map(LongFilter::copy).orElse(null);
        this.pincodeId = other.optionalPincodeId().map(LongFilter::copy).orElse(null);
        this.countryId = other.optionalCountryId().map(LongFilter::copy).orElse(null);
        this.geoPointId = other.optionalGeoPointId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PostalAddressCriteria copy() {
        return new PostalAddressCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getToName() {
        return toName;
    }

    public Optional<StringFilter> optionalToName() {
        return Optional.ofNullable(toName);
    }

    public StringFilter toName() {
        if (toName == null) {
            setToName(new StringFilter());
        }
        return toName;
    }

    public void setToName(StringFilter toName) {
        this.toName = toName;
    }

    public StringFilter getAddress1() {
        return address1;
    }

    public Optional<StringFilter> optionalAddress1() {
        return Optional.ofNullable(address1);
    }

    public StringFilter address1() {
        if (address1 == null) {
            setAddress1(new StringFilter());
        }
        return address1;
    }

    public void setAddress1(StringFilter address1) {
        this.address1 = address1;
    }

    public StringFilter getAddress2() {
        return address2;
    }

    public Optional<StringFilter> optionalAddress2() {
        return Optional.ofNullable(address2);
    }

    public StringFilter address2() {
        if (address2 == null) {
            setAddress2(new StringFilter());
        }
        return address2;
    }

    public void setAddress2(StringFilter address2) {
        this.address2 = address2;
    }

    public StringFilter getCity() {
        return city;
    }

    public Optional<StringFilter> optionalCity() {
        return Optional.ofNullable(city);
    }

    public StringFilter city() {
        if (city == null) {
            setCity(new StringFilter());
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getLandmark() {
        return landmark;
    }

    public Optional<StringFilter> optionalLandmark() {
        return Optional.ofNullable(landmark);
    }

    public StringFilter landmark() {
        if (landmark == null) {
            setLandmark(new StringFilter());
        }
        return landmark;
    }

    public void setLandmark(StringFilter landmark) {
        this.landmark = landmark;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public Optional<StringFilter> optionalPostalCode() {
        return Optional.ofNullable(postalCode);
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            setPostalCode(new StringFilter());
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public BooleanFilter getIsDefault() {
        return isDefault;
    }

    public Optional<BooleanFilter> optionalIsDefault() {
        return Optional.ofNullable(isDefault);
    }

    public BooleanFilter isDefault() {
        if (isDefault == null) {
            setIsDefault(new BooleanFilter());
        }
        return isDefault;
    }

    public void setIsDefault(BooleanFilter isDefault) {
        this.isDefault = isDefault;
    }

    public StringFilter getCustomAddressType() {
        return customAddressType;
    }

    public Optional<StringFilter> optionalCustomAddressType() {
        return Optional.ofNullable(customAddressType);
    }

    public StringFilter customAddressType() {
        if (customAddressType == null) {
            setCustomAddressType(new StringFilter());
        }
        return customAddressType;
    }

    public void setCustomAddressType(StringFilter customAddressType) {
        this.customAddressType = customAddressType;
    }

    public StringFilter getStateStr() {
        return stateStr;
    }

    public Optional<StringFilter> optionalStateStr() {
        return Optional.ofNullable(stateStr);
    }

    public StringFilter stateStr() {
        if (stateStr == null) {
            setStateStr(new StringFilter());
        }
        return stateStr;
    }

    public void setStateStr(StringFilter stateStr) {
        this.stateStr = stateStr;
    }

    public StringFilter getCountryStr() {
        return countryStr;
    }

    public Optional<StringFilter> optionalCountryStr() {
        return Optional.ofNullable(countryStr);
    }

    public StringFilter countryStr() {
        if (countryStr == null) {
            setCountryStr(new StringFilter());
        }
        return countryStr;
    }

    public void setCountryStr(StringFilter countryStr) {
        this.countryStr = countryStr;
    }

    public BooleanFilter getIsIndianAddress() {
        return isIndianAddress;
    }

    public Optional<BooleanFilter> optionalIsIndianAddress() {
        return Optional.ofNullable(isIndianAddress);
    }

    public BooleanFilter isIndianAddress() {
        if (isIndianAddress == null) {
            setIsIndianAddress(new BooleanFilter());
        }
        return isIndianAddress;
    }

    public void setIsIndianAddress(BooleanFilter isIndianAddress) {
        this.isIndianAddress = isIndianAddress;
    }

    public StringFilter getNote() {
        return note;
    }

    public Optional<StringFilter> optionalNote() {
        return Optional.ofNullable(note);
    }

    public StringFilter note() {
        if (note == null) {
            setNote(new StringFilter());
        }
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public StringFilter getDirections() {
        return directions;
    }

    public Optional<StringFilter> optionalDirections() {
        return Optional.ofNullable(directions);
    }

    public StringFilter directions() {
        if (directions == null) {
            setDirections(new StringFilter());
        }
        return directions;
    }

    public void setDirections(StringFilter directions) {
        this.directions = directions;
    }

    public LongFilter getContactMechId() {
        return contactMechId;
    }

    public Optional<LongFilter> optionalContactMechId() {
        return Optional.ofNullable(contactMechId);
    }

    public LongFilter contactMechId() {
        if (contactMechId == null) {
            setContactMechId(new LongFilter());
        }
        return contactMechId;
    }

    public void setContactMechId(LongFilter contactMechId) {
        this.contactMechId = contactMechId;
    }

    public LongFilter getStateId() {
        return stateId;
    }

    public Optional<LongFilter> optionalStateId() {
        return Optional.ofNullable(stateId);
    }

    public LongFilter stateId() {
        if (stateId == null) {
            setStateId(new LongFilter());
        }
        return stateId;
    }

    public void setStateId(LongFilter stateId) {
        this.stateId = stateId;
    }

    public LongFilter getPincodeId() {
        return pincodeId;
    }

    public Optional<LongFilter> optionalPincodeId() {
        return Optional.ofNullable(pincodeId);
    }

    public LongFilter pincodeId() {
        if (pincodeId == null) {
            setPincodeId(new LongFilter());
        }
        return pincodeId;
    }

    public void setPincodeId(LongFilter pincodeId) {
        this.pincodeId = pincodeId;
    }

    public LongFilter getCountryId() {
        return countryId;
    }

    public Optional<LongFilter> optionalCountryId() {
        return Optional.ofNullable(countryId);
    }

    public LongFilter countryId() {
        if (countryId == null) {
            setCountryId(new LongFilter());
        }
        return countryId;
    }

    public void setCountryId(LongFilter countryId) {
        this.countryId = countryId;
    }

    public LongFilter getGeoPointId() {
        return geoPointId;
    }

    public Optional<LongFilter> optionalGeoPointId() {
        return Optional.ofNullable(geoPointId);
    }

    public LongFilter geoPointId() {
        if (geoPointId == null) {
            setGeoPointId(new LongFilter());
        }
        return geoPointId;
    }

    public void setGeoPointId(LongFilter geoPointId) {
        this.geoPointId = geoPointId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PostalAddressCriteria that = (PostalAddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(toName, that.toName) &&
            Objects.equals(address1, that.address1) &&
            Objects.equals(address2, that.address2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(landmark, that.landmark) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(isDefault, that.isDefault) &&
            Objects.equals(customAddressType, that.customAddressType) &&
            Objects.equals(stateStr, that.stateStr) &&
            Objects.equals(countryStr, that.countryStr) &&
            Objects.equals(isIndianAddress, that.isIndianAddress) &&
            Objects.equals(note, that.note) &&
            Objects.equals(directions, that.directions) &&
            Objects.equals(contactMechId, that.contactMechId) &&
            Objects.equals(stateId, that.stateId) &&
            Objects.equals(pincodeId, that.pincodeId) &&
            Objects.equals(countryId, that.countryId) &&
            Objects.equals(geoPointId, that.geoPointId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            toName,
            address1,
            address2,
            city,
            landmark,
            postalCode,
            isDefault,
            customAddressType,
            stateStr,
            countryStr,
            isIndianAddress,
            note,
            directions,
            contactMechId,
            stateId,
            pincodeId,
            countryId,
            geoPointId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostalAddressCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalToName().map(f -> "toName=" + f + ", ").orElse("") +
            optionalAddress1().map(f -> "address1=" + f + ", ").orElse("") +
            optionalAddress2().map(f -> "address2=" + f + ", ").orElse("") +
            optionalCity().map(f -> "city=" + f + ", ").orElse("") +
            optionalLandmark().map(f -> "landmark=" + f + ", ").orElse("") +
            optionalPostalCode().map(f -> "postalCode=" + f + ", ").orElse("") +
            optionalIsDefault().map(f -> "isDefault=" + f + ", ").orElse("") +
            optionalCustomAddressType().map(f -> "customAddressType=" + f + ", ").orElse("") +
            optionalStateStr().map(f -> "stateStr=" + f + ", ").orElse("") +
            optionalCountryStr().map(f -> "countryStr=" + f + ", ").orElse("") +
            optionalIsIndianAddress().map(f -> "isIndianAddress=" + f + ", ").orElse("") +
            optionalNote().map(f -> "note=" + f + ", ").orElse("") +
            optionalDirections().map(f -> "directions=" + f + ", ").orElse("") +
            optionalContactMechId().map(f -> "contactMechId=" + f + ", ").orElse("") +
            optionalStateId().map(f -> "stateId=" + f + ", ").orElse("") +
            optionalPincodeId().map(f -> "pincodeId=" + f + ", ").orElse("") +
            optionalCountryId().map(f -> "countryId=" + f + ", ").orElse("") +
            optionalGeoPointId().map(f -> "geoPointId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
