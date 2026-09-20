package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CatalogueCategory;
import com.snp.repository.CatalogueCategoryRepository;
import com.snp.service.criteria.CatalogueCategoryCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CatalogueCategory} entities in the database.
 * The main input is a {@link CatalogueCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CatalogueCategory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CatalogueCategoryQueryService extends QueryService<CatalogueCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogueCategoryQueryService.class);

    private final CatalogueCategoryRepository catalogueCategoryRepository;

    public CatalogueCategoryQueryService(CatalogueCategoryRepository catalogueCategoryRepository) {
        this.catalogueCategoryRepository = catalogueCategoryRepository;
    }

    /**
     * Return a {@link List} of {@link CatalogueCategory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CatalogueCategory> findByCriteria(CatalogueCategoryCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<CatalogueCategory> specification = createSpecification(criteria);
        return catalogueCategoryRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CatalogueCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CatalogueCategory> specification = createSpecification(criteria);
        return catalogueCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link CatalogueCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CatalogueCategory> createSpecification(CatalogueCategoryCriteria criteria) {
        Specification<CatalogueCategory> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CatalogueCategory_.catalogue, JoinType.LEFT);
                root.fetch(CatalogueCategory_.productCategory, JoinType.LEFT);
                root.fetch(CatalogueCategory_.catalogueCategoryType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CatalogueCategory_.id),
                    buildRangeSpecification(criteria.getSequenceNo(), CatalogueCategory_.sequenceNo),
                    buildSpecification(criteria.getCatalogueId(), root ->
                        root.join(CatalogueCategory_.catalogue, JoinType.LEFT).get(Catalogue_.id)
                    ),
                    buildSpecification(criteria.getProductCategoryId(), root ->
                        root.join(CatalogueCategory_.productCategory, JoinType.LEFT).get(ProductCategory_.id)
                    ),
                    buildSpecification(criteria.getCatalogueCategoryTypeId(), root ->
                        root.join(CatalogueCategory_.catalogueCategoryType, JoinType.LEFT).get(CatalogueCategoryType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
