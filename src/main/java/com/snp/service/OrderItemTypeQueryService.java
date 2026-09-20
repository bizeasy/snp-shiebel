package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.OrderItemType;
import com.snp.repository.OrderItemTypeRepository;
import com.snp.service.criteria.OrderItemTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OrderItemType} entities in the database.
 * The main input is a {@link OrderItemTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderItemType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderItemTypeQueryService extends QueryService<OrderItemType> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemTypeQueryService.class);

    private final OrderItemTypeRepository orderItemTypeRepository;

    public OrderItemTypeQueryService(OrderItemTypeRepository orderItemTypeRepository) {
        this.orderItemTypeRepository = orderItemTypeRepository;
    }

    /**
     * Return a {@link List} of {@link OrderItemType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderItemType> findByCriteria(OrderItemTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<OrderItemType> specification = createSpecification(criteria);
        return orderItemTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderItemTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderItemType> specification = createSpecification(criteria);
        return orderItemTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderItemTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderItemType> createSpecification(OrderItemTypeCriteria criteria) {
        Specification<OrderItemType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), OrderItemType_.id),
                    buildStringSpecification(criteria.getName(), OrderItemType_.name),
                    buildStringSpecification(criteria.getDescription(), OrderItemType_.description)
                )
            );
        }
        return specification;
    }
}
