package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.TaxAuthority;
import com.snp.repository.TaxAuthorityRepository;
import com.snp.service.criteria.TaxAuthorityCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TaxAuthority} entities in the database.
 * The main input is a {@link TaxAuthorityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaxAuthority} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaxAuthorityQueryService extends QueryService<TaxAuthority> {

    private static final Logger LOG = LoggerFactory.getLogger(TaxAuthorityQueryService.class);

    private final TaxAuthorityRepository taxAuthorityRepository;

    public TaxAuthorityQueryService(TaxAuthorityRepository taxAuthorityRepository) {
        this.taxAuthorityRepository = taxAuthorityRepository;
    }

    /**
     * Return a {@link List} of {@link TaxAuthority} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaxAuthority> findByCriteria(TaxAuthorityCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<TaxAuthority> specification = createSpecification(criteria);
        return taxAuthorityRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaxAuthorityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TaxAuthority> specification = createSpecification(criteria);
        return taxAuthorityRepository.count(specification);
    }

    /**
     * Function to convert {@link TaxAuthorityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaxAuthority> createSpecification(TaxAuthorityCriteria criteria) {
        Specification<TaxAuthority> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(TaxAuthority_.geo, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), TaxAuthority_.id),
                    buildStringSpecification(criteria.getName(), TaxAuthority_.name),
                    buildSpecification(criteria.getGeoId(), root -> root.join(TaxAuthority_.geo, JoinType.LEFT).get(Geo_.id))
                )
            );
        }
        return specification;
    }
}
