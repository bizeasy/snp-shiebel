package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.UomConversion;
import com.snp.repository.UomConversionRepository;
import com.snp.service.criteria.UomConversionCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UomConversion} entities in the database.
 * The main input is a {@link UomConversionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UomConversion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UomConversionQueryService extends QueryService<UomConversion> {

    private static final Logger LOG = LoggerFactory.getLogger(UomConversionQueryService.class);

    private final UomConversionRepository uomConversionRepository;

    public UomConversionQueryService(UomConversionRepository uomConversionRepository) {
        this.uomConversionRepository = uomConversionRepository;
    }

    /**
     * Return a {@link List} of {@link UomConversion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UomConversion> findByCriteria(UomConversionCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<UomConversion> specification = createSpecification(criteria);
        return uomConversionRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UomConversionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UomConversion> specification = createSpecification(criteria);
        return uomConversionRepository.count(specification);
    }

    /**
     * Function to convert {@link UomConversionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UomConversion> createSpecification(UomConversionCriteria criteria) {
        Specification<UomConversion> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(UomConversion_.fromUom, JoinType.LEFT);
                root.fetch(UomConversion_.toUom, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), UomConversion_.id),
                    buildStringSpecification(criteria.getName(), UomConversion_.name),
                    buildRangeSpecification(criteria.getConversionFactor(), UomConversion_.conversionFactor),
                    buildRangeSpecification(criteria.getFromDate(), UomConversion_.fromDate),
                    buildRangeSpecification(criteria.getToDate(), UomConversion_.toDate),
                    buildSpecification(criteria.getFromUomId(), root -> root.join(UomConversion_.fromUom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getToUomId(), root -> root.join(UomConversion_.toUom, JoinType.LEFT).get(Uom_.id))
                )
            );
        }
        return specification;
    }
}
