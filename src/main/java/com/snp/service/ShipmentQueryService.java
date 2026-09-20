package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Shipment;
import com.snp.repository.ShipmentRepository;
import com.snp.service.criteria.ShipmentCriteria;
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
 * Service for executing complex queries for {@link Shipment} entities in the database.
 * The main input is a {@link ShipmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Shipment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentQueryService extends QueryService<Shipment> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentQueryService.class);

    private final ShipmentRepository shipmentRepository;

    public ShipmentQueryService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    /**
     * Return a {@link Page} of {@link Shipment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Shipment> findByCriteria(ShipmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Shipment> specification = createSpecification(criteria);
        return shipmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Shipment> specification = createSpecification(criteria);
        return shipmentRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Shipment> createSpecification(ShipmentCriteria criteria) {
        Specification<Shipment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Shipment_.shipmentType, JoinType.LEFT);
                root.fetch(Shipment_.shipmentMethodType, JoinType.LEFT);
                root.fetch(Shipment_.status, JoinType.LEFT);
                root.fetch(Shipment_.order, JoinType.LEFT);
                root.fetch(Shipment_.orderItem, JoinType.LEFT);
                root.fetch(Shipment_.originFacility, JoinType.LEFT);
                root.fetch(Shipment_.destinationFacility, JoinType.LEFT);
                root.fetch(Shipment_.fromParty, JoinType.LEFT);
                root.fetch(Shipment_.toParty, JoinType.LEFT);
                root.fetch(Shipment_.carrierParty, JoinType.LEFT);
                root.fetch(Shipment_.contactMech, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Shipment_.id),
                    buildRangeSpecification(criteria.getEstimatedShipDate(), Shipment_.estimatedShipDate),
                    buildRangeSpecification(criteria.getEstimatedReadyDate(), Shipment_.estimatedReadyDate),
                    buildRangeSpecification(criteria.getEstimatedArrivalDate(), Shipment_.estimatedArrivalDate),
                    buildRangeSpecification(criteria.getEstimatedShipCost(), Shipment_.estimatedShipCost),
                    buildSpecification(criteria.getShipmentTypeId(), root ->
                        root.join(Shipment_.shipmentType, JoinType.LEFT).get(ShipmentType_.id)
                    ),
                    buildSpecification(criteria.getShipmentMethodTypeId(), root ->
                        root.join(Shipment_.shipmentMethodType, JoinType.LEFT).get(ShipmentMethodType_.id)
                    ),
                    buildSpecification(criteria.getStatusId(), root -> root.join(Shipment_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getOrderId(), root -> root.join(Shipment_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getOrderItemId(), root -> root.join(Shipment_.orderItem, JoinType.LEFT).get(OrderItem_.id)),
                    buildSpecification(criteria.getOriginFacilityId(), root ->
                        root.join(Shipment_.originFacility, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getDestinationFacilityId(), root ->
                        root.join(Shipment_.destinationFacility, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getFromPartyId(), root -> root.join(Shipment_.fromParty, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getToPartyId(), root -> root.join(Shipment_.toParty, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getCarrierPartyId(), root ->
                        root.join(Shipment_.carrierParty, JoinType.LEFT).get(Party_.id)
                    ),
                    buildSpecification(criteria.getContactMechId(), root ->
                        root.join(Shipment_.contactMech, JoinType.LEFT).get(ContactMech_.id)
                    )
                )
            );
        }
        return specification;
    }
}
