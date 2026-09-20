package com.snp.service;

import com.snp.domain.ShoppingList;
import com.snp.repository.ShoppingListRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ShoppingList}.
 */
@Service
@Transactional
public class ShoppingListService {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingListService.class);

    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    /**
     * Save a shoppingList.
     *
     * @param shoppingList the entity to save.
     * @return the persisted entity.
     */
    public ShoppingList save(ShoppingList shoppingList) {
        LOG.debug("Request to save ShoppingList : {}", shoppingList);
        return shoppingListRepository.save(shoppingList);
    }

    /**
     * Update a shoppingList.
     *
     * @param shoppingList the entity to save.
     * @return the persisted entity.
     */
    public ShoppingList update(ShoppingList shoppingList) {
        LOG.debug("Request to update ShoppingList : {}", shoppingList);
        return shoppingListRepository.save(shoppingList);
    }

    /**
     * Partially update a shoppingList.
     *
     * @param shoppingList the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShoppingList> partialUpdate(ShoppingList shoppingList) {
        LOG.debug("Request to partially update ShoppingList : {}", shoppingList);

        return shoppingListRepository
            .findById(shoppingList.getId())
            .map(existingShoppingList -> {
                updateIfPresent(existingShoppingList::setName, shoppingList.getName());
                updateIfPresent(existingShoppingList::setIsActive, shoppingList.getIsActive());

                return existingShoppingList;
            })
            .map(shoppingListRepository::save);
    }

    /**
     * Get all the shoppingLists with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ShoppingList> findAllWithEagerRelationships(Pageable pageable) {
        return shoppingListRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one shoppingList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShoppingList> findOne(Long id) {
        LOG.debug("Request to get ShoppingList : {}", id);
        return shoppingListRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the shoppingList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShoppingList : {}", id);
        shoppingListRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
