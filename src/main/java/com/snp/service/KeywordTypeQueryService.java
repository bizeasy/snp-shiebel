package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.KeywordType;
import com.snp.repository.KeywordTypeRepository;
import com.snp.service.criteria.KeywordTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link KeywordType} entities in the database.
 * The main input is a {@link KeywordTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link KeywordType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class KeywordTypeQueryService extends QueryService<KeywordType> {

    private static final Logger LOG = LoggerFactory.getLogger(KeywordTypeQueryService.class);

    private final KeywordTypeRepository keywordTypeRepository;

    public KeywordTypeQueryService(KeywordTypeRepository keywordTypeRepository) {
        this.keywordTypeRepository = keywordTypeRepository;
    }

    /**
     * Return a {@link List} of {@link KeywordType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<KeywordType> findByCriteria(KeywordTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<KeywordType> specification = createSpecification(criteria);
        return keywordTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(KeywordTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<KeywordType> specification = createSpecification(criteria);
        return keywordTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link KeywordTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<KeywordType> createSpecification(KeywordTypeCriteria criteria) {
        Specification<KeywordType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), KeywordType_.id),
                    buildStringSpecification(criteria.getName(), KeywordType_.name)
                )
            );
        }
        return specification;
    }
}
