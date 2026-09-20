package com.snp.service.criteria;

import com.snp.domain.enumeration.SharePermission;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.FileShare} entity. This class is used
 * in {@link com.snp.web.rest.FileShareResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /file-shares?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileShareCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SharePermission
     */
    public static class SharePermissionFilter extends Filter<SharePermission> {

        public SharePermissionFilter() {}

        public SharePermissionFilter(SharePermissionFilter filter) {
            super(filter);
        }

        @Override
        public SharePermissionFilter copy() {
            return new SharePermissionFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private SharePermissionFilter permission;

    private StringFilter sharedWithEmail;

    private InstantFilter sharedDate;

    private InstantFilter expiryDate;

    private StringFilter accessToken;

    private BooleanFilter isRevoked;

    private LongFilter folderId;

    private LongFilter attachmentId;

    private LongFilter sharedWithId;

    private LongFilter sharedById;

    private Boolean distinct;

    public FileShareCriteria() {}

    public FileShareCriteria(FileShareCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.permission = other.optionalPermission().map(SharePermissionFilter::copy).orElse(null);
        this.sharedWithEmail = other.optionalSharedWithEmail().map(StringFilter::copy).orElse(null);
        this.sharedDate = other.optionalSharedDate().map(InstantFilter::copy).orElse(null);
        this.expiryDate = other.optionalExpiryDate().map(InstantFilter::copy).orElse(null);
        this.accessToken = other.optionalAccessToken().map(StringFilter::copy).orElse(null);
        this.isRevoked = other.optionalIsRevoked().map(BooleanFilter::copy).orElse(null);
        this.folderId = other.optionalFolderId().map(LongFilter::copy).orElse(null);
        this.attachmentId = other.optionalAttachmentId().map(LongFilter::copy).orElse(null);
        this.sharedWithId = other.optionalSharedWithId().map(LongFilter::copy).orElse(null);
        this.sharedById = other.optionalSharedById().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FileShareCriteria copy() {
        return new FileShareCriteria(this);
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

    public SharePermissionFilter getPermission() {
        return permission;
    }

    public Optional<SharePermissionFilter> optionalPermission() {
        return Optional.ofNullable(permission);
    }

    public SharePermissionFilter permission() {
        if (permission == null) {
            setPermission(new SharePermissionFilter());
        }
        return permission;
    }

    public void setPermission(SharePermissionFilter permission) {
        this.permission = permission;
    }

    public StringFilter getSharedWithEmail() {
        return sharedWithEmail;
    }

    public Optional<StringFilter> optionalSharedWithEmail() {
        return Optional.ofNullable(sharedWithEmail);
    }

    public StringFilter sharedWithEmail() {
        if (sharedWithEmail == null) {
            setSharedWithEmail(new StringFilter());
        }
        return sharedWithEmail;
    }

    public void setSharedWithEmail(StringFilter sharedWithEmail) {
        this.sharedWithEmail = sharedWithEmail;
    }

    public InstantFilter getSharedDate() {
        return sharedDate;
    }

    public Optional<InstantFilter> optionalSharedDate() {
        return Optional.ofNullable(sharedDate);
    }

    public InstantFilter sharedDate() {
        if (sharedDate == null) {
            setSharedDate(new InstantFilter());
        }
        return sharedDate;
    }

    public void setSharedDate(InstantFilter sharedDate) {
        this.sharedDate = sharedDate;
    }

    public InstantFilter getExpiryDate() {
        return expiryDate;
    }

    public Optional<InstantFilter> optionalExpiryDate() {
        return Optional.ofNullable(expiryDate);
    }

    public InstantFilter expiryDate() {
        if (expiryDate == null) {
            setExpiryDate(new InstantFilter());
        }
        return expiryDate;
    }

    public void setExpiryDate(InstantFilter expiryDate) {
        this.expiryDate = expiryDate;
    }

    public StringFilter getAccessToken() {
        return accessToken;
    }

    public Optional<StringFilter> optionalAccessToken() {
        return Optional.ofNullable(accessToken);
    }

    public StringFilter accessToken() {
        if (accessToken == null) {
            setAccessToken(new StringFilter());
        }
        return accessToken;
    }

    public void setAccessToken(StringFilter accessToken) {
        this.accessToken = accessToken;
    }

    public BooleanFilter getIsRevoked() {
        return isRevoked;
    }

    public Optional<BooleanFilter> optionalIsRevoked() {
        return Optional.ofNullable(isRevoked);
    }

    public BooleanFilter isRevoked() {
        if (isRevoked == null) {
            setIsRevoked(new BooleanFilter());
        }
        return isRevoked;
    }

    public void setIsRevoked(BooleanFilter isRevoked) {
        this.isRevoked = isRevoked;
    }

    public LongFilter getFolderId() {
        return folderId;
    }

    public Optional<LongFilter> optionalFolderId() {
        return Optional.ofNullable(folderId);
    }

    public LongFilter folderId() {
        if (folderId == null) {
            setFolderId(new LongFilter());
        }
        return folderId;
    }

    public void setFolderId(LongFilter folderId) {
        this.folderId = folderId;
    }

    public LongFilter getAttachmentId() {
        return attachmentId;
    }

    public Optional<LongFilter> optionalAttachmentId() {
        return Optional.ofNullable(attachmentId);
    }

    public LongFilter attachmentId() {
        if (attachmentId == null) {
            setAttachmentId(new LongFilter());
        }
        return attachmentId;
    }

    public void setAttachmentId(LongFilter attachmentId) {
        this.attachmentId = attachmentId;
    }

    public LongFilter getSharedWithId() {
        return sharedWithId;
    }

    public Optional<LongFilter> optionalSharedWithId() {
        return Optional.ofNullable(sharedWithId);
    }

    public LongFilter sharedWithId() {
        if (sharedWithId == null) {
            setSharedWithId(new LongFilter());
        }
        return sharedWithId;
    }

    public void setSharedWithId(LongFilter sharedWithId) {
        this.sharedWithId = sharedWithId;
    }

    public LongFilter getSharedById() {
        return sharedById;
    }

    public Optional<LongFilter> optionalSharedById() {
        return Optional.ofNullable(sharedById);
    }

    public LongFilter sharedById() {
        if (sharedById == null) {
            setSharedById(new LongFilter());
        }
        return sharedById;
    }

    public void setSharedById(LongFilter sharedById) {
        this.sharedById = sharedById;
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
        final FileShareCriteria that = (FileShareCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(permission, that.permission) &&
            Objects.equals(sharedWithEmail, that.sharedWithEmail) &&
            Objects.equals(sharedDate, that.sharedDate) &&
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(accessToken, that.accessToken) &&
            Objects.equals(isRevoked, that.isRevoked) &&
            Objects.equals(folderId, that.folderId) &&
            Objects.equals(attachmentId, that.attachmentId) &&
            Objects.equals(sharedWithId, that.sharedWithId) &&
            Objects.equals(sharedById, that.sharedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            permission,
            sharedWithEmail,
            sharedDate,
            expiryDate,
            accessToken,
            isRevoked,
            folderId,
            attachmentId,
            sharedWithId,
            sharedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileShareCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPermission().map(f -> "permission=" + f + ", ").orElse("") +
            optionalSharedWithEmail().map(f -> "sharedWithEmail=" + f + ", ").orElse("") +
            optionalSharedDate().map(f -> "sharedDate=" + f + ", ").orElse("") +
            optionalExpiryDate().map(f -> "expiryDate=" + f + ", ").orElse("") +
            optionalAccessToken().map(f -> "accessToken=" + f + ", ").orElse("") +
            optionalIsRevoked().map(f -> "isRevoked=" + f + ", ").orElse("") +
            optionalFolderId().map(f -> "folderId=" + f + ", ").orElse("") +
            optionalAttachmentId().map(f -> "attachmentId=" + f + ", ").orElse("") +
            optionalSharedWithId().map(f -> "sharedWithId=" + f + ", ").orElse("") +
            optionalSharedById().map(f -> "sharedById=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
