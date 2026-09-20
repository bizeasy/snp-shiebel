package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ItemIssuance;
import com.snp.repository.ItemIssuanceRepository;
import com.snp.service.criteria.ItemIssuanceCriteria;
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
 * Service for executing complex queries for {@link ItemIssuance} entities in the database.
 * The main input is a {@link ItemIssuanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ItemIssuance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemIssuanceQueryService extends QueryService<ItemIssuance> {

    private static final Logger LOG = LoggerFactory.getLogger(ItemIssuanceQueryService.class);

    private final ItemIssuanceRepository itemIssuanceRepository;

    public ItemIssuanceQueryService(ItemIssuanceRepository itemIssuanceRepository) {
        this.itemIssuanceRepository = itemIssuanceRepository;
    }

    /**
     * Return a {@link Page} of {@link ItemIssuance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemIssuance> findByCriteria(ItemIssuanceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ItemIssuance> specification = createSpecification(criteria);
        return itemIssuanceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemIssuanceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ItemIssuance> specification = createSpecification(criteria);
        return itemIssuanceRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemIssuanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ItemIssuance> createSpecification(ItemIssuanceCriteria criteria) {
        Specification<ItemIssuance> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ItemIssuance_.inventoryItem, JoinType.LEFT);
                root.fetch(ItemIssuance_.order, JoinType.LEFT);
                root.fetch(ItemIssuance_.orderItem, JoinType.LEFT);
                root.fetch(ItemIssuance_.shipment, JoinType.LEFT);
                root.fetch(ItemIssuance_.shipmentItem, JoinType.LEFT);
                root.fetch(ItemIssuance_.issuedByParty, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ItemIssuance_.id),
                    buildStringSpecification(criteria.getMessage(), ItemIssuance_.message),
                    buildRangeSpecification(criteria.getIssuedDate(), ItemIssuance_.issuedDate),
                    buildStringSpecification(criteria.getIssuedBy(), ItemIssuance_.issuedBy),
                    buildRangeSpecification(criteria.getQuantity(), ItemIssuance_.quantity),
                    buildRangeSpecification(criteria.getCancelQuantity(), ItemIssuance_.cancelQuantity),
                    buildSpecification(criteria.getInventoryItemId(), root ->
                        root.join(ItemIssuance_.inventoryItem, JoinType.LEFT).get(InventoryItem_.id)
                    ),
                    buildSpecification(criteria.getOrderId(), root -> root.join(ItemIssuance_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(ItemIssuance_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    ),
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(ItemIssuance_.shipment, JoinType.LEFT).get(Shipment_.id)
                    ),
                    buildSpecification(criteria.getShipmentItemId(), root ->
                        root.join(ItemIssuance_.shipmentItem, JoinType.LEFT).get(ShipmentItem_.id)
                    ),
                    buildSpecification(criteria.getIssuedByPartyId(), root ->
                        root.join(ItemIssuance_.issuedByParty, JoinType.LEFT).get(Party_.id)
                    )
                )
            );
        }
        return specification;
    }
}
