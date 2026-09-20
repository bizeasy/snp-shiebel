package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.OrderStatus;
import com.snp.repository.OrderStatusRepository;
import com.snp.service.criteria.OrderStatusCriteria;
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
 * Service for executing complex queries for {@link OrderStatus} entities in the database.
 * The main input is a {@link OrderStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderStatus} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderStatusQueryService extends QueryService<OrderStatus> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderStatusQueryService.class);

    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusQueryService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderStatus} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderStatus> findByCriteria(OrderStatusCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderStatus> specification = createSpecification(criteria);
        return orderStatusRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderStatusCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderStatus> specification = createSpecification(criteria);
        return orderStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderStatus> createSpecification(OrderStatusCriteria criteria) {
        Specification<OrderStatus> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(OrderStatus_.order, JoinType.LEFT);
                root.fetch(OrderStatus_.status, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), OrderStatus_.id),
                    buildRangeSpecification(criteria.getStatusDateTime(), OrderStatus_.statusDateTime),
                    buildRangeSpecification(criteria.getSequenceNo(), OrderStatus_.sequenceNo),
                    buildSpecification(criteria.getOrderId(), root -> root.join(OrderStatus_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getStatusId(), root -> root.join(OrderStatus_.status, JoinType.LEFT).get(Status_.id))
                )
            );
        }
        return specification;
    }
}
