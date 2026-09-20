package com.snp.service;

import com.snp.domain.ShoppingListItem;
import com.snp.repository.ShoppingListItemRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ShoppingListItem}.
 */
@Service
@Transactional
public class ShoppingListItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingListItemService.class);

    private final ShoppingListItemRepository shoppingListItemRepository;

    public ShoppingListItemService(ShoppingListItemRepository shoppingListItemRepository) {
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    /**
     * Save a shoppingListItem.
     *
     * @param shoppingListItem the entity to save.
     * @return the persisted entity.
     */
    public ShoppingListItem save(ShoppingListItem shoppingListItem) {
        LOG.debug("Request to save ShoppingListItem : {}", shoppingListItem);
        return shoppingListItemRepository.save(shoppingListItem);
    }

    /**
     * Update a shoppingListItem.
     *
     * @param shoppingListItem the entity to save.
     * @return the persisted entity.
     */
    public ShoppingListItem update(ShoppingListItem shoppingListItem) {
        LOG.debug("Request to update ShoppingListItem : {}", shoppingListItem);
        return shoppingListItemRepository.save(shoppingListItem);
    }

    /**
     * Partially update a shoppingListItem.
     *
     * @param shoppingListItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShoppingListItem> partialUpdate(ShoppingListItem shoppingListItem) {
        LOG.debug("Request to partially update ShoppingListItem : {}", shoppingListItem);

        return shoppingListItemRepository
            .findById(shoppingListItem.getId())
            .map(existingShoppingListItem -> {
                updateIfPresent(existingShoppingListItem::setSequenceNo, shoppingListItem.getSequenceNo());
                updateIfPresent(existingShoppingListItem::setQuantity, shoppingListItem.getQuantity());
                updateIfPresent(existingShoppingListItem::setRequiredByDate, shoppingListItem.getRequiredByDate());
                updateIfPresent(existingShoppingListItem::setSelectedAmount, shoppingListItem.getSelectedAmount());
                updateIfPresent(existingShoppingListItem::setMaxAmount, shoppingListItem.getMaxAmount());
                updateIfPresent(existingShoppingListItem::setDescription, shoppingListItem.getDescription());
                updateIfPresent(existingShoppingListItem::setStory, shoppingListItem.getStory());

                return existingShoppingListItem;
            })
            .map(shoppingListItemRepository::save);
    }

    /**
     * Get all the shoppingListItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ShoppingListItem> findAllWithEagerRelationships(Pageable pageable) {
        return shoppingListItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one shoppingListItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShoppingListItem> findOne(Long id) {
        LOG.debug("Request to get ShoppingListItem : {}", id);
        return shoppingListItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the shoppingListItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShoppingListItem : {}", id);
        shoppingListItemRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
