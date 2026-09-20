package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Geo;
import com.snp.repository.GeoRepository;
import com.snp.service.criteria.GeoCriteria;
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
 * Service for executing complex queries for {@link Geo} entities in the database.
 * The main input is a {@link GeoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Geo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeoQueryService extends QueryService<Geo> {

    private static final Logger LOG = LoggerFactory.getLogger(GeoQueryService.class);

    private final GeoRepository geoRepository;

    public GeoQueryService(GeoRepository geoRepository) {
        this.geoRepository = geoRepository;
    }

    /**
     * Return a {@link Page} of {@link Geo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Geo> findByCriteria(GeoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Geo> specification = createSpecification(criteria);
        return geoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Geo> specification = createSpecification(criteria);
        return geoRepository.count(specification);
    }

    /**
     * Function to convert {@link GeoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Geo> createSpecification(GeoCriteria criteria) {
        Specification<Geo> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Geo_.geoType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Geo_.id),
                    buildStringSpecification(criteria.getName(), Geo_.name),
                    buildStringSpecification(criteria.getCode(), Geo_.code),
                    buildStringSpecification(criteria.getAbbreviation(), Geo_.abbreviation),
                    buildSpecification(criteria.getGeoTypeId(), root -> root.join(Geo_.geoType, JoinType.LEFT).get(GeoType_.id))
                )
            );
        }
        return specification;
    }
}
