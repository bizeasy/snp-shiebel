package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Facility;
import com.snp.repository.FacilityRepository;
import com.snp.service.criteria.FacilityCriteria;
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
 * Service for executing complex queries for {@link Facility} entities in the database.
 * The main input is a {@link FacilityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Facility} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacilityQueryService extends QueryService<Facility> {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityQueryService.class);

    private final FacilityRepository facilityRepository;

    public FacilityQueryService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    /**
     * Return a {@link Page} of {@link Facility} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Facility> findByCriteria(FacilityCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Facility> specification = createSpecification(criteria);
        return facilityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacilityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Facility> specification = createSpecification(criteria);
        return facilityRepository.count(specification);
    }

    /**
     * Function to convert {@link FacilityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Facility> createSpecification(FacilityCriteria criteria) {
        Specification<Facility> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Facility_.facilityType, JoinType.LEFT);
                root.fetch(Facility_.parentFacility, JoinType.LEFT);
                root.fetch(Facility_.ownerParty, JoinType.LEFT);
                root.fetch(Facility_.facilityGroup, JoinType.LEFT);
                root.fetch(Facility_.sizeUom, JoinType.LEFT);
                root.fetch(Facility_.geoPoint, JoinType.LEFT);
                root.fetch(Facility_.inventoryItemType, JoinType.LEFT);
                root.fetch(Facility_.productStore, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Facility_.id),
                    buildStringSpecification(criteria.getName(), Facility_.name),
                    buildStringSpecification(criteria.getDescription(), Facility_.description),
                    buildRangeSpecification(criteria.getFacilitySize(), Facility_.facilitySize),
                    buildSpecification(criteria.getFacilityTypeId(), root ->
                        root.join(Facility_.facilityType, JoinType.LEFT).get(FacilityType_.id)
                    ),
                    buildSpecification(criteria.getParentFacilityId(), root ->
                        root.join(Facility_.parentFacility, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getOwnerPartyId(), root -> root.join(Facility_.ownerParty, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getFacilityGroupId(), root ->
                        root.join(Facility_.facilityGroup, JoinType.LEFT).get(FacilityGroup_.id)
                    ),
                    buildSpecification(criteria.getSizeUomId(), root -> root.join(Facility_.sizeUom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getGeoPointId(), root -> root.join(Facility_.geoPoint, JoinType.LEFT).get(GeoPoint_.id)),
                    buildSpecification(criteria.getInventoryItemTypeId(), root ->
                        root.join(Facility_.inventoryItemType, JoinType.LEFT).get(InventoryItemType_.id)
                    ),
                    buildSpecification(criteria.getProductStoreId(), root ->
                        root.join(Facility_.productStore, JoinType.LEFT).get(ProductStore_.id)
                    )
                )
            );
        }
        return specification;
    }
}
