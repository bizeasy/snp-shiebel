package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductCategory;
import com.snp.repository.ProductCategoryRepository;
import com.snp.service.criteria.ProductCategoryCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductCategory} entities in the database.
 * The main input is a {@link ProductCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductCategory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductCategoryQueryService extends QueryService<ProductCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCategoryQueryService.class);

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryQueryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    /**
     * Return a {@link List} of {@link ProductCategory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductCategory> findByCriteria(ProductCategoryCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ProductCategory> specification = createSpecification(criteria);
        return productCategoryRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductCategory> specification = createSpecification(criteria);
        return productCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductCategory> createSpecification(ProductCategoryCriteria criteria) {
        Specification<ProductCategory> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductCategory_.productCategoryType, JoinType.LEFT);
                root.fetch(ProductCategory_.parent, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductCategory_.id),
                    buildStringSpecification(criteria.getName(), ProductCategory_.name),
                    buildStringSpecification(criteria.getDescription(), ProductCategory_.description),
                    buildStringSpecification(criteria.getLongDescription(), ProductCategory_.longDescription),
                    buildStringSpecification(criteria.getAttribute(), ProductCategory_.attribute),
                    buildRangeSpecification(criteria.getSequenceNo(), ProductCategory_.sequenceNo),
                    buildStringSpecification(criteria.getImageUrl(), ProductCategory_.imageUrl),
                    buildStringSpecification(criteria.getAltImageUrl(), ProductCategory_.altImageUrl),
                    buildStringSpecification(criteria.getBannerUrl(), ProductCategory_.bannerUrl),
                    buildStringSpecification(criteria.getLargeImageUrl(), ProductCategory_.largeImageUrl),
                    buildStringSpecification(criteria.getAltLargeImageUrl(), ProductCategory_.altLargeImageUrl),
                    buildSpecification(criteria.getIsBanner(), ProductCategory_.isBanner),
                    buildSpecification(criteria.getProductCategoryTypeId(), root ->
                        root.join(ProductCategory_.productCategoryType, JoinType.LEFT).get(ProductCategoryType_.id)
                    ),
                    buildSpecification(criteria.getParentId(), root ->
                        root.join(ProductCategory_.parent, JoinType.LEFT).get(ProductCategory_.id)
                    )
                )
            );
        }
        return specification;
    }
}
