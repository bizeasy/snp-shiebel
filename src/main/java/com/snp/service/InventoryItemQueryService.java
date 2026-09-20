package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.InventoryItem;
import com.snp.repository.InventoryItemRepository;
import com.snp.service.criteria.InventoryItemCriteria;
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
 * Service for executing complex queries for {@link InventoryItem} entities in the database.
 * The main input is a {@link InventoryItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InventoryItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InventoryItemQueryService extends QueryService<InventoryItem> {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemQueryService.class);

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemQueryService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    /**
     * Return a {@link Page} of {@link InventoryItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InventoryItem> findByCriteria(InventoryItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InventoryItem> specification = createSpecification(criteria);
        return inventoryItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InventoryItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InventoryItem> specification = createSpecification(criteria);
        return inventoryItemRepository.count(specification);
    }

    /**
     * Function to convert {@link InventoryItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InventoryItem> createSpecification(InventoryItemCriteria criteria) {
        Specification<InventoryItem> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(InventoryItem_.inventoryItemType, JoinType.LEFT);
                root.fetch(InventoryItem_.product, JoinType.LEFT);
                root.fetch(InventoryItem_.supplier, JoinType.LEFT);
                root.fetch(InventoryItem_.ownerParty, JoinType.LEFT);
                root.fetch(InventoryItem_.status, JoinType.LEFT);
                root.fetch(InventoryItem_.facility, JoinType.LEFT);
                root.fetch(InventoryItem_.facilityLocation, JoinType.LEFT);
                root.fetch(InventoryItem_.uom, JoinType.LEFT);
                root.fetch(InventoryItem_.currencyUom, JoinType.LEFT);
                root.fetch(InventoryItem_.lot, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), InventoryItem_.id),
                    buildRangeSpecification(criteria.getReceivedDate(), InventoryItem_.receivedDate),
                    buildRangeSpecification(criteria.getManufacturedDate(), InventoryItem_.manufacturedDate),
                    buildRangeSpecification(criteria.getExpiryDate(), InventoryItem_.expiryDate),
                    buildRangeSpecification(criteria.getRetestDate(), InventoryItem_.retestDate),
                    buildStringSpecification(criteria.getContainerId(), InventoryItem_.containerId),
                    buildStringSpecification(criteria.getBatchNo(), InventoryItem_.batchNo),
                    buildStringSpecification(criteria.getMfgBatchNo(), InventoryItem_.mfgBatchNo),
                    buildStringSpecification(criteria.getLotNoStr(), InventoryItem_.lotNoStr),
                    buildStringSpecification(criteria.getBinNumber(), InventoryItem_.binNumber),
                    buildStringSpecification(criteria.getComments(), InventoryItem_.comments),
                    buildRangeSpecification(criteria.getQuantityOnHandTotal(), InventoryItem_.quantityOnHandTotal),
                    buildRangeSpecification(criteria.getAvailableToPromiseTotal(), InventoryItem_.availableToPromiseTotal),
                    buildRangeSpecification(criteria.getAccountingQuantityTotal(), InventoryItem_.accountingQuantityTotal),
                    buildRangeSpecification(criteria.getOldQuantityOnHand(), InventoryItem_.oldQuantityOnHand),
                    buildRangeSpecification(criteria.getOldAvailableToPromise(), InventoryItem_.oldAvailableToPromise),
                    buildStringSpecification(criteria.getSerialNumber(), InventoryItem_.serialNumber),
                    buildStringSpecification(criteria.getSoftIdentifier(), InventoryItem_.softIdentifier),
                    buildStringSpecification(criteria.getActivationNumber(), InventoryItem_.activationNumber),
                    buildRangeSpecification(criteria.getActivationValidThru(), InventoryItem_.activationValidThru),
                    buildRangeSpecification(criteria.getUnitCost(), InventoryItem_.unitCost),
                    buildSpecification(criteria.getInventoryItemTypeId(), root ->
                        root.join(InventoryItem_.inventoryItemType, JoinType.LEFT).get(InventoryItemType_.id)
                    ),
                    buildSpecification(criteria.getProductId(), root -> root.join(InventoryItem_.product, JoinType.LEFT).get(Product_.id)),
                    buildSpecification(criteria.getSupplierId(), root -> root.join(InventoryItem_.supplier, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getOwnerPartyId(), root ->
                        root.join(InventoryItem_.ownerParty, JoinType.LEFT).get(Party_.id)
                    ),
                    buildSpecification(criteria.getStatusId(), root -> root.join(InventoryItem_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getFacilityId(), root ->
                        root.join(InventoryItem_.facility, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getFacilityLocationId(), root ->
                        root.join(InventoryItem_.facilityLocation, JoinType.LEFT).get(FacilityLocation_.id)
                    ),
                    buildSpecification(criteria.getUomId(), root -> root.join(InventoryItem_.uom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getCurrencyUomId(), root ->
                        root.join(InventoryItem_.currencyUom, JoinType.LEFT).get(Uom_.id)
                    ),
                    buildSpecification(criteria.getLotId(), root -> root.join(InventoryItem_.lot, JoinType.LEFT).get(Lot_.id))
                )
            );
        }
        return specification;
    }
}
