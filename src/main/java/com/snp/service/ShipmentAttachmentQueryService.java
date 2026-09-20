package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ShipmentAttachment;
import com.snp.repository.ShipmentAttachmentRepository;
import com.snp.service.criteria.ShipmentAttachmentCriteria;
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
 * Service for executing complex queries for {@link ShipmentAttachment} entities in the database.
 * The main input is a {@link ShipmentAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShipmentAttachment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentAttachmentQueryService extends QueryService<ShipmentAttachment> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentAttachmentQueryService.class);

    private final ShipmentAttachmentRepository shipmentAttachmentRepository;

    public ShipmentAttachmentQueryService(ShipmentAttachmentRepository shipmentAttachmentRepository) {
        this.shipmentAttachmentRepository = shipmentAttachmentRepository;
    }

    /**
     * Return a {@link Page} of {@link ShipmentAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentAttachment> findByCriteria(ShipmentAttachmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShipmentAttachment> specification = createSpecification(criteria);
        return shipmentAttachmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentAttachmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShipmentAttachment> specification = createSpecification(criteria);
        return shipmentAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentAttachment> createSpecification(ShipmentAttachmentCriteria criteria) {
        Specification<ShipmentAttachment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ShipmentAttachment_.shipment, JoinType.LEFT);
                root.fetch(ShipmentAttachment_.attachment, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ShipmentAttachment_.id),
                    buildStringSpecification(criteria.getName(), ShipmentAttachment_.name),
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(ShipmentAttachment_.shipment, JoinType.LEFT).get(Shipment_.id)
                    ),
                    buildSpecification(criteria.getAttachmentId(), root ->
                        root.join(ShipmentAttachment_.attachment, JoinType.LEFT).get(Attachment_.id)
                    )
                )
            );
        }
        return specification;
    }
}
