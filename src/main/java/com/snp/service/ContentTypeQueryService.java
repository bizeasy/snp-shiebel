package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ContentType;
import com.snp.repository.ContentTypeRepository;
import com.snp.service.criteria.ContentTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ContentType} entities in the database.
 * The main input is a {@link ContentTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContentType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContentTypeQueryService extends QueryService<ContentType> {

    private static final Logger LOG = LoggerFactory.getLogger(ContentTypeQueryService.class);

    private final ContentTypeRepository contentTypeRepository;

    public ContentTypeQueryService(ContentTypeRepository contentTypeRepository) {
        this.contentTypeRepository = contentTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ContentType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContentType> findByCriteria(ContentTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ContentType> specification = createSpecification(criteria);
        return contentTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContentTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ContentType> specification = createSpecification(criteria);
        return contentTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ContentTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContentType> createSpecification(ContentTypeCriteria criteria) {
        Specification<ContentType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ContentType_.id),
                    buildStringSpecification(criteria.getName(), ContentType_.name)
                )
            );
        }
        return specification;
    }
}
