package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.OrderItemBilling;
import com.snp.repository.OrderItemBillingRepository;
import com.snp.service.criteria.OrderItemBillingCriteria;
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
 * Service for executing complex queries for {@link OrderItemBilling} entities in the database.
 * The main input is a {@link OrderItemBillingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderItemBilling} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderItemBillingQueryService extends QueryService<OrderItemBilling> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemBillingQueryService.class);

    private final OrderItemBillingRepository orderItemBillingRepository;

    public OrderItemBillingQueryService(OrderItemBillingRepository orderItemBillingRepository) {
        this.orderItemBillingRepository = orderItemBillingRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderItemBilling} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderItemBilling> findByCriteria(OrderItemBillingCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderItemBilling> specification = createSpecification(criteria);
        return orderItemBillingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderItemBillingCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderItemBilling> specification = createSpecification(criteria);
        return orderItemBillingRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderItemBillingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderItemBilling> createSpecification(OrderItemBillingCriteria criteria) {
        Specification<OrderItemBilling> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(OrderItemBilling_.orderItem, JoinType.LEFT);
                root.fetch(OrderItemBilling_.invoiceItem, JoinType.LEFT);
                root.fetch(OrderItemBilling_.shipmentReceipt, JoinType.LEFT);
                root.fetch(OrderItemBilling_.itemIssuance, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), OrderItemBilling_.id),
                    buildRangeSpecification(criteria.getQuantity(), OrderItemBilling_.quantity),
                    buildRangeSpecification(criteria.getAmount(), OrderItemBilling_.amount),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(OrderItemBilling_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    ),
                    buildSpecification(criteria.getInvoiceItemId(), root ->
                        root.join(OrderItemBilling_.invoiceItem, JoinType.LEFT).get(InvoiceItem_.id)
                    ),
                    buildSpecification(criteria.getShipmentReceiptId(), root ->
                        root.join(OrderItemBilling_.shipmentReceipt, JoinType.LEFT).get(ShipmentReceipt_.id)
                    ),
                    buildSpecification(criteria.getItemIssuanceId(), root ->
                        root.join(OrderItemBilling_.itemIssuance, JoinType.LEFT).get(ItemIssuance_.id)
                    )
                )
            );
        }
        return specification;
    }
}
