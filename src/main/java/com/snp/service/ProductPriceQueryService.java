package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductPrice;
import com.snp.repository.ProductPriceRepository;
import com.snp.service.criteria.ProductPriceCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductPrice} entities in the database.
 * The main input is a {@link ProductPriceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductPrice} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductPriceQueryService extends QueryService<ProductPrice> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPriceQueryService.class);

    private final ProductPriceRepository productPriceRepository;

    public ProductPriceQueryService(ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductPrice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductPrice> findByCriteria(ProductPriceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductPrice> specification = createSpecification(criteria);
        return productPriceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductPriceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductPrice> specification = createSpecification(criteria);
        return productPriceRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductPriceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductPrice> createSpecification(ProductPriceCriteria criteria) {
        Specification<ProductPrice> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductPrice_.product, JoinType.LEFT);
                root.fetch(ProductPrice_.productPriceType, JoinType.LEFT);
                root.fetch(ProductPrice_.productPricePurpose, JoinType.LEFT);
                root.fetch(ProductPrice_.currencyUom, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductPrice_.id),
                    buildRangeSpecification(criteria.getFromDate(), ProductPrice_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), ProductPrice_.thruDate),
                    buildRangeSpecification(criteria.getPrice(), ProductPrice_.price),
                    buildRangeSpecification(criteria.getCgst(), ProductPrice_.cgst),
                    buildRangeSpecification(criteria.getIgst(), ProductPrice_.igst),
                    buildRangeSpecification(criteria.getSgst(), ProductPrice_.sgst),
                    buildRangeSpecification(criteria.getTotalPrice(), ProductPrice_.totalPrice),
                    buildSpecification(criteria.getProductId(), root -> root.join(ProductPrice_.product, JoinType.LEFT).get(Product_.id)),
                    buildSpecification(criteria.getProductPriceTypeId(), root ->
                        root.join(ProductPrice_.productPriceType, JoinType.LEFT).get(ProductPriceType_.id)
                    ),
                    buildSpecification(criteria.getProductPricePurposeId(), root ->
                        root.join(ProductPrice_.productPricePurpose, JoinType.LEFT).get(ProductPricePurpose_.id)
                    ),
                    buildSpecification(criteria.getCurrencyUomId(), root ->
                        root.join(ProductPrice_.currencyUom, JoinType.LEFT).get(Uom_.id)
                    )
                )
            );
        }
        return specification;
    }
}
