package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.GeoAssocType;
import com.snp.repository.GeoAssocTypeRepository;
import com.snp.service.criteria.GeoAssocTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GeoAssocType} entities in the database.
 * The main input is a {@link GeoAssocTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GeoAssocType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeoAssocTypeQueryService extends QueryService<GeoAssocType> {

    private static final Logger LOG = LoggerFactory.getLogger(GeoAssocTypeQueryService.class);

    private final GeoAssocTypeRepository geoAssocTypeRepository;

    public GeoAssocTypeQueryService(GeoAssocTypeRepository geoAssocTypeRepository) {
        this.geoAssocTypeRepository = geoAssocTypeRepository;
    }

    /**
     * Return a {@link List} of {@link GeoAssocType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GeoAssocType> findByCriteria(GeoAssocTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<GeoAssocType> specification = createSpecification(criteria);
        return geoAssocTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeoAssocTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<GeoAssocType> specification = createSpecification(criteria);
        return geoAssocTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link GeoAssocTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GeoAssocType> createSpecification(GeoAssocTypeCriteria criteria) {
        Specification<GeoAssocType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), GeoAssocType_.id),
                    buildStringSpecification(criteria.getName(), GeoAssocType_.name)
                )
            );
        }
        return specification;
    }
}
