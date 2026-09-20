package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Uom;
import com.snp.repository.UomRepository;
import com.snp.service.criteria.UomCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Uom} entities in the database.
 * The main input is a {@link UomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Uom} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UomQueryService extends QueryService<Uom> {

    private static final Logger LOG = LoggerFactory.getLogger(UomQueryService.class);

    private final UomRepository uomRepository;

    public UomQueryService(UomRepository uomRepository) {
        this.uomRepository = uomRepository;
    }

    /**
     * Return a {@link List} of {@link Uom} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Uom> findByCriteria(UomCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Uom> specification = createSpecification(criteria);
        return uomRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UomCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Uom> specification = createSpecification(criteria);
        return uomRepository.count(specification);
    }

    /**
     * Function to convert {@link UomCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Uom> createSpecification(UomCriteria criteria) {
        Specification<Uom> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Uom_.uomType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Uom_.id),
                    buildStringSpecification(criteria.getName(), Uom_.name),
                    buildStringSpecification(criteria.getDescription(), Uom_.description),
                    buildRangeSpecification(criteria.getSequenceNo(), Uom_.sequenceNo),
                    buildStringSpecification(criteria.getAbbreviation(), Uom_.abbreviation),
                    buildSpecification(criteria.getUomTypeId(), root -> root.join(Uom_.uomType, JoinType.LEFT).get(UomType_.id))
                )
            );
        }
        return specification;
    }
}
