package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ShipmentReceipt;
import com.snp.repository.ShipmentReceiptRepository;
import com.snp.service.criteria.ShipmentReceiptCriteria;
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
 * Service for executing complex queries for {@link ShipmentReceipt} entities in the database.
 * The main input is a {@link ShipmentReceiptCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShipmentReceipt} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentReceiptQueryService extends QueryService<ShipmentReceipt> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentReceiptQueryService.class);

    private final ShipmentReceiptRepository shipmentReceiptRepository;

    public ShipmentReceiptQueryService(ShipmentReceiptRepository shipmentReceiptRepository) {
        this.shipmentReceiptRepository = shipmentReceiptRepository;
    }

    /**
     * Return a {@link Page} of {@link ShipmentReceipt} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentReceipt> findByCriteria(ShipmentReceiptCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShipmentReceipt> specification = createSpecification(criteria);
        return shipmentReceiptRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentReceiptCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShipmentReceipt> specification = createSpecification(criteria);
        return shipmentReceiptRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentReceiptCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentReceipt> createSpecification(ShipmentReceiptCriteria criteria) {
        Specification<ShipmentReceipt> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ShipmentReceipt_.shipment, JoinType.LEFT);
                root.fetch(ShipmentReceipt_.shipmentItem, JoinType.LEFT);
                root.fetch(ShipmentReceipt_.order, JoinType.LEFT);
                root.fetch(ShipmentReceipt_.orderItem, JoinType.LEFT);
                root.fetch(ShipmentReceipt_.inventoryItem, JoinType.LEFT);
                root.fetch(ShipmentReceipt_.product, JoinType.LEFT);
                root.fetch(ShipmentReceipt_.receivedBy, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ShipmentReceipt_.id),
                    buildRangeSpecification(criteria.getSequenceNo(), ShipmentReceipt_.sequenceNo),
                    buildRangeSpecification(criteria.getQuantity(), ShipmentReceipt_.quantity),
                    buildRangeSpecification(criteria.getQuantityAccepted(), ShipmentReceipt_.quantityAccepted),
                    buildRangeSpecification(criteria.getQuantityRejected(), ShipmentReceipt_.quantityRejected),
                    buildRangeSpecification(criteria.getReceivedDate(), ShipmentReceipt_.receivedDate),
                    buildStringSpecification(criteria.getMessage(), ShipmentReceipt_.message),
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(ShipmentReceipt_.shipment, JoinType.LEFT).get(Shipment_.id)
                    ),
                    buildSpecification(criteria.getShipmentItemId(), root ->
                        root.join(ShipmentReceipt_.shipmentItem, JoinType.LEFT).get(ShipmentItem_.id)
                    ),
                    buildSpecification(criteria.getOrderId(), root -> root.join(ShipmentReceipt_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(ShipmentReceipt_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    ),
                    buildSpecification(criteria.getInventoryItemId(), root ->
                        root.join(ShipmentReceipt_.inventoryItem, JoinType.LEFT).get(InventoryItem_.id)
                    ),
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(ShipmentReceipt_.product, JoinType.LEFT).get(Product_.id)
                    ),
                    buildSpecification(criteria.getReceivedById(), root ->
                        root.join(ShipmentReceipt_.receivedBy, JoinType.LEFT).get(Party_.id)
                    )
                )
            );
        }
        return specification;
    }
}
