package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ShipmentType;
import com.snp.repository.ShipmentTypeRepository;
import com.snp.service.criteria.ShipmentTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ShipmentType} entities in the database.
 * The main input is a {@link ShipmentTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShipmentType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentTypeQueryService extends QueryService<ShipmentType> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentTypeQueryService.class);

    private final ShipmentTypeRepository shipmentTypeRepository;

    public ShipmentTypeQueryService(ShipmentTypeRepository shipmentTypeRepository) {
        this.shipmentTypeRepository = shipmentTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ShipmentType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShipmentType> findByCriteria(ShipmentTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ShipmentType> specification = createSpecification(criteria);
        return shipmentTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShipmentType> specification = createSpecification(criteria);
        return shipmentTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentType> createSpecification(ShipmentTypeCriteria criteria) {
        Specification<ShipmentType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ShipmentType_.id),
                    buildStringSpecification(criteria.getName(), ShipmentType_.name),
                    buildStringSpecification(criteria.getDescription(), ShipmentType_.description)
                )
            );
        }
        return specification;
    }
}
