package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snp.domain.enumeration.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Party.
 */
@Entity
@Table(name = "party")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Party implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "is_organisation")
    private Boolean isOrganisation;

    @Size(max = 200)
    @Column(name = "organisation_name", length = 200)
    private String organisationName;

    @Size(max = 100)
    @Column(name = "organisation_short_name", length = 100)
    private String organisationShortName;

    @Size(max = 20)
    @Column(name = "salutation", length = 20)
    private String salutation;

    @Size(max = 60)
    @Column(name = "first_name", length = 60)
    private String firstName;

    @Size(max = 60)
    @Column(name = "middle_name", length = 60)
    private String middleName;

    @Size(max = 60)
    @Column(name = "last_name", length = 60)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Size(max = 20)
    @Column(name = "primary_phone", length = 20)
    private String primaryPhone;

    @Size(min = 5, max = 75)
    @Column(name = "primary_email", length = 75)
    private String primaryEmail;

    @Column(name = "is_temporary_password")
    private Boolean isTemporaryPassword;

    @Size(max = 2000)
    @Column(name = "logo_image_url", length = 2000)
    private String logoImageUrl;

    @Size(max = 2000)
    @Column(name = "profile_image_url", length = 2000)
    private String profileImageUrl;

    @Lob
    @Column(name = "notes")
    private String notes;

    @Lob
    @Column(name = "postal_address")
    private String postalAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private PartyType partyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contactMech", "state", "pincode", "country", "geoPoint" }, allowSetters = true)
    private PostalAddress address;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Party id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsOrganisation() {
        return this.isOrganisation;
    }

    public Party isOrganisation(Boolean isOrganisation) {
        this.setIsOrganisation(isOrganisation);
        return this;
    }

    public void setIsOrganisation(Boolean isOrganisation) {
        this.isOrganisation = isOrganisation;
    }

    public String getOrganisationName() {
        return this.organisationName;
    }

    public Party organisationName(String organisationName) {
        this.setOrganisationName(organisationName);
        return this;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getOrganisationShortName() {
        return this.organisationShortName;
    }

    public Party organisationShortName(String organisationShortName) {
        this.setOrganisationShortName(organisationShortName);
        return this;
    }

    public void setOrganisationShortName(String organisationShortName) {
        this.organisationShortName = organisationShortName;
    }

    public String getSalutation() {
        return this.salutation;
    }

    public Party salutation(String salutation) {
        this.setSalutation(salutation);
        return this;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Party firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public Party middleName(String middleName) {
        this.setMiddleName(middleName);
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Party lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Party gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Party birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPrimaryPhone() {
        return this.primaryPhone;
    }

    public Party primaryPhone(String primaryPhone) {
        this.setPrimaryPhone(primaryPhone);
        return this;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getPrimaryEmail() {
        return this.primaryEmail;
    }

    public Party primaryEmail(String primaryEmail) {
        this.setPrimaryEmail(primaryEmail);
        return this;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public Boolean getIsTemporaryPassword() {
        return this.isTemporaryPassword;
    }

    public Party isTemporaryPassword(Boolean isTemporaryPassword) {
        this.setIsTemporaryPassword(isTemporaryPassword);
        return this;
    }

    public void setIsTemporaryPassword(Boolean isTemporaryPassword) {
        this.isTemporaryPassword = isTemporaryPassword;
    }

    public String getLogoImageUrl() {
        return this.logoImageUrl;
    }

    public Party logoImageUrl(String logoImageUrl) {
        this.setLogoImageUrl(logoImageUrl);
        return this;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public Party profileImageUrl(String profileImageUrl) {
        this.setProfileImageUrl(profileImageUrl);
        return this;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getNotes() {
        return this.notes;
    }

    public Party notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPostalAddress() {
        return this.postalAddress;
    }

    public Party postalAddress(String postalAddress) {
        this.setPostalAddress(postalAddress);
        return this;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Party user(User user) {
        this.setUser(user);
        return this;
    }

    public PartyType getPartyType() {
        return this.partyType;
    }

    public void setPartyType(PartyType partyType) {
        this.partyType = partyType;
    }

    public Party partyType(PartyType partyType) {
        this.setPartyType(partyType);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Party status(Status status) {
        this.setStatus(status);
        return this;
    }

    public PostalAddress getAddress() {
        return this.address;
    }

    public void setAddress(PostalAddress postalAddress) {
        this.address = postalAddress;
    }

    public Party address(PostalAddress postalAddress) {
        this.setAddress(postalAddress);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Party)) {
            return false;
        }
        return getId() != null && getId().equals(((Party) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Party{" +
            "id=" + getId() +
            ", isOrganisation='" + getIsOrganisation() + "'" +
            ", organisationName='" + getOrganisationName() + "'" +
            ", organisationShortName='" + getOrganisationShortName() + "'" +
            ", salutation='" + getSalutation() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", gender='" + getGender() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", primaryPhone='" + getPrimaryPhone() + "'" +
            ", primaryEmail='" + getPrimaryEmail() + "'" +
            ", isTemporaryPassword='" + getIsTemporaryPassword() + "'" +
            ", logoImageUrl='" + getLogoImageUrl() + "'" +
            ", profileImageUrl='" + getProfileImageUrl() + "'" +
            ", notes='" + getNotes() + "'" +
            ", postalAddress='" + getPostalAddress() + "'" +
            "}";
    }
}
