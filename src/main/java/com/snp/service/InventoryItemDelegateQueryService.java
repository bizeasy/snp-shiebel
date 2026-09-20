package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.InventoryItemDelegate;
import com.snp.repository.InventoryItemDelegateRepository;
import com.snp.service.criteria.InventoryItemDelegateCriteria;
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
 * Service for executing complex queries for {@link InventoryItemDelegate} entities in the database.
 * The main input is a {@link InventoryItemDelegateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InventoryItemDelegate} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InventoryItemDelegateQueryService extends QueryService<InventoryItemDelegate> {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemDelegateQueryService.class);

    private final InventoryItemDelegateRepository inventoryItemDelegateRepository;

    public InventoryItemDelegateQueryService(InventoryItemDelegateRepository inventoryItemDelegateRepository) {
        this.inventoryItemDelegateRepository = inventoryItemDelegateRepository;
    }

    /**
     * Return a {@link Page} of {@link InventoryItemDelegate} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InventoryItemDelegate> findByCriteria(InventoryItemDelegateCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InventoryItemDelegate> specification = createSpecification(criteria);
        return inventoryItemDelegateRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InventoryItemDelegateCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InventoryItemDelegate> specification = createSpecification(criteria);
        return inventoryItemDelegateRepository.count(specification);
    }

    /**
     * Function to convert {@link InventoryItemDelegateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InventoryItemDelegate> createSpecification(InventoryItemDelegateCriteria criteria) {
        Specification<InventoryItemDelegate> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(InventoryItemDelegate_.inventoryItem, JoinType.LEFT);
                root.fetch(InventoryItemDelegate_.order, JoinType.LEFT);
                root.fetch(InventoryItemDelegate_.orderItem, JoinType.LEFT);
                root.fetch(InventoryItemDelegate_.invoice, JoinType.LEFT);
                root.fetch(InventoryItemDelegate_.invoiceItem, JoinType.LEFT);
                root.fetch(InventoryItemDelegate_.shipment, JoinType.LEFT);
                root.fetch(InventoryItemDelegate_.shipmentItem, JoinType.LEFT);
                root.fetch(InventoryItemDelegate_.itemIssuance, JoinType.LEFT);
                root.fetch(InventoryItemDelegate_.receipt, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), InventoryItemDelegate_.id),
                    buildRangeSpecification(criteria.getSequenceNo(), InventoryItemDelegate_.sequenceNo),
                    buildRangeSpecification(criteria.getEffectiveDate(), InventoryItemDelegate_.effectiveDate),
                    buildRangeSpecification(criteria.getQuantityOnHandDiff(), InventoryItemDelegate_.quantityOnHandDiff),
                    buildRangeSpecification(criteria.getAvailableToPromiseDiff(), InventoryItemDelegate_.availableToPromiseDiff),
                    buildRangeSpecification(criteria.getAccountingQuantityDiff(), InventoryItemDelegate_.accountingQuantityDiff),
                    buildRangeSpecification(criteria.getUnitCost(), InventoryItemDelegate_.unitCost),
                    buildStringSpecification(criteria.getRemarks(), InventoryItemDelegate_.remarks),
                    buildSpecification(criteria.getInventoryItemId(), root ->
                        root.join(InventoryItemDelegate_.inventoryItem, JoinType.LEFT).get(InventoryItem_.id)
                    ),
                    buildSpecification(criteria.getOrderId(), root ->
                        root.join(InventoryItemDelegate_.order, JoinType.LEFT).get(Order_.id)
                    ),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(InventoryItemDelegate_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    ),
                    buildSpecification(criteria.getInvoiceId(), root ->
                        root.join(InventoryItemDelegate_.invoice, JoinType.LEFT).get(Invoice_.id)
                    ),
                    buildSpecification(criteria.getInvoiceItemId(), root ->
                        root.join(InventoryItemDelegate_.invoiceItem, JoinType.LEFT).get(InvoiceItem_.id)
                    ),
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(InventoryItemDelegate_.shipment, JoinType.LEFT).get(Shipment_.id)
                    ),
                    buildSpecification(criteria.getShipmentItemId(), root ->
                        root.join(InventoryItemDelegate_.shipmentItem, JoinType.LEFT).get(ShipmentItem_.id)
                    ),
                    buildSpecification(criteria.getItemIssuanceId(), root ->
                        root.join(InventoryItemDelegate_.itemIssuance, JoinType.LEFT).get(ItemIssuance_.id)
                    ),
                    buildSpecification(criteria.getReceiptId(), root ->
                        root.join(InventoryItemDelegate_.receipt, JoinType.LEFT).get(ShipmentReceipt_.id)
                    )
                )
            );
        }
        return specification;
    }
}
