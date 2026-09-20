package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ShoppingListItem;
import com.snp.repository.ShoppingListItemRepository;
import com.snp.service.criteria.ShoppingListItemCriteria;
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
 * Service for executing complex queries for {@link ShoppingListItem} entities in the database.
 * The main input is a {@link ShoppingListItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShoppingListItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShoppingListItemQueryService extends QueryService<ShoppingListItem> {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingListItemQueryService.class);

    private final ShoppingListItemRepository shoppingListItemRepository;

    public ShoppingListItemQueryService(ShoppingListItemRepository shoppingListItemRepository) {
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    /**
     * Return a {@link Page} of {@link ShoppingListItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShoppingListItem> findByCriteria(ShoppingListItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShoppingListItem> specification = createSpecification(criteria);
        return shoppingListItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShoppingListItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShoppingListItem> specification = createSpecification(criteria);
        return shoppingListItemRepository.count(specification);
    }

    /**
     * Function to convert {@link ShoppingListItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShoppingListItem> createSpecification(ShoppingListItemCriteria criteria) {
        Specification<ShoppingListItem> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ShoppingListItem_.shoppingList, JoinType.LEFT);
                root.fetch(ShoppingListItem_.product, JoinType.LEFT);
                root.fetch(ShoppingListItem_.status, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ShoppingListItem_.id),
                    buildRangeSpecification(criteria.getSequenceNo(), ShoppingListItem_.sequenceNo),
                    buildRangeSpecification(criteria.getQuantity(), ShoppingListItem_.quantity),
                    buildRangeSpecification(criteria.getRequiredByDate(), ShoppingListItem_.requiredByDate),
                    buildRangeSpecification(criteria.getSelectedAmount(), ShoppingListItem_.selectedAmount),
                    buildRangeSpecification(criteria.getMaxAmount(), ShoppingListItem_.maxAmount),
                    buildStringSpecification(criteria.getDescription(), ShoppingListItem_.description),
                    buildStringSpecification(criteria.getStory(), ShoppingListItem_.story),
                    buildSpecification(criteria.getShoppingListId(), root ->
                        root.join(ShoppingListItem_.shoppingList, JoinType.LEFT).get(ShoppingList_.id)
                    ),
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(ShoppingListItem_.product, JoinType.LEFT).get(Product_.id)
                    ),
                    buildSpecification(criteria.getStatusId(), root -> root.join(ShoppingListItem_.status, JoinType.LEFT).get(Status_.id))
                )
            );
        }
        return specification;
    }
}
