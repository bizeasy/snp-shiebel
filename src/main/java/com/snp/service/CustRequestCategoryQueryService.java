package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CustRequestCategory;
import com.snp.repository.CustRequestCategoryRepository;
import com.snp.service.criteria.CustRequestCategoryCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CustRequestCategory} entities in the database.
 * The main input is a {@link CustRequestCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustRequestCategory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustRequestCategoryQueryService extends QueryService<CustRequestCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestCategoryQueryService.class);

    private final CustRequestCategoryRepository custRequestCategoryRepository;

    public CustRequestCategoryQueryService(CustRequestCategoryRepository custRequestCategoryRepository) {
        this.custRequestCategoryRepository = custRequestCategoryRepository;
    }

    /**
     * Return a {@link List} of {@link CustRequestCategory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustRequestCategory> findByCriteria(CustRequestCategoryCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<CustRequestCategory> specification = createSpecification(criteria);
        return custRequestCategoryRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustRequestCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustRequestCategory> specification = createSpecification(criteria);
        return custRequestCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link CustRequestCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustRequestCategory> createSpecification(CustRequestCategoryCriteria criteria) {
        Specification<CustRequestCategory> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CustRequestCategory_.custRequestType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CustRequestCategory_.id),
                    buildStringSpecification(criteria.getName(), CustRequestCategory_.name),
                    buildStringSpecification(criteria.getDescription(), CustRequestCategory_.description),
                    buildSpecification(criteria.getCustRequestTypeId(), root ->
                        root.join(CustRequestCategory_.custRequestType, JoinType.LEFT).get(CustRequestType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
