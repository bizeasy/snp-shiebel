package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductCategoryMember;
import com.snp.repository.ProductCategoryMemberRepository;
import com.snp.service.criteria.ProductCategoryMemberCriteria;
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
 * Service for executing complex queries for {@link ProductCategoryMember} entities in the database.
 * The main input is a {@link ProductCategoryMemberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductCategoryMember} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductCategoryMemberQueryService extends QueryService<ProductCategoryMember> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCategoryMemberQueryService.class);

    private final ProductCategoryMemberRepository productCategoryMemberRepository;

    public ProductCategoryMemberQueryService(ProductCategoryMemberRepository productCategoryMemberRepository) {
        this.productCategoryMemberRepository = productCategoryMemberRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductCategoryMember} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductCategoryMember> findByCriteria(ProductCategoryMemberCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductCategoryMember> specification = createSpecification(criteria);
        return productCategoryMemberRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCategoryMemberCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductCategoryMember> specification = createSpecification(criteria);
        return productCategoryMemberRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCategoryMemberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductCategoryMember> createSpecification(ProductCategoryMemberCriteria criteria) {
        Specification<ProductCategoryMember> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductCategoryMember_.product, JoinType.LEFT);
                root.fetch(ProductCategoryMember_.productCategory, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductCategoryMember_.id),
                    buildRangeSpecification(criteria.getFromDate(), ProductCategoryMember_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), ProductCategoryMember_.thruDate),
                    buildRangeSpecification(criteria.getSequenceNo(), ProductCategoryMember_.sequenceNo),
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(ProductCategoryMember_.product, JoinType.LEFT).get(Product_.id)
                    ),
                    buildSpecification(criteria.getProductCategoryId(), root ->
                        root.join(ProductCategoryMember_.productCategory, JoinType.LEFT).get(ProductCategory_.id)
                    )
                )
            );
        }
        return specification;
    }
}
