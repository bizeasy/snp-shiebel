package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.GeoType;
import com.snp.repository.GeoTypeRepository;
import com.snp.service.criteria.GeoTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GeoType} entities in the database.
 * The main input is a {@link GeoTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GeoType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeoTypeQueryService extends QueryService<GeoType> {

    private static final Logger LOG = LoggerFactory.getLogger(GeoTypeQueryService.class);

    private final GeoTypeRepository geoTypeRepository;

    public GeoTypeQueryService(GeoTypeRepository geoTypeRepository) {
        this.geoTypeRepository = geoTypeRepository;
    }

    /**
     * Return a {@link List} of {@link GeoType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GeoType> findByCriteria(GeoTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<GeoType> specification = createSpecification(criteria);
        return geoTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeoTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<GeoType> specification = createSpecification(criteria);
        return geoTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link GeoTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GeoType> createSpecification(GeoTypeCriteria criteria) {
        Specification<GeoType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), GeoType_.id),
                    buildStringSpecification(criteria.getName(), GeoType_.name)
                )
            );
        }
        return specification;
    }
}
