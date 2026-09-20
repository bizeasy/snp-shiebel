package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductAssocType;
import com.snp.repository.ProductAssocTypeRepository;
import com.snp.service.criteria.ProductAssocTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductAssocType} entities in the database.
 * The main input is a {@link ProductAssocTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductAssocType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductAssocTypeQueryService extends QueryService<ProductAssocType> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAssocTypeQueryService.class);

    private final ProductAssocTypeRepository productAssocTypeRepository;

    public ProductAssocTypeQueryService(ProductAssocTypeRepository productAssocTypeRepository) {
        this.productAssocTypeRepository = productAssocTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ProductAssocType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductAssocType> findByCriteria(ProductAssocTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ProductAssocType> specification = createSpecification(criteria);
        return productAssocTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductAssocTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductAssocType> specification = createSpecification(criteria);
        return productAssocTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductAssocTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductAssocType> createSpecification(ProductAssocTypeCriteria criteria) {
        Specification<ProductAssocType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductAssocType_.id),
                    buildStringSpecification(criteria.getName(), ProductAssocType_.name),
                    buildStringSpecification(criteria.getDescription(), ProductAssocType_.description)
                )
            );
        }
        return specification;
    }
}
