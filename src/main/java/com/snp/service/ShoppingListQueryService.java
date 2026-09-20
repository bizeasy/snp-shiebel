package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ShoppingList;
import com.snp.repository.ShoppingListRepository;
import com.snp.service.criteria.ShoppingListCriteria;
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
 * Service for executing complex queries for {@link ShoppingList} entities in the database.
 * The main input is a {@link ShoppingListCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShoppingList} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShoppingListQueryService extends QueryService<ShoppingList> {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingListQueryService.class);

    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListQueryService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    /**
     * Return a {@link Page} of {@link ShoppingList} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShoppingList> findByCriteria(ShoppingListCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShoppingList> specification = createSpecification(criteria);
        return shoppingListRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShoppingListCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShoppingList> specification = createSpecification(criteria);
        return shoppingListRepository.count(specification);
    }

    /**
     * Function to convert {@link ShoppingListCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShoppingList> createSpecification(ShoppingListCriteria criteria) {
        Specification<ShoppingList> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ShoppingList_.shoppingListType, JoinType.LEFT);
                root.fetch(ShoppingList_.party, JoinType.LEFT);
                root.fetch(ShoppingList_.status, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ShoppingList_.id),
                    buildStringSpecification(criteria.getName(), ShoppingList_.name),
                    buildSpecification(criteria.getIsActive(), ShoppingList_.isActive),
                    buildSpecification(criteria.getShoppingListTypeId(), root ->
                        root.join(ShoppingList_.shoppingListType, JoinType.LEFT).get(ShoppingListType_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root -> root.join(ShoppingList_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getStatusId(), root -> root.join(ShoppingList_.status, JoinType.LEFT).get(Status_.id))
                )
            );
        }
        return specification;
    }
}
