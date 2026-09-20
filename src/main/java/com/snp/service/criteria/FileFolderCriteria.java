package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.FileFolder} entity. This class is used
 * in {@link com.snp.web.rest.FileFolderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /file-folders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileFolderCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter path;

    private BooleanFilter isSystemFolder;

    private BooleanFilter isTrashed;

    private InstantFilter createdDate;

    private LongFilter parentId;

    private LongFilter ownerId;

    private Boolean distinct;

    public FileFolderCriteria() {}

    public FileFolderCriteria(FileFolderCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.path = other.optionalPath().map(StringFilter::copy).orElse(null);
        this.isSystemFolder = other.optionalIsSystemFolder().map(BooleanFilter::copy).orElse(null);
        this.isTrashed = other.optionalIsTrashed().map(BooleanFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(LongFilter::copy).orElse(null);
        this.ownerId = other.optionalOwnerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FileFolderCriteria copy() {
        return new FileFolderCriteria(this);
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

    public StringFilter getPath() {
        return path;
    }

    public Optional<StringFilter> optionalPath() {
        return Optional.ofNullable(path);
    }

    public StringFilter path() {
        if (path == null) {
            setPath(new StringFilter());
        }
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
    }

    public BooleanFilter getIsSystemFolder() {
        return isSystemFolder;
    }

    public Optional<BooleanFilter> optionalIsSystemFolder() {
        return Optional.ofNullable(isSystemFolder);
    }

    public BooleanFilter isSystemFolder() {
        if (isSystemFolder == null) {
            setIsSystemFolder(new BooleanFilter());
        }
        return isSystemFolder;
    }

    public void setIsSystemFolder(BooleanFilter isSystemFolder) {
        this.isSystemFolder = isSystemFolder;
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

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public Optional<LongFilter> optionalParentId() {
        return Optional.ofNullable(parentId);
    }

    public LongFilter parentId() {
        if (parentId == null) {
            setParentId(new LongFilter());
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
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
        final FileFolderCriteria that = (FileFolderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(path, that.path) &&
            Objects.equals(isSystemFolder, that.isSystemFolder) &&
            Objects.equals(isTrashed, that.isTrashed) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path, isSystemFolder, isTrashed, createdDate, parentId, ownerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileFolderCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalPath().map(f -> "path=" + f + ", ").orElse("") +
            optionalIsSystemFolder().map(f -> "isSystemFolder=" + f + ", ").orElse("") +
            optionalIsTrashed().map(f -> "isTrashed=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalOwnerId().map(f -> "ownerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
