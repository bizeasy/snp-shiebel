package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ReturnItemBilling;
import com.snp.repository.ReturnItemBillingRepository;
import com.snp.service.criteria.ReturnItemBillingCriteria;
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
 * Service for executing complex queries for {@link ReturnItemBilling} entities in the database.
 * The main input is a {@link ReturnItemBillingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReturnItemBilling} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReturnItemBillingQueryService extends QueryService<ReturnItemBilling> {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnItemBillingQueryService.class);

    private final ReturnItemBillingRepository returnItemBillingRepository;

    public ReturnItemBillingQueryService(ReturnItemBillingRepository returnItemBillingRepository) {
        this.returnItemBillingRepository = returnItemBillingRepository;
    }

    /**
     * Return a {@link Page} of {@link ReturnItemBilling} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReturnItemBilling> findByCriteria(ReturnItemBillingCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReturnItemBilling> specification = createSpecification(criteria);
        return returnItemBillingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReturnItemBillingCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReturnItemBilling> specification = createSpecification(criteria);
        return returnItemBillingRepository.count(specification);
    }

    /**
     * Function to convert {@link ReturnItemBillingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReturnItemBilling> createSpecification(ReturnItemBillingCriteria criteria) {
        Specification<ReturnItemBilling> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ReturnItemBilling_.returnItem, JoinType.LEFT);
                root.fetch(ReturnItemBilling_.orderItem, JoinType.LEFT);
                root.fetch(ReturnItemBilling_.invoiceItem, JoinType.LEFT);
                root.fetch(ReturnItemBilling_.shipmentReceipt, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ReturnItemBilling_.id),
                    buildRangeSpecification(criteria.getQuantity(), ReturnItemBilling_.quantity),
                    buildRangeSpecification(criteria.getAmount(), ReturnItemBilling_.amount),
                    buildSpecification(criteria.getReturnItemId(), root ->
                        root.join(ReturnItemBilling_.returnItem, JoinType.LEFT).get(ReturnItem_.id)
                    ),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(ReturnItemBilling_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    ),
                    buildSpecification(criteria.getInvoiceItemId(), root ->
                        root.join(ReturnItemBilling_.invoiceItem, JoinType.LEFT).get(InvoiceItem_.id)
                    ),
                    buildSpecification(criteria.getShipmentReceiptId(), root ->
                        root.join(ReturnItemBilling_.shipmentReceipt, JoinType.LEFT).get(ShipmentReceipt_.id)
                    )
                )
            );
        }
        return specification;
    }
}
