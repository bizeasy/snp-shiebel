package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.Attachment} entity. This class is used
 * in {@link com.snp.web.rest.AttachmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttachmentCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter attachmentUrl;

    private StringFilter mimeType;

    private LongFilter fileSize;

    private InstantFilter uploadedDate;

    private BooleanFilter isTrashed;

    private StringFilter checksum;

    private LongFilter contentTypeId;

    private LongFilter folderId;

    private LongFilter ownerId;

    private Boolean distinct;

    public AttachmentCriteria() {}

    public AttachmentCriteria(AttachmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.attachmentUrl = other.optionalAttachmentUrl().map(StringFilter::copy).orElse(null);
        this.mimeType = other.optionalMimeType().map(StringFilter::copy).orElse(null);
        this.fileSize = other.optionalFileSize().map(LongFilter::copy).orElse(null);
        this.uploadedDate = other.optionalUploadedDate().map(InstantFilter::copy).orElse(null);
        this.isTrashed = other.optionalIsTrashed().map(BooleanFilter::copy).orElse(null);
        this.checksum = other.optionalChecksum().map(StringFilter::copy).orElse(null);
        this.contentTypeId = other.optionalContentTypeId().map(LongFilter::copy).orElse(null);
        this.folderId = other.optionalFolderId().map(LongFilter::copy).orElse(null);
        this.ownerId = other.optionalOwnerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AttachmentCriteria copy() {
        return new AttachmentCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getAttachmentUrl() {
        return attachmentUrl;
    }

    public Optional<StringFilter> optionalAttachmentUrl() {
        return Optional.ofNullable(attachmentUrl);
    }

    public StringFilter attachmentUrl() {
        if (attachmentUrl == null) {
            setAttachmentUrl(new StringFilter());
        }
        return attachmentUrl;
    }

    public void setAttachmentUrl(StringFilter attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public StringFilter getMimeType() {
        return mimeType;
    }

    public Optional<StringFilter> optionalMimeType() {
        return Optional.ofNullable(mimeType);
    }

    public StringFilter mimeType() {
        if (mimeType == null) {
            setMimeType(new StringFilter());
        }
        return mimeType;
    }

    public void setMimeType(StringFilter mimeType) {
        this.mimeType = mimeType;
    }

    public LongFilter getFileSize() {
        return fileSize;
    }

    public Optional<LongFilter> optionalFileSize() {
        return Optional.ofNullable(fileSize);
    }

    public LongFilter fileSize() {
        if (fileSize == null) {
            setFileSize(new LongFilter());
        }
        return fileSize;
    }

    public void setFileSize(LongFilter fileSize) {
        this.fileSize = fileSize;
    }

    public InstantFilter getUploadedDate() {
        return uploadedDate;
    }

    public Optional<InstantFilter> optionalUploadedDate() {
        return Optional.ofNullable(uploadedDate);
    }

    public InstantFilter uploadedDate() {
        if (uploadedDate == null) {
            setUploadedDate(new InstantFilter());
        }
        return uploadedDate;
    }

    public void setUploadedDate(InstantFilter uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public BooleanFilter getIsTrashed() {
        return isTrashed;
    }

    public Optional<BooleanFilter> optionalIsTrashed() {
        return Optional.ofNullable(isTrashed);
    }

    public BooleanFilter isTrashed() {
        if (isTrashed == null) {
            setIsTrashed(new BooleanFilter());
        }
        return isTrashed;
    }

    public void setIsTrashed(BooleanFilter isTrashed) {
        this.isTrashed = isTrashed;
    }

    public StringFilter getChecksum() {
        return checksum;
    }

    public Optional<StringFilter> optionalChecksum() {
        return Optional.ofNullable(checksum);
    }

    public StringFilter checksum() {
        if (checksum == null) {
            setChecksum(new StringFilter());
        }
        return checksum;
    }

    public void setChecksum(StringFilter checksum) {
        this.checksum = checksum;
    }

    public LongFilter getContentTypeId() {
        return contentTypeId;
    }

    public Optional<LongFilter> optionalContentTypeId() {
        return Optional.ofNullable(contentTypeId);
    }

    public LongFilter contentTypeId() {
        if (contentTypeId == null) {
            setContentTypeId(new LongFilter());
        }
        return contentTypeId;
    }

    public void setContentTypeId(LongFilter contentTypeId) {
        this.contentTypeId = contentTypeId;
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

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public Optional<LongFilter> optionalOwnerId() {
        return Optional.ofNullable(ownerId);
    }

    public LongFilter ownerId() {
        if (ownerId == null) {
            setOwnerId(new LongFilter());
        }
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
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
        final AttachmentCriteria that = (AttachmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(attachmentUrl, that.attachmentUrl) &&
            Objects.equals(mimeType, that.mimeType) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(uploadedDate, that.uploadedDate) &&
            Objects.equals(isTrashed, that.isTrashed) &&
            Objects.equals(checksum, that.checksum) &&
            Objects.equals(contentTypeId, that.contentTypeId) &&
            Objects.equals(folderId, that.folderId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            attachmentUrl,
            mimeType,
            fileSize,
            uploadedDate,
            isTrashed,
            checksum,
            contentTypeId,
            folderId,
            ownerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalAttachmentUrl().map(f -> "attachmentUrl=" + f + ", ").orElse("") +
            optionalMimeType().map(f -> "mimeType=" + f + ", ").orElse("") +
            optionalFileSize().map(f -> "fileSize=" + f + ", ").orElse("") +
            optionalUploadedDate().map(f -> "uploadedDate=" + f + ", ").orElse("") +
            optionalIsTrashed().map(f -> "isTrashed=" + f + ", ").orElse("") +
            optionalChecksum().map(f -> "checksum=" + f + ", ").orElse("") +
            optionalContentTypeId().map(f -> "contentTypeId=" + f + ", ").orElse("") +
            optionalFolderId().map(f -> "folderId=" + f + ", ").orElse("") +
            optionalOwnerId().map(f -> "ownerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
