package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CustRequestItemAttachment;
import com.snp.repository.CustRequestItemAttachmentRepository;
import com.snp.service.criteria.CustRequestItemAttachmentCriteria;
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
 * Service for executing complex queries for {@link CustRequestItemAttachment} entities in the database.
 * The main input is a {@link CustRequestItemAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustRequestItemAttachment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustRequestItemAttachmentQueryService extends QueryService<CustRequestItemAttachment> {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestItemAttachmentQueryService.class);

    private final CustRequestItemAttachmentRepository custRequestItemAttachmentRepository;

    public CustRequestItemAttachmentQueryService(CustRequestItemAttachmentRepository custRequestItemAttachmentRepository) {
        this.custRequestItemAttachmentRepository = custRequestItemAttachmentRepository;
    }

    /**
     * Return a {@link Page} of {@link CustRequestItemAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustRequestItemAttachment> findByCriteria(CustRequestItemAttachmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustRequestItemAttachment> specification = createSpecification(criteria);
        return custRequestItemAttachmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustRequestItemAttachmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustRequestItemAttachment> specification = createSpecification(criteria);
        return custRequestItemAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link CustRequestItemAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustRequestItemAttachment> createSpecification(CustRequestItemAttachmentCriteria criteria) {
        Specification<CustRequestItemAttachment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CustRequestItemAttachment_.custRequestItem, JoinType.LEFT);
                root.fetch(CustRequestItemAttachment_.attachment, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CustRequestItemAttachment_.id),
                    buildStringSpecification(criteria.getName(), CustRequestItemAttachment_.name),
                    buildSpecification(criteria.getCustRequestItemId(), root ->
                        root.join(CustRequestItemAttachment_.custRequestItem, JoinType.LEFT).get(CustRequestItem_.id)
                    ),
                    buildSpecification(criteria.getAttachmentId(), root ->
                        root.join(CustRequestItemAttachment_.attachment, JoinType.LEFT).get(Attachment_.id)
                    )
                )
            );
        }
        return specification;
    }
}
