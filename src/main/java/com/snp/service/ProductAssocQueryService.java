package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductAssoc;
import com.snp.repository.ProductAssocRepository;
import com.snp.service.criteria.ProductAssocCriteria;
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
 * Service for executing complex queries for {@link ProductAssoc} entities in the database.
 * The main input is a {@link ProductAssocCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductAssoc} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductAssocQueryService extends QueryService<ProductAssoc> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAssocQueryService.class);

    private final ProductAssocRepository productAssocRepository;

    public ProductAssocQueryService(ProductAssocRepository productAssocRepository) {
        this.productAssocRepository = productAssocRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductAssoc} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductAssoc> findByCriteria(ProductAssocCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductAssoc> specification = createSpecification(criteria);
        return productAssocRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductAssocCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductAssoc> specification = createSpecification(criteria);
        return productAssocRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductAssocCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductAssoc> createSpecification(ProductAssocCriteria criteria) {
        Specification<ProductAssoc> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductAssoc_.product, JoinType.LEFT);
                root.fetch(ProductAssoc_.productTo, JoinType.LEFT);
                root.fetch(ProductAssoc_.productAssocType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductAssoc_.id),
                    buildRangeSpecification(criteria.getFromDate(), ProductAssoc_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), ProductAssoc_.thruDate),
                    buildRangeSpecification(criteria.getSequenceNo(), ProductAssoc_.sequenceNo),
                    buildRangeSpecification(criteria.getQuantity(), ProductAssoc_.quantity),
                    buildSpecification(criteria.getProductId(), root -> root.join(ProductAssoc_.product, JoinType.LEFT).get(Product_.id)),
                    buildSpecification(criteria.getProductToId(), root ->
                        root.join(ProductAssoc_.productTo, JoinType.LEFT).get(Product_.id)
                    ),
                    buildSpecification(criteria.getProductAssocTypeId(), root ->
                        root.join(ProductAssoc_.productAssocType, JoinType.LEFT).get(ProductAssocType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
