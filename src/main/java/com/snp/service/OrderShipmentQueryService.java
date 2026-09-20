package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.OrderShipment;
import com.snp.repository.OrderShipmentRepository;
import com.snp.service.criteria.OrderShipmentCriteria;
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
 * Service for executing complex queries for {@link OrderShipment} entities in the database.
 * The main input is a {@link OrderShipmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderShipment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderShipmentQueryService extends QueryService<OrderShipment> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderShipmentQueryService.class);

    private final OrderShipmentRepository orderShipmentRepository;

    public OrderShipmentQueryService(OrderShipmentRepository orderShipmentRepository) {
        this.orderShipmentRepository = orderShipmentRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderShipment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderShipment> findByCriteria(OrderShipmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderShipment> specification = createSpecification(criteria);
        return orderShipmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderShipmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderShipment> specification = createSpecification(criteria);
        return orderShipmentRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderShipmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderShipment> createSpecification(OrderShipmentCriteria criteria) {
        Specification<OrderShipment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(OrderShipment_.order, JoinType.LEFT);
                root.fetch(OrderShipment_.orderItem, JoinType.LEFT);
                root.fetch(OrderShipment_.shipment, JoinType.LEFT);
                root.fetch(OrderShipment_.shipmentItem, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), OrderShipment_.id),
                    buildRangeSpecification(criteria.getQuantity(), OrderShipment_.quantity),
                    buildSpecification(criteria.getOrderId(), root -> root.join(OrderShipment_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(OrderShipment_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    ),
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(OrderShipment_.shipment, JoinType.LEFT).get(Shipment_.id)
                    ),
                    buildSpecification(criteria.getShipmentItemId(), root ->
                        root.join(OrderShipment_.shipmentItem, JoinType.LEFT).get(ShipmentItem_.id)
                    )
                )
            );
        }
        return specification;
    }
}
