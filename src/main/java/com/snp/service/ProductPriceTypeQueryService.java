package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductPriceType;
import com.snp.repository.ProductPriceTypeRepository;
import com.snp.service.criteria.ProductPriceTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductPriceType} entities in the database.
 * The main input is a {@link ProductPriceTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductPriceType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductPriceTypeQueryService extends QueryService<ProductPriceType> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPriceTypeQueryService.class);

    private final ProductPriceTypeRepository productPriceTypeRepository;

    public ProductPriceTypeQueryService(ProductPriceTypeRepository productPriceTypeRepository) {
        this.productPriceTypeRepository = productPriceTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ProductPriceType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductPriceType> findByCriteria(ProductPriceTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ProductPriceType> specification = createSpecification(criteria);
        return productPriceTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductPriceTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductPriceType> specification = createSpecification(criteria);
        return productPriceTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductPriceTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductPriceType> createSpecification(ProductPriceTypeCriteria criteria) {
        Specification<ProductPriceType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductPriceType_.id),
                    buildStringSpecification(criteria.getName(), ProductPriceType_.name),
                    buildStringSpecification(criteria.getDescription(), ProductPriceType_.description)
                )
            );
        }
        return specification;
    }
}
