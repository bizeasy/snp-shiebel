package com.snp.service;

import com.snp.domain.InventoryItemDelegate;
import com.snp.repository.InventoryItemDelegateRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.InventoryItemDelegate}.
 */
@Service
@Transactional
public class InventoryItemDelegateService {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemDelegateService.class);

    private final InventoryItemDelegateRepository inventoryItemDelegateRepository;

    public InventoryItemDelegateService(InventoryItemDelegateRepository inventoryItemDelegateRepository) {
        this.inventoryItemDelegateRepository = inventoryItemDelegateRepository;
    }

    /**
     * Save a inventoryItemDelegate.
     *
     * @param inventoryItemDelegate the entity to save.
     * @return the persisted entity.
     */
    public InventoryItemDelegate save(InventoryItemDelegate inventoryItemDelegate) {
        LOG.debug("Request to save InventoryItemDelegate : {}", inventoryItemDelegate);
        return inventoryItemDelegateRepository.save(inventoryItemDelegate);
    }

    /**
     * Update a inventoryItemDelegate.
     *
     * @param inventoryItemDelegate the entity to save.
     * @return the persisted entity.
     */
    public InventoryItemDelegate update(InventoryItemDelegate inventoryItemDelegate) {
        LOG.debug("Request to update InventoryItemDelegate : {}", inventoryItemDelegate);
        return inventoryItemDelegateRepository.save(inventoryItemDelegate);
    }

    /**
     * Partially update a inventoryItemDelegate.
     *
     * @param inventoryItemDelegate the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InventoryItemDelegate> partialUpdate(InventoryItemDelegate inventoryItemDelegate) {
        LOG.debug("Request to partially update InventoryItemDelegate : {}", inventoryItemDelegate);

        return inventoryItemDelegateRepository
            .findById(inventoryItemDelegate.getId())
            .map(existingInventoryItemDelegate -> {
                updateIfPresent(existingInventoryItemDelegate::setSequenceNo, inventoryItemDelegate.getSequenceNo());
                updateIfPresent(existingInventoryItemDelegate::setEffectiveDate, inventoryItemDelegate.getEffectiveDate());
                updateIfPresent(existingInventoryItemDelegate::setQuantityOnHandDiff, inventoryItemDelegate.getQuantityOnHandDiff());
                updateIfPresent(
                    existingInventoryItemDelegate::setAvailableToPromiseDiff,
                    inventoryItemDelegate.getAvailableToPromiseDiff()
                );
                updateIfPresent(
                    existingInventoryItemDelegate::setAccountingQuantityDiff,
                    inventoryItemDelegate.getAccountingQuantityDiff()
                );
                updateIfPresent(existingInventoryItemDelegate::setUnitCost, inventoryItemDelegate.getUnitCost());
                updateIfPresent(existingInventoryItemDelegate::setRemarks, inventoryItemDelegate.getRemarks());

                return existingInventoryItemDelegate;
            })
            .map(inventoryItemDelegateRepository::save);
    }

    /**
     * Get one inventoryItemDelegate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InventoryItemDelegate> findOne(Long id) {
        LOG.debug("Request to get InventoryItemDelegate : {}", id);
        return inventoryItemDelegateRepository.findById(id);
    }

    /**
     * Delete the inventoryItemDelegate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InventoryItemDelegate : {}", id);
        inventoryItemDelegateRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
