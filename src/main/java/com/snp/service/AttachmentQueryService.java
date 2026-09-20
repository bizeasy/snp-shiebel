package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Attachment;
import com.snp.repository.AttachmentRepository;
import com.snp.service.criteria.AttachmentCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Attachment} entities in the database.
 * The main input is a {@link AttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Attachment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AttachmentQueryService extends QueryService<Attachment> {

    private static final Logger LOG = LoggerFactory.getLogger(AttachmentQueryService.class);

    private final AttachmentRepository attachmentRepository;

    public AttachmentQueryService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * Return a {@link Page} of {@link Attachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Attachment> findByCriteria(AttachmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Attachment> specification = createSpecification(criteria);
        return attachmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AttachmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Attachment> specification = createSpecification(criteria);
        return attachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link AttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Attachment> createSpecification(AttachmentCriteria criteria) {
        Specification<Attachment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Attachment_.contentType, JoinType.LEFT);
                root.fetch(Attachment_.folder, JoinType.LEFT);
                root.fetch(Attachment_.owner, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Attachment_.id),
                    buildStringSpecification(criteria.getName(), Attachment_.name),
                    buildStringSpecification(criteria.getAttachmentUrl(), Attachment_.attachmentUrl),
                    buildStringSpecification(criteria.getMimeType(), Attachment_.mimeType),
                    buildRangeSpecification(criteria.getFileSize(), Attachment_.fileSize),
                    buildRangeSpecification(criteria.getUploadedDate(), Attachment_.uploadedDate),
                    buildSpecification(criteria.getIsTrashed(), Attachment_.isTrashed),
                    buildStringSpecification(criteria.getChecksum(), Attachment_.checksum),
                    buildSpecification(criteria.getContentTypeId(), root ->
                        root.join(Attachment_.contentType, JoinType.LEFT).get(ContentType_.id)
                    ),
                    buildSpecification(criteria.getFolderId(), root -> root.join(Attachment_.folder, JoinType.LEFT).get(FileFolder_.id)),
                    buildSpecification(criteria.getOwnerId(), root -> root.join(Attachment_.owner, JoinType.LEFT).get(Party_.id))
                )
            );
        }
        return specification;
    }
}
