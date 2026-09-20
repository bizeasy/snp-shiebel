package com.snp.service;

import com.snp.domain.InventoryItemType;
import com.snp.repository.InventoryItemTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.InventoryItemType}.
 */
@Service
@Transactional
public class InventoryItemTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemTypeService.class);

    private final InventoryItemTypeRepository inventoryItemTypeRepository;

    public InventoryItemTypeService(InventoryItemTypeRepository inventoryItemTypeRepository) {
        this.inventoryItemTypeRepository = inventoryItemTypeRepository;
    }

    /**
     * Save a inventoryItemType.
     *
     * @param inventoryItemType the entity to save.
     * @return the persisted entity.
     */
    public InventoryItemType save(InventoryItemType inventoryItemType) {
        LOG.debug("Request to save InventoryItemType : {}", inventoryItemType);
        return inventoryItemTypeRepository.save(inventoryItemType);
    }

    /**
     * Update a inventoryItemType.
     *
     * @param inventoryItemType the entity to save.
     * @return the persisted entity.
     */
    public InventoryItemType update(InventoryItemType inventoryItemType) {
        LOG.debug("Request to update InventoryItemType : {}", inventoryItemType);
        return inventoryItemTypeRepository.save(inventoryItemType);
    }

    /**
     * Partially update a inventoryItemType.
     *
     * @param inventoryItemType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InventoryItemType> partialUpdate(InventoryItemType inventoryItemType) {
        LOG.debug("Request to partially update InventoryItemType : {}", inventoryItemType);

        return inventoryItemTypeRepository
            .findById(inventoryItemType.getId())
            .map(existingInventoryItemType -> {
                updateIfPresent(existingInventoryItemType::setName, inventoryItemType.getName());
                updateIfPresent(existingInventoryItemType::setDescription, inventoryItemType.getDescription());

                return existingInventoryItemType;
            })
            .map(inventoryItemTypeRepository::save);
    }

    /**
     * Get one inventoryItemType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InventoryItemType> findOne(Long id) {
        LOG.debug("Request to get InventoryItemType : {}", id);
        return inventoryItemTypeRepository.findById(id);
    }

    /**
     * Delete the inventoryItemType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InventoryItemType : {}", id);
        inventoryItemTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
