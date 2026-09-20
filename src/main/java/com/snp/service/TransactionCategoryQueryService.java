package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.TransactionCategory;
import com.snp.repository.TransactionCategoryRepository;
import com.snp.service.criteria.TransactionCategoryCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TransactionCategory} entities in the database.
 * The main input is a {@link TransactionCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionCategory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionCategoryQueryService extends QueryService<TransactionCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionCategoryQueryService.class);

    private final TransactionCategoryRepository transactionCategoryRepository;

    public TransactionCategoryQueryService(TransactionCategoryRepository transactionCategoryRepository) {
        this.transactionCategoryRepository = transactionCategoryRepository;
    }

    /**
     * Return a {@link List} of {@link TransactionCategory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionCategory> findByCriteria(TransactionCategoryCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<TransactionCategory> specification = createSpecification(criteria);
        return transactionCategoryRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TransactionCategory> specification = createSpecification(criteria);
        return transactionCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionCategory> createSpecification(TransactionCategoryCriteria criteria) {
        Specification<TransactionCategory> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), TransactionCategory_.id),
                    buildStringSpecification(criteria.getName(), TransactionCategory_.name),
                    buildStringSpecification(criteria.getDescription(), TransactionCategory_.description)
                )
            );
        }
        return specification;
    }
}
