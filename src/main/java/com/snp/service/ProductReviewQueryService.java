package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductReview;
import com.snp.repository.ProductReviewRepository;
import com.snp.service.criteria.ProductReviewCriteria;
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
 * Service for executing complex queries for {@link ProductReview} entities in the database.
 * The main input is a {@link ProductReviewCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductReview} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductReviewQueryService extends QueryService<ProductReview> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductReviewQueryService.class);

    private final ProductReviewRepository productReviewRepository;

    public ProductReviewQueryService(ProductReviewRepository productReviewRepository) {
        this.productReviewRepository = productReviewRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductReview} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductReview> findByCriteria(ProductReviewCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductReview> specification = createSpecification(criteria);
        return productReviewRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductReviewCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductReview> specification = createSpecification(criteria);
        return productReviewRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductReviewCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductReview> createSpecification(ProductReviewCriteria criteria) {
        Specification<ProductReview> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductReview_.product, JoinType.LEFT);
                root.fetch(ProductReview_.party, JoinType.LEFT);
                root.fetch(ProductReview_.orderItem, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductReview_.id),
                    buildRangeSpecification(criteria.getRating(), ProductReview_.rating),
                    buildStringSpecification(criteria.getMessage(), ProductReview_.message),
                    buildRangeSpecification(criteria.getReviewDate(), ProductReview_.reviewDate),
                    buildSpecification(criteria.getProductId(), root -> root.join(ProductReview_.product, JoinType.LEFT).get(Product_.id)),
                    buildSpecification(criteria.getPartyId(), root -> root.join(ProductReview_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(ProductReview_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    )
                )
            );
        }
        return specification;
    }
}
