package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.GeoAssoc;
import com.snp.repository.GeoAssocRepository;
import com.snp.service.criteria.GeoAssocCriteria;
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
 * Service for executing complex queries for {@link GeoAssoc} entities in the database.
 * The main input is a {@link GeoAssocCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link GeoAssoc} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeoAssocQueryService extends QueryService<GeoAssoc> {

    private static final Logger LOG = LoggerFactory.getLogger(GeoAssocQueryService.class);

    private final GeoAssocRepository geoAssocRepository;

    public GeoAssocQueryService(GeoAssocRepository geoAssocRepository) {
        this.geoAssocRepository = geoAssocRepository;
    }

    /**
     * Return a {@link Page} of {@link GeoAssoc} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GeoAssoc> findByCriteria(GeoAssocCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GeoAssoc> specification = createSpecification(criteria);
        return geoAssocRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeoAssocCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<GeoAssoc> specification = createSpecification(criteria);
        return geoAssocRepository.count(specification);
    }

    /**
     * Function to convert {@link GeoAssocCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GeoAssoc> createSpecification(GeoAssocCriteria criteria) {
        Specification<GeoAssoc> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(GeoAssoc_.geo, JoinType.LEFT);
                root.fetch(GeoAssoc_.geoTo, JoinType.LEFT);
                root.fetch(GeoAssoc_.geoAssocType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), GeoAssoc_.id),
                    buildSpecification(criteria.getGeoId(), root -> root.join(GeoAssoc_.geo, JoinType.LEFT).get(Geo_.id)),
                    buildSpecification(criteria.getGeoToId(), root -> root.join(GeoAssoc_.geoTo, JoinType.LEFT).get(Geo_.id)),
                    buildSpecification(criteria.getGeoAssocTypeId(), root ->
                        root.join(GeoAssoc_.geoAssocType, JoinType.LEFT).get(GeoAssocType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
