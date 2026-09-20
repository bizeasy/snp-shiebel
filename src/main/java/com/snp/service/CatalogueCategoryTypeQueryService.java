package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CatalogueCategoryType;
import com.snp.repository.CatalogueCategoryTypeRepository;
import com.snp.service.criteria.CatalogueCategoryTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CatalogueCategoryType} entities in the database.
 * The main input is a {@link CatalogueCategoryTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CatalogueCategoryType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CatalogueCategoryTypeQueryService extends QueryService<CatalogueCategoryType> {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogueCategoryTypeQueryService.class);

    private final CatalogueCategoryTypeRepository catalogueCategoryTypeRepository;

    public CatalogueCategoryTypeQueryService(CatalogueCategoryTypeRepository catalogueCategoryTypeRepository) {
        this.catalogueCategoryTypeRepository = catalogueCategoryTypeRepository;
    }

    /**
     * Return a {@link List} of {@link CatalogueCategoryType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CatalogueCategoryType> findByCriteria(CatalogueCategoryTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<CatalogueCategoryType> specification = createSpecification(criteria);
        return catalogueCategoryTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CatalogueCategoryTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CatalogueCategoryType> specification = createSpecification(criteria);
        return catalogueCategoryTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CatalogueCategoryTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CatalogueCategoryType> createSpecification(CatalogueCategoryTypeCriteria criteria) {
        Specification<CatalogueCategoryType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CatalogueCategoryType_.id),
                    buildStringSpecification(criteria.getName(), CatalogueCategoryType_.name),
                    buildStringSpecification(criteria.getDescription(), CatalogueCategoryType_.description)
                )
            );
        }
        return specification;
    }
}
