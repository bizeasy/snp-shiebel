package com.snp.service.criteria;

import com.snp.domain.enumeration.Gender;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.Party} entity. This class is used
 * in {@link com.snp.web.rest.PartyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartyCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter isOrganisation;

    private StringFilter organisationName;

    private StringFilter organisationShortName;

    private StringFilter salutation;

    private StringFilter firstName;

    private StringFilter middleName;

    private StringFilter lastName;

    private GenderFilter gender;

    private LocalDateFilter birthDate;

    private StringFilter primaryPhone;

    private StringFilter primaryEmail;

    private BooleanFilter isTemporaryPassword;

    private StringFilter logoImageUrl;

    private StringFilter profileImageUrl;

    private LongFilter userId;

    private LongFilter partyTypeId;

    private LongFilter statusId;

    private LongFilter addressId;

    private Boolean distinct;

    public PartyCriteria() {}

    public PartyCriteria(PartyCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.isOrganisation = other.optionalIsOrganisation().map(BooleanFilter::copy).orElse(null);
        this.organisationName = other.optionalOrganisationName().map(StringFilter::copy).orElse(null);
        this.organisationShortName = other.optionalOrganisationShortName().map(StringFilter::copy).orElse(null);
        this.salutation = other.optionalSalutation().map(StringFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.middleName = other.optionalMiddleName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(GenderFilter::copy).orElse(null);
        this.birthDate = other.optionalBirthDate().map(LocalDateFilter::copy).orElse(null);
        this.primaryPhone = other.optionalPrimaryPhone().map(StringFilter::copy).orElse(null);
        this.primaryEmail = other.optionalPrimaryEmail().map(StringFilter::copy).orElse(null);
        this.isTemporaryPassword = other.optionalIsTemporaryPassword().map(BooleanFilter::copy).orElse(null);
        this.logoImageUrl = other.optionalLogoImageUrl().map(StringFilter::copy).orElse(null);
        this.profileImageUrl = other.optionalProfileImageUrl().map(StringFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.partyTypeId = other.optionalPartyTypeId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.addressId = other.optionalAddressId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PartyCriteria copy() {
        return new PartyCriteria(this);
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

    public BooleanFilter getIsOrganisation() {
        return isOrganisation;
    }

    public Optional<BooleanFilter> optionalIsOrganisation() {
        return Optional.ofNullable(isOrganisation);
    }

    public BooleanFilter isOrganisation() {
        if (isOrganisation == null) {
            setIsOrganisation(new BooleanFilter());
        }
        return isOrganisation;
    }

    public void setIsOrganisation(BooleanFilter isOrganisation) {
        this.isOrganisation = isOrganisation;
    }

    public StringFilter getOrganisationName() {
        return organisationName;
    }

    public Optional<StringFilter> optionalOrganisationName() {
        return Optional.ofNullable(organisationName);
    }

    public StringFilter organisationName() {
        if (organisationName == null) {
            setOrganisationName(new StringFilter());
        }
        return organisationName;
    }

    public void setOrganisationName(StringFilter organisationName) {
        this.organisationName = organisationName;
    }

    public StringFilter getOrganisationShortName() {
        return organisationShortName;
    }

    public Optional<StringFilter> optionalOrganisationShortName() {
        return Optional.ofNullable(organisationShortName);
    }

    public StringFilter organisationShortName() {
        if (organisationShortName == null) {
            setOrganisationShortName(new StringFilter());
        }
        return organisationShortName;
    }

    public void setOrganisationShortName(StringFilter organisationShortName) {
        this.organisationShortName = organisationShortName;
    }

    public StringFilter getSalutation() {
        return salutation;
    }

    public Optional<StringFilter> optionalSalutation() {
        return Optional.ofNullable(salutation);
    }

    public StringFilter salutation() {
        if (salutation == null) {
            setSalutation(new StringFilter());
        }
        return salutation;
    }

    public void setSalutation(StringFilter salutation) {
        this.salutation = salutation;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public Optional<StringFilter> optionalFirstName() {
        return Optional.ofNullable(firstName);
    }

    public StringFilter firstName() {
        if (firstName == null) {
            setFirstName(new StringFilter());
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getMiddleName() {
        return middleName;
    }

    public Optional<StringFilter> optionalMiddleName() {
        return Optional.ofNullable(middleName);
    }

    public StringFilter middleName() {
        if (middleName == null) {
            setMiddleName(new StringFilter());
        }
        return middleName;
    }

    public void setMiddleName(StringFilter middleName) {
        this.middleName = middleName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public Optional<StringFilter> optionalLastName() {
        return Optional.ofNullable(lastName);
    }

    public StringFilter lastName() {
        if (lastName == null) {
            setLastName(new StringFilter());
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public Optional<GenderFilter> optionalGender() {
        return Optional.ofNullable(gender);
    }

    public GenderFilter gender() {
        if (gender == null) {
            setGender(new GenderFilter());
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public Optional<LocalDateFilter> optionalBirthDate() {
        return Optional.ofNullable(birthDate);
    }

    public LocalDateFilter birthDate() {
        if (birthDate == null) {
            setBirthDate(new LocalDateFilter());
        }
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
    }

    public StringFilter getPrimaryPhone() {
        return primaryPhone;
    }

    public Optional<StringFilter> optionalPrimaryPhone() {
        return Optional.ofNullable(primaryPhone);
    }

    public StringFilter primaryPhone() {
        if (primaryPhone == null) {
            setPrimaryPhone(new StringFilter());
        }
        return primaryPhone;
    }

    public void setPrimaryPhone(StringFilter primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public StringFilter getPrimaryEmail() {
        return primaryEmail;
    }

    public Optional<StringFilter> optionalPrimaryEmail() {
        return Optional.ofNullable(primaryEmail);
    }

    public StringFilter primaryEmail() {
        if (primaryEmail == null) {
            setPrimaryEmail(new StringFilter());
        }
        return primaryEmail;
    }

    public void setPrimaryEmail(StringFilter primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public BooleanFilter getIsTemporaryPassword() {
        return isTemporaryPassword;
    }

    public Optional<BooleanFilter> optionalIsTemporaryPassword() {
        return Optional.ofNullable(isTemporaryPassword);
    }

    public BooleanFilter isTemporaryPassword() {
        if (isTemporaryPassword == null) {
            setIsTemporaryPassword(new BooleanFilter());
        }
        return isTemporaryPassword;
    }

    public void setIsTemporaryPassword(BooleanFilter isTemporaryPassword) {
        this.isTemporaryPassword = isTemporaryPassword;
    }

    public StringFilter getLogoImageUrl() {
        return logoImageUrl;
    }

    public Optional<StringFilter> optionalLogoImageUrl() {
        return Optional.ofNullable(logoImageUrl);
    }

    public StringFilter logoImageUrl() {
        if (logoImageUrl == null) {
            setLogoImageUrl(new StringFilter());
        }
        return logoImageUrl;
    }

    public void setLogoImageUrl(StringFilter logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
    }

    public StringFilter getProfileImageUrl() {
        return profileImageUrl;
    }

    public Optional<StringFilter> optionalProfileImageUrl() {
        return Optional.ofNullable(profileImageUrl);
    }

    public StringFilter profileImageUrl() {
        if (profileImageUrl == null) {
            setProfileImageUrl(new StringFilter());
        }
        return profileImageUrl;
    }

    public void setProfileImageUrl(StringFilter profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getPartyTypeId() {
        return partyTypeId;
    }

    public Optional<LongFilter> optionalPartyTypeId() {
        return Optional.ofNullable(partyTypeId);
    }

    public LongFilter partyTypeId() {
        if (partyTypeId == null) {
            setPartyTypeId(new LongFilter());
        }
        return partyTypeId;
    }

    public void setPartyTypeId(LongFilter partyTypeId) {
        this.partyTypeId = partyTypeId;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public Optional<LongFilter> optionalStatusId() {
        return Optional.ofNullable(statusId);
    }

    public LongFilter statusId() {
        if (statusId == null) {
            setStatusId(new LongFilter());
        }
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public Optional<LongFilter> optionalAddressId() {
        return Optional.ofNullable(addressId);
    }

    public LongFilter addressId() {
        if (addressId == null) {
            setAddressId(new LongFilter());
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
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
        final PartyCriteria that = (PartyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(isOrganisation, that.isOrganisation) &&
            Objects.equals(organisationName, that.organisationName) &&
            Objects.equals(organisationShortName, that.organisationShortName) &&
            Objects.equals(salutation, that.salutation) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(middleName, that.middleName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(primaryPhone, that.primaryPhone) &&
            Objects.equals(primaryEmail, that.primaryEmail) &&
            Objects.equals(isTemporaryPassword, that.isTemporaryPassword) &&
            Objects.equals(logoImageUrl, that.logoImageUrl) &&
            Objects.equals(profileImageUrl, that.profileImageUrl) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(partyTypeId, that.partyTypeId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            isOrganisation,
            organisationName,
            organisationShortName,
            salutation,
            firstName,
            middleName,
            lastName,
            gender,
            birthDate,
            primaryPhone,
            primaryEmail,
            isTemporaryPassword,
            logoImageUrl,
            profileImageUrl,
            userId,
            partyTypeId,
            statusId,
            addressId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIsOrganisation().map(f -> "isOrganisation=" + f + ", ").orElse("") +
            optionalOrganisationName().map(f -> "organisationName=" + f + ", ").orElse("") +
            optionalOrganisationShortName().map(f -> "organisationShortName=" + f + ", ").orElse("") +
            optionalSalutation().map(f -> "salutation=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalMiddleName().map(f -> "middleName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalBirthDate().map(f -> "birthDate=" + f + ", ").orElse("") +
            optionalPrimaryPhone().map(f -> "primaryPhone=" + f + ", ").orElse("") +
            optionalPrimaryEmail().map(f -> "primaryEmail=" + f + ", ").orElse("") +
            optionalIsTemporaryPassword().map(f -> "isTemporaryPassword=" + f + ", ").orElse("") +
            optionalLogoImageUrl().map(f -> "logoImageUrl=" + f + ", ").orElse("") +
            optionalProfileImageUrl().map(f -> "profileImageUrl=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalPartyTypeId().map(f -> "partyTypeId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalAddressId().map(f -> "addressId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
