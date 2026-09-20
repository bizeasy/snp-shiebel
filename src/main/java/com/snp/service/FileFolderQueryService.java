package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.FileFolder;
import com.snp.repository.FileFolderRepository;
import com.snp.service.criteria.FileFolderCriteria;
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
 * Service for executing complex queries for {@link FileFolder} entities in the database.
 * The main input is a {@link FileFolderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FileFolder} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FileFolderQueryService extends QueryService<FileFolder> {

    private static final Logger LOG = LoggerFactory.getLogger(FileFolderQueryService.class);

    private final FileFolderRepository fileFolderRepository;

    public FileFolderQueryService(FileFolderRepository fileFolderRepository) {
        this.fileFolderRepository = fileFolderRepository;
    }

    /**
     * Return a {@link Page} of {@link FileFolder} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FileFolder> findByCriteria(FileFolderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FileFolder> specification = createSpecification(criteria);
        return fileFolderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FileFolderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FileFolder> specification = createSpecification(criteria);
        return fileFolderRepository.count(specification);
    }

    /**
     * Function to convert {@link FileFolderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FileFolder> createSpecification(FileFolderCriteria criteria) {
        Specification<FileFolder> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(FileFolder_.parent, JoinType.LEFT);
                root.fetch(FileFolder_.owner, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), FileFolder_.id),
                    buildStringSpecification(criteria.getName(), FileFolder_.name),
                    buildStringSpecification(criteria.getPath(), FileFolder_.path),
                    buildSpecification(criteria.getIsSystemFolder(), FileFolder_.isSystemFolder),
                    buildSpecification(criteria.getIsTrashed(), FileFolder_.isTrashed),
                    buildRangeSpecification(criteria.getCreatedDate(), FileFolder_.createdDate),
                    buildSpecification(criteria.getParentId(), root -> root.join(FileFolder_.parent, JoinType.LEFT).get(FileFolder_.id)),
                    buildSpecification(criteria.getOwnerId(), root -> root.join(FileFolder_.owner, JoinType.LEFT).get(Party_.id))
                )
            );
        }
        return specification;
    }
}
