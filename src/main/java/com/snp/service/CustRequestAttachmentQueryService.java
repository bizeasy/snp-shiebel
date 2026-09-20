package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CustRequestAttachment;
import com.snp.repository.CustRequestAttachmentRepository;
import com.snp.service.criteria.CustRequestAttachmentCriteria;
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
 * Service for executing complex queries for {@link CustRequestAttachment} entities in the database.
 * The main input is a {@link CustRequestAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustRequestAttachment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustRequestAttachmentQueryService extends QueryService<CustRequestAttachment> {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestAttachmentQueryService.class);

    private final CustRequestAttachmentRepository custRequestAttachmentRepository;

    public CustRequestAttachmentQueryService(CustRequestAttachmentRepository custRequestAttachmentRepository) {
        this.custRequestAttachmentRepository = custRequestAttachmentRepository;
    }

    /**
     * Return a {@link Page} of {@link CustRequestAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustRequestAttachment> findByCriteria(CustRequestAttachmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustRequestAttachment> specification = createSpecification(criteria);
        return custRequestAttachmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustRequestAttachmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustRequestAttachment> specification = createSpecification(criteria);
        return custRequestAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link CustRequestAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustRequestAttachment> createSpecification(CustRequestAttachmentCriteria criteria) {
        Specification<CustRequestAttachment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CustRequestAttachment_.custRequest, JoinType.LEFT);
                root.fetch(CustRequestAttachment_.attachment, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CustRequestAttachment_.id),
                    buildStringSpecification(criteria.getName(), CustRequestAttachment_.name),
                    buildSpecification(criteria.getCustRequestId(), root ->
                        root.join(CustRequestAttachment_.custRequest, JoinType.LEFT).get(CustRequest_.id)
                    ),
                    buildSpecification(criteria.getAttachmentId(), root ->
                        root.join(CustRequestAttachment_.attachment, JoinType.LEFT).get(Attachment_.id)
                    )
                )
            );
        }
        return specification;
    }
}
