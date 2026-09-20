package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.TaxSlab;
import com.snp.repository.TaxSlabRepository;
import com.snp.service.criteria.TaxSlabCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TaxSlab} entities in the database.
 * The main input is a {@link TaxSlabCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaxSlab} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaxSlabQueryService extends QueryService<TaxSlab> {

    private static final Logger LOG = LoggerFactory.getLogger(TaxSlabQueryService.class);

    private final TaxSlabRepository taxSlabRepository;

    public TaxSlabQueryService(TaxSlabRepository taxSlabRepository) {
        this.taxSlabRepository = taxSlabRepository;
    }

    /**
     * Return a {@link List} of {@link TaxSlab} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaxSlab> findByCriteria(TaxSlabCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<TaxSlab> specification = createSpecification(criteria);
        return taxSlabRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaxSlabCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TaxSlab> specification = createSpecification(criteria);
        return taxSlabRepository.count(specification);
    }

    /**
     * Function to convert {@link TaxSlabCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaxSlab> createSpecification(TaxSlabCriteria criteria) {
        Specification<TaxSlab> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), TaxSlab_.id),
                    buildStringSpecification(criteria.getName(), TaxSlab_.name),
                    buildRangeSpecification(criteria.getRate(), TaxSlab_.rate)
                )
            );
        }
        return specification;
    }
}
