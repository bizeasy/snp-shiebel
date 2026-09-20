package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.QuoteAttachment;
import com.snp.repository.QuoteAttachmentRepository;
import com.snp.service.criteria.QuoteAttachmentCriteria;
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
 * Service for executing complex queries for {@link QuoteAttachment} entities in the database.
 * The main input is a {@link QuoteAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link QuoteAttachment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuoteAttachmentQueryService extends QueryService<QuoteAttachment> {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteAttachmentQueryService.class);

    private final QuoteAttachmentRepository quoteAttachmentRepository;

    public QuoteAttachmentQueryService(QuoteAttachmentRepository quoteAttachmentRepository) {
        this.quoteAttachmentRepository = quoteAttachmentRepository;
    }

    /**
     * Return a {@link Page} of {@link QuoteAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuoteAttachment> findByCriteria(QuoteAttachmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuoteAttachment> specification = createSpecification(criteria);
        return quoteAttachmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuoteAttachmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<QuoteAttachment> specification = createSpecification(criteria);
        return quoteAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link QuoteAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuoteAttachment> createSpecification(QuoteAttachmentCriteria criteria) {
        Specification<QuoteAttachment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(QuoteAttachment_.quote, JoinType.LEFT);
                root.fetch(QuoteAttachment_.attachment, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), QuoteAttachment_.id),
                    buildStringSpecification(criteria.getName(), QuoteAttachment_.name),
                    buildSpecification(criteria.getQuoteId(), root -> root.join(QuoteAttachment_.quote, JoinType.LEFT).get(Quote_.id)),
                    buildSpecification(criteria.getAttachmentId(), root ->
                        root.join(QuoteAttachment_.attachment, JoinType.LEFT).get(Attachment_.id)
                    )
                )
            );
        }
        return specification;
    }
}
