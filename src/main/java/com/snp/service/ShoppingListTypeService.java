package com.snp.service;

import com.snp.domain.ShoppingListType;
import com.snp.repository.ShoppingListTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ShoppingListType}.
 */
@Service
@Transactional
public class ShoppingListTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingListTypeService.class);

    private final ShoppingListTypeRepository shoppingListTypeRepository;

    public ShoppingListTypeService(ShoppingListTypeRepository shoppingListTypeRepository) {
        this.shoppingListTypeRepository = shoppingListTypeRepository;
    }

    /**
     * Save a shoppingListType.
     *
     * @param shoppingListType the entity to save.
     * @return the persisted entity.
     */
    public ShoppingListType save(ShoppingListType shoppingListType) {
        LOG.debug("Request to save ShoppingListType : {}", shoppingListType);
        return shoppingListTypeRepository.save(shoppingListType);
    }

    /**
     * Update a shoppingListType.
     *
     * @param shoppingListType the entity to save.
     * @return the persisted entity.
     */
    public ShoppingListType update(ShoppingListType shoppingListType) {
        LOG.debug("Request to update ShoppingListType : {}", shoppingListType);
        return shoppingListTypeRepository.save(shoppingListType);
    }

    /**
     * Partially update a shoppingListType.
     *
     * @param shoppingListType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShoppingListType> partialUpdate(ShoppingListType shoppingListType) {
        LOG.debug("Request to partially update ShoppingListType : {}", shoppingListType);

        return shoppingListTypeRepository
            .findById(shoppingListType.getId())
            .map(existingShoppingListType -> {
                updateIfPresent(existingShoppingListType::setName, shoppingListType.getName());
                updateIfPresent(existingShoppingListType::setDescription, shoppingListType.getDescription());

                return existingShoppingListType;
            })
            .map(shoppingListTypeRepository::save);
    }

    /**
     * Get one shoppingListType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShoppingListType> findOne(Long id) {
        LOG.debug("Request to get ShoppingListType : {}", id);
        return shoppingListTypeRepository.findById(id);
    }

    /**
     * Delete the shoppingListType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShoppingListType : {}", id);
        shoppingListTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
