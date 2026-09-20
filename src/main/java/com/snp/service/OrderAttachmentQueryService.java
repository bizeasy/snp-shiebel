package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.OrderAttachment;
import com.snp.repository.OrderAttachmentRepository;
import com.snp.service.criteria.OrderAttachmentCriteria;
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
 * Service for executing complex queries for {@link OrderAttachment} entities in the database.
 * The main input is a {@link OrderAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderAttachment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderAttachmentQueryService extends QueryService<OrderAttachment> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderAttachmentQueryService.class);

    private final OrderAttachmentRepository orderAttachmentRepository;

    public OrderAttachmentQueryService(OrderAttachmentRepository orderAttachmentRepository) {
        this.orderAttachmentRepository = orderAttachmentRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderAttachment> findByCriteria(OrderAttachmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderAttachment> specification = createSpecification(criteria);
        return orderAttachmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderAttachmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderAttachment> specification = createSpecification(criteria);
        return orderAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderAttachment> createSpecification(OrderAttachmentCriteria criteria) {
        Specification<OrderAttachment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(OrderAttachment_.order, JoinType.LEFT);
                root.fetch(OrderAttachment_.attachment, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), OrderAttachment_.id),
                    buildStringSpecification(criteria.getName(), OrderAttachment_.name),
                    buildSpecification(criteria.getOrderId(), root -> root.join(OrderAttachment_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getAttachmentId(), root ->
                        root.join(OrderAttachment_.attachment, JoinType.LEFT).get(Attachment_.id)
                    )
                )
            );
        }
        return specification;
    }
}
