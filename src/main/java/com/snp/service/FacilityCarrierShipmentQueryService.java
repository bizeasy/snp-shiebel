package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.FacilityCarrierShipment;
import com.snp.repository.FacilityCarrierShipmentRepository;
import com.snp.service.criteria.FacilityCarrierShipmentCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FacilityCarrierShipment} entities in the database.
 * The main input is a {@link FacilityCarrierShipmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacilityCarrierShipment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacilityCarrierShipmentQueryService extends QueryService<FacilityCarrierShipment> {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityCarrierShipmentQueryService.class);

    private final FacilityCarrierShipmentRepository facilityCarrierShipmentRepository;

    public FacilityCarrierShipmentQueryService(FacilityCarrierShipmentRepository facilityCarrierShipmentRepository) {
        this.facilityCarrierShipmentRepository = facilityCarrierShipmentRepository;
    }

    /**
     * Return a {@link List} of {@link FacilityCarrierShipment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacilityCarrierShipment> findByCriteria(FacilityCarrierShipmentCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<FacilityCarrierShipment> specification = createSpecification(criteria);
        return facilityCarrierShipmentRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacilityCarrierShipmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FacilityCarrierShipment> specification = createSpecification(criteria);
        return facilityCarrierShipmentRepository.count(specification);
    }

    /**
     * Function to convert {@link FacilityCarrierShipmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FacilityCarrierShipment> createSpecification(FacilityCarrierShipmentCriteria criteria) {
        Specification<FacilityCarrierShipment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(FacilityCarrierShipment_.facility, JoinType.LEFT);
                root.fetch(FacilityCarrierShipment_.party, JoinType.LEFT);
                root.fetch(FacilityCarrierShipment_.roleType, JoinType.LEFT);
                root.fetch(FacilityCarrierShipment_.shipmentMethodType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), FacilityCarrierShipment_.id),
                    buildSpecification(criteria.getFacilityId(), root ->
                        root.join(FacilityCarrierShipment_.facility, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root ->
                        root.join(FacilityCarrierShipment_.party, JoinType.LEFT).get(Party_.id)
                    ),
                    buildSpecification(criteria.getRoleTypeId(), root ->
                        root.join(FacilityCarrierShipment_.roleType, JoinType.LEFT).get(RoleType_.id)
                    ),
                    buildSpecification(criteria.getShipmentMethodTypeId(), root ->
                        root.join(FacilityCarrierShipment_.shipmentMethodType, JoinType.LEFT).get(ShipmentMethodType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
