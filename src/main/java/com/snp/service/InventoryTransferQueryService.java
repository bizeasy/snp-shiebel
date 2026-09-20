package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.InventoryTransfer;
import com.snp.repository.InventoryTransferRepository;
import com.snp.service.criteria.InventoryTransferCriteria;
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
 * Service for executing complex queries for {@link InventoryTransfer} entities in the database.
 * The main input is a {@link InventoryTransferCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InventoryTransfer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InventoryTransferQueryService extends QueryService<InventoryTransfer> {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryTransferQueryService.class);

    private final InventoryTransferRepository inventoryTransferRepository;

    public InventoryTransferQueryService(InventoryTransferRepository inventoryTransferRepository) {
        this.inventoryTransferRepository = inventoryTransferRepository;
    }

    /**
     * Return a {@link Page} of {@link InventoryTransfer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InventoryTransfer> findByCriteria(InventoryTransferCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InventoryTransfer> specification = createSpecification(criteria);
        return inventoryTransferRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InventoryTransferCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InventoryTransfer> specification = createSpecification(criteria);
        return inventoryTransferRepository.count(specification);
    }

    /**
     * Function to convert {@link InventoryTransferCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InventoryTransfer> createSpecification(InventoryTransferCriteria criteria) {
        Specification<InventoryTransfer> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(InventoryTransfer_.inventoryItem, JoinType.LEFT);
                root.fetch(InventoryTransfer_.status, JoinType.LEFT);
                root.fetch(InventoryTransfer_.facility, JoinType.LEFT);
                root.fetch(InventoryTransfer_.facilityLocation, JoinType.LEFT);
                root.fetch(InventoryTransfer_.facilityTo, JoinType.LEFT);
                root.fetch(InventoryTransfer_.facilityLocationTo, JoinType.LEFT);
                root.fetch(InventoryTransfer_.issuance, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), InventoryTransfer_.id),
                    buildRangeSpecification(criteria.getSentDate(), InventoryTransfer_.sentDate),
                    buildRangeSpecification(criteria.getReceivedDate(), InventoryTransfer_.receivedDate),
                    buildStringSpecification(criteria.getComments(), InventoryTransfer_.comments),
                    buildSpecification(criteria.getInventoryItemId(), root ->
                        root.join(InventoryTransfer_.inventoryItem, JoinType.LEFT).get(InventoryItem_.id)
                    ),
                    buildSpecification(criteria.getStatusId(), root -> root.join(InventoryTransfer_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getFacilityId(), root ->
                        root.join(InventoryTransfer_.facility, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getFacilityLocationId(), root ->
                        root.join(InventoryTransfer_.facilityLocation, JoinType.LEFT).get(FacilityLocation_.id)
                    ),
                    buildSpecification(criteria.getFacilityToId(), root ->
                        root.join(InventoryTransfer_.facilityTo, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getFacilityLocationToId(), root ->
                        root.join(InventoryTransfer_.facilityLocationTo, JoinType.LEFT).get(FacilityLocation_.id)
                    ),
                    buildSpecification(criteria.getIssuanceId(), root ->
                        root.join(InventoryTransfer_.issuance, JoinType.LEFT).get(ItemIssuance_.id)
                    )
                )
            );
        }
        return specification;
    }
}
