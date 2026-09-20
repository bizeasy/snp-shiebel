package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductStore;
import com.snp.repository.ProductStoreRepository;
import com.snp.service.criteria.ProductStoreCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductStore} entities in the database.
 * The main input is a {@link ProductStoreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductStore} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductStoreQueryService extends QueryService<ProductStore> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductStoreQueryService.class);

    private final ProductStoreRepository productStoreRepository;

    public ProductStoreQueryService(ProductStoreRepository productStoreRepository) {
        this.productStoreRepository = productStoreRepository;
    }

    /**
     * Return a {@link List} of {@link ProductStore} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductStore> findByCriteria(ProductStoreCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ProductStore> specification = createSpecification(criteria);
        return productStoreRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductStoreCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductStore> specification = createSpecification(criteria);
        return productStoreRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductStoreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductStore> createSpecification(ProductStoreCriteria criteria) {
        Specification<ProductStore> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductStore_.owner, JoinType.LEFT);
                root.fetch(ProductStore_.admin, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductStore_.id),
                    buildStringSpecification(criteria.getName(), ProductStore_.name),
                    buildStringSpecification(criteria.getTitle(), ProductStore_.title),
                    buildSpecification(criteria.getOwnerId(), root -> root.join(ProductStore_.owner, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getAdminId(), root -> root.join(ProductStore_.admin, JoinType.LEFT).get(Party_.id))
                )
            );
        }
        return specification;
    }
}
