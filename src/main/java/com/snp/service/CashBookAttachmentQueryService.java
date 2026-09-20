package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CashBookAttachment;
import com.snp.repository.CashBookAttachmentRepository;
import com.snp.service.criteria.CashBookAttachmentCriteria;
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
 * Service for executing complex queries for {@link CashBookAttachment} entities in the database.
 * The main input is a {@link CashBookAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CashBookAttachment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CashBookAttachmentQueryService extends QueryService<CashBookAttachment> {

    private static final Logger LOG = LoggerFactory.getLogger(CashBookAttachmentQueryService.class);

    private final CashBookAttachmentRepository cashBookAttachmentRepository;

    public CashBookAttachmentQueryService(CashBookAttachmentRepository cashBookAttachmentRepository) {
        this.cashBookAttachmentRepository = cashBookAttachmentRepository;
    }

    /**
     * Return a {@link Page} of {@link CashBookAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CashBookAttachment> findByCriteria(CashBookAttachmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CashBookAttachment> specification = createSpecification(criteria);
        return cashBookAttachmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CashBookAttachmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CashBookAttachment> specification = createSpecification(criteria);
        return cashBookAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link CashBookAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CashBookAttachment> createSpecification(CashBookAttachmentCriteria criteria) {
        Specification<CashBookAttachment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CashBookAttachment_.cashBook, JoinType.LEFT);
                root.fetch(CashBookAttachment_.attachment, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CashBookAttachment_.id),
                    buildStringSpecification(criteria.getName(), CashBookAttachment_.name),
                    buildSpecification(criteria.getCashBookId(), root ->
                        root.join(CashBookAttachment_.cashBook, JoinType.LEFT).get(CashBook_.id)
                    ),
                    buildSpecification(criteria.getAttachmentId(), root ->
                        root.join(CashBookAttachment_.attachment, JoinType.LEFT).get(Attachment_.id)
                    )
                )
            );
        }
        return specification;
    }
}
