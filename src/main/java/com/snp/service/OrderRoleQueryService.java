package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.OrderRole;
import com.snp.repository.OrderRoleRepository;
import com.snp.service.criteria.OrderRoleCriteria;
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
 * Service for executing complex queries for {@link OrderRole} entities in the database.
 * The main input is a {@link OrderRoleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderRole} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderRoleQueryService extends QueryService<OrderRole> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderRoleQueryService.class);

    private final OrderRoleRepository orderRoleRepository;

    public OrderRoleQueryService(OrderRoleRepository orderRoleRepository) {
        this.orderRoleRepository = orderRoleRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderRole} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderRole> findByCriteria(OrderRoleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderRole> specification = createSpecification(criteria);
        return orderRoleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderRoleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderRole> specification = createSpecification(criteria);
        return orderRoleRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderRoleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderRole> createSpecification(OrderRoleCriteria criteria) {
        Specification<OrderRole> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(OrderRole_.order, JoinType.LEFT);
                root.fetch(OrderRole_.orderItem, JoinType.LEFT);
                root.fetch(OrderRole_.party, JoinType.LEFT);
                root.fetch(OrderRole_.roleType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), OrderRole_.id),
                    buildSpecification(criteria.getOrderId(), root -> root.join(OrderRole_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(OrderRole_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root -> root.join(OrderRole_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getRoleTypeId(), root -> root.join(OrderRole_.roleType, JoinType.LEFT).get(RoleType_.id))
                )
            );
        }
        return specification;
    }
}
