package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CarrierShipmentMethod;
import com.snp.repository.CarrierShipmentMethodRepository;
import com.snp.service.criteria.CarrierShipmentMethodCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CarrierShipmentMethod} entities in the database.
 * The main input is a {@link CarrierShipmentMethodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CarrierShipmentMethod} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CarrierShipmentMethodQueryService extends QueryService<CarrierShipmentMethod> {

    private static final Logger LOG = LoggerFactory.getLogger(CarrierShipmentMethodQueryService.class);

    private final CarrierShipmentMethodRepository carrierShipmentMethodRepository;

    public CarrierShipmentMethodQueryService(CarrierShipmentMethodRepository carrierShipmentMethodRepository) {
        this.carrierShipmentMethodRepository = carrierShipmentMethodRepository;
    }

    /**
     * Return a {@link List} of {@link CarrierShipmentMethod} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CarrierShipmentMethod> findByCriteria(CarrierShipmentMethodCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<CarrierShipmentMethod> specification = createSpecification(criteria);
        return carrierShipmentMethodRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CarrierShipmentMethodCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CarrierShipmentMethod> specification = createSpecification(criteria);
        return carrierShipmentMethodRepository.count(specification);
    }

    /**
     * Function to convert {@link CarrierShipmentMethodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CarrierShipmentMethod> createSpecification(CarrierShipmentMethodCriteria criteria) {
        Specification<CarrierShipmentMethod> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CarrierShipmentMethod_.shipmentMethodType, JoinType.LEFT);
                root.fetch(CarrierShipmentMethod_.party, JoinType.LEFT);
                root.fetch(CarrierShipmentMethod_.roleType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CarrierShipmentMethod_.id),
                    buildRangeSpecification(criteria.getSequenceNo(), CarrierShipmentMethod_.sequenceNo),
                    buildStringSpecification(criteria.getCarrierServiceCode(), CarrierShipmentMethod_.carrierServiceCode),
                    buildSpecification(criteria.getShipmentMethodTypeId(), root ->
                        root.join(CarrierShipmentMethod_.shipmentMethodType, JoinType.LEFT).get(ShipmentMethodType_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root ->
                        root.join(CarrierShipmentMethod_.party, JoinType.LEFT).get(Party_.id)
                    ),
                    buildSpecification(criteria.getRoleTypeId(), root ->
                        root.join(CarrierShipmentMethod_.roleType, JoinType.LEFT).get(RoleType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
