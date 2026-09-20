package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductCategoryType;
import com.snp.repository.ProductCategoryTypeRepository;
import com.snp.service.criteria.ProductCategoryTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductCategoryType} entities in the database.
 * The main input is a {@link ProductCategoryTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductCategoryType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductCategoryTypeQueryService extends QueryService<ProductCategoryType> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCategoryTypeQueryService.class);

    private final ProductCategoryTypeRepository productCategoryTypeRepository;

    public ProductCategoryTypeQueryService(ProductCategoryTypeRepository productCategoryTypeRepository) {
        this.productCategoryTypeRepository = productCategoryTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ProductCategoryType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductCategoryType> findByCriteria(ProductCategoryTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ProductCategoryType> specification = createSpecification(criteria);
        return productCategoryTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCategoryTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductCategoryType> specification = createSpecification(criteria);
        return productCategoryTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCategoryTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductCategoryType> createSpecification(ProductCategoryTypeCriteria criteria) {
        Specification<ProductCategoryType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductCategoryType_.id),
                    buildStringSpecification(criteria.getName(), ProductCategoryType_.name),
                    buildStringSpecification(criteria.getDescription(), ProductCategoryType_.description)
                )
            );
        }
        return specification;
    }
}
