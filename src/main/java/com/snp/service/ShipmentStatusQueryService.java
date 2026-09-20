package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ShipmentStatus;
import com.snp.repository.ShipmentStatusRepository;
import com.snp.service.criteria.ShipmentStatusCriteria;
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
 * Service for executing complex queries for {@link ShipmentStatus} entities in the database.
 * The main input is a {@link ShipmentStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShipmentStatus} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentStatusQueryService extends QueryService<ShipmentStatus> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentStatusQueryService.class);

    private final ShipmentStatusRepository shipmentStatusRepository;

    public ShipmentStatusQueryService(ShipmentStatusRepository shipmentStatusRepository) {
        this.shipmentStatusRepository = shipmentStatusRepository;
    }

    /**
     * Return a {@link Page} of {@link ShipmentStatus} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentStatus> findByCriteria(ShipmentStatusCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShipmentStatus> specification = createSpecification(criteria);
        return shipmentStatusRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentStatusCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShipmentStatus> specification = createSpecification(criteria);
        return shipmentStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentStatus> createSpecification(ShipmentStatusCriteria criteria) {
        Specification<ShipmentStatus> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ShipmentStatus_.shipment, JoinType.LEFT);
                root.fetch(ShipmentStatus_.status, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ShipmentStatus_.id),
                    buildRangeSpecification(criteria.getStatusDateTime(), ShipmentStatus_.statusDateTime),
                    buildRangeSpecification(criteria.getSequenceNo(), ShipmentStatus_.sequenceNo),
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(ShipmentStatus_.shipment, JoinType.LEFT).get(Shipment_.id)
                    ),
                    buildSpecification(criteria.getStatusId(), root -> root.join(ShipmentStatus_.status, JoinType.LEFT).get(Status_.id))
                )
            );
        }
        return specification;
    }
}
