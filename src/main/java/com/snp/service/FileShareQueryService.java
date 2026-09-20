package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.FileShare;
import com.snp.repository.FileShareRepository;
import com.snp.service.criteria.FileShareCriteria;
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
 * Service for executing complex queries for {@link FileShare} entities in the database.
 * The main input is a {@link FileShareCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FileShare} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FileShareQueryService extends QueryService<FileShare> {

    private static final Logger LOG = LoggerFactory.getLogger(FileShareQueryService.class);

    private final FileShareRepository fileShareRepository;

    public FileShareQueryService(FileShareRepository fileShareRepository) {
        this.fileShareRepository = fileShareRepository;
    }

    /**
     * Return a {@link Page} of {@link FileShare} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FileShare> findByCriteria(FileShareCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FileShare> specification = createSpecification(criteria);
        return fileShareRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FileShareCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FileShare> specification = createSpecification(criteria);
        return fileShareRepository.count(specification);
    }

    /**
     * Function to convert {@link FileShareCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FileShare> createSpecification(FileShareCriteria criteria) {
        Specification<FileShare> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(FileShare_.folder, JoinType.LEFT);
                root.fetch(FileShare_.attachment, JoinType.LEFT);
                root.fetch(FileShare_.sharedWith, JoinType.LEFT);
                root.fetch(FileShare_.sharedBy, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), FileShare_.id),
                    buildSpecification(criteria.getPermission(), FileShare_.permission),
                    buildStringSpecification(criteria.getSharedWithEmail(), FileShare_.sharedWithEmail),
                    buildRangeSpecification(criteria.getSharedDate(), FileShare_.sharedDate),
                    buildRangeSpecification(criteria.getExpiryDate(), FileShare_.expiryDate),
                    buildStringSpecification(criteria.getAccessToken(), FileShare_.accessToken),
                    buildSpecification(criteria.getIsRevoked(), FileShare_.isRevoked),
                    buildSpecification(criteria.getFolderId(), root -> root.join(FileShare_.folder, JoinType.LEFT).get(FileFolder_.id)),
                    buildSpecification(criteria.getAttachmentId(), root ->
                        root.join(FileShare_.attachment, JoinType.LEFT).get(Attachment_.id)
                    ),
                    buildSpecification(criteria.getSharedWithId(), root -> root.join(FileShare_.sharedWith, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getSharedById(), root -> root.join(FileShare_.sharedBy, JoinType.LEFT).get(Party_.id))
                )
            );
        }
        return specification;
    }
}
