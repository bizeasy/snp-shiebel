package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ShipmentItem;
import com.snp.repository.ShipmentItemRepository;
import com.snp.service.criteria.ShipmentItemCriteria;
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
 * Service for executing complex queries for {@link ShipmentItem} entities in the database.
 * The main input is a {@link ShipmentItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShipmentItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentItemQueryService extends QueryService<ShipmentItem> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentItemQueryService.class);

    private final ShipmentItemRepository shipmentItemRepository;

    public ShipmentItemQueryService(ShipmentItemRepository shipmentItemRepository) {
        this.shipmentItemRepository = shipmentItemRepository;
    }

    /**
     * Return a {@link Page} of {@link ShipmentItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentItem> findByCriteria(ShipmentItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShipmentItem> specification = createSpecification(criteria);
        return shipmentItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShipmentItem> specification = createSpecification(criteria);
        return shipmentItemRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentItem> createSpecification(ShipmentItemCriteria criteria) {
        Specification<ShipmentItem> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ShipmentItem_.shipment, JoinType.LEFT);
                root.fetch(ShipmentItem_.product, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ShipmentItem_.id),
                    buildRangeSpecification(criteria.getSequenceNo(), ShipmentItem_.sequenceNo),
                    buildRangeSpecification(criteria.getQuantity(), ShipmentItem_.quantity),
                    buildStringSpecification(criteria.getMessage(), ShipmentItem_.message),
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(ShipmentItem_.shipment, JoinType.LEFT).get(Shipment_.id)
                    ),
                    buildSpecification(criteria.getProductId(), root -> root.join(ShipmentItem_.product, JoinType.LEFT).get(Product_.id))
                )
            );
        }
        return specification;
    }
}
