package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductPricePurpose;
import com.snp.repository.ProductPricePurposeRepository;
import com.snp.service.criteria.ProductPricePurposeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductPricePurpose} entities in the database.
 * The main input is a {@link ProductPricePurposeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductPricePurpose} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductPricePurposeQueryService extends QueryService<ProductPricePurpose> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPricePurposeQueryService.class);

    private final ProductPricePurposeRepository productPricePurposeRepository;

    public ProductPricePurposeQueryService(ProductPricePurposeRepository productPricePurposeRepository) {
        this.productPricePurposeRepository = productPricePurposeRepository;
    }

    /**
     * Return a {@link List} of {@link ProductPricePurpose} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductPricePurpose> findByCriteria(ProductPricePurposeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ProductPricePurpose> specification = createSpecification(criteria);
        return productPricePurposeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductPricePurposeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductPricePurpose> specification = createSpecification(criteria);
        return productPricePurposeRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductPricePurposeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductPricePurpose> createSpecification(ProductPricePurposeCriteria criteria) {
        Specification<ProductPricePurpose> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductPricePurpose_.id),
                    buildStringSpecification(criteria.getName(), ProductPricePurpose_.name),
                    buildStringSpecification(criteria.getDescription(), ProductPricePurpose_.description)
                )
            );
        }
        return specification;
    }
}
