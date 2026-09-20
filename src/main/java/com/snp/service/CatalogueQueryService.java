package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Catalogue;
import com.snp.repository.CatalogueRepository;
import com.snp.service.criteria.CatalogueCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Catalogue} entities in the database.
 * The main input is a {@link CatalogueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Catalogue} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CatalogueQueryService extends QueryService<Catalogue> {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogueQueryService.class);

    private final CatalogueRepository catalogueRepository;

    public CatalogueQueryService(CatalogueRepository catalogueRepository) {
        this.catalogueRepository = catalogueRepository;
    }

    /**
     * Return a {@link List} of {@link Catalogue} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Catalogue> findByCriteria(CatalogueCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Catalogue> specification = createSpecification(criteria);
        return catalogueRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CatalogueCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Catalogue> specification = createSpecification(criteria);
        return catalogueRepository.count(specification);
    }

    /**
     * Function to convert {@link CatalogueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Catalogue> createSpecification(CatalogueCriteria criteria) {
        Specification<Catalogue> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Catalogue_.id),
                    buildStringSpecification(criteria.getName(), Catalogue_.name),
                    buildStringSpecification(criteria.getDescription(), Catalogue_.description),
                    buildRangeSpecification(criteria.getSequenceNo(), Catalogue_.sequenceNo),
                    buildStringSpecification(criteria.getImagePath(), Catalogue_.imagePath),
                    buildStringSpecification(criteria.getMobileImagePath(), Catalogue_.mobileImagePath),
                    buildStringSpecification(criteria.getAltImage1(), Catalogue_.altImage1),
                    buildStringSpecification(criteria.getAltImage2(), Catalogue_.altImage2),
                    buildStringSpecification(criteria.getAltImage3(), Catalogue_.altImage3),
                    buildStringSpecification(criteria.getBannerUrl(), Catalogue_.bannerUrl),
                    buildSpecification(criteria.getIsBanner(), Catalogue_.isBanner)
                )
            );
        }
        return specification;
    }
}
