package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.OrderContactMech;
import com.snp.repository.OrderContactMechRepository;
import com.snp.service.criteria.OrderContactMechCriteria;
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
 * Service for executing complex queries for {@link OrderContactMech} entities in the database.
 * The main input is a {@link OrderContactMechCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderContactMech} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderContactMechQueryService extends QueryService<OrderContactMech> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderContactMechQueryService.class);

    private final OrderContactMechRepository orderContactMechRepository;

    public OrderContactMechQueryService(OrderContactMechRepository orderContactMechRepository) {
        this.orderContactMechRepository = orderContactMechRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderContactMech} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderContactMech> findByCriteria(OrderContactMechCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderContactMech> specification = createSpecification(criteria);
        return orderContactMechRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderContactMechCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderContactMech> specification = createSpecification(criteria);
        return orderContactMechRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderContactMechCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderContactMech> createSpecification(OrderContactMechCriteria criteria) {
        Specification<OrderContactMech> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(OrderContactMech_.order, JoinType.LEFT);
                root.fetch(OrderContactMech_.contactMech, JoinType.LEFT);
                root.fetch(OrderContactMech_.contactMechPurpose, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), OrderContactMech_.id),
                    buildRangeSpecification(criteria.getFromDate(), OrderContactMech_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), OrderContactMech_.thruDate),
                    buildSpecification(criteria.getOrderId(), root -> root.join(OrderContactMech_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getContactMechId(), root ->
                        root.join(OrderContactMech_.contactMech, JoinType.LEFT).get(ContactMech_.id)
                    ),
                    buildSpecification(criteria.getContactMechPurposeId(), root ->
                        root.join(OrderContactMech_.contactMechPurpose, JoinType.LEFT).get(ContactMechPurpose_.id)
                    )
                )
            );
        }
        return specification;
    }
}
