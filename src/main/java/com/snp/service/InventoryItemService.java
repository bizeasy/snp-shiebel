package com.snp.service;

import com.snp.domain.InventoryItem;
import com.snp.repository.InventoryItemRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.InventoryItem}.
 */
@Service
@Transactional
public class InventoryItemService {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemService.class);

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    /**
     * Save a inventoryItem.
     *
     * @param inventoryItem the entity to save.
     * @return the persisted entity.
     */
    public InventoryItem save(InventoryItem inventoryItem) {
        LOG.debug("Request to save InventoryItem : {}", inventoryItem);
        return inventoryItemRepository.save(inventoryItem);
    }

    /**
     * Update a inventoryItem.
     *
     * @param inventoryItem the entity to save.
     * @return the persisted entity.
     */
    public InventoryItem update(InventoryItem inventoryItem) {
        LOG.debug("Request to update InventoryItem : {}", inventoryItem);
        return inventoryItemRepository.save(inventoryItem);
    }

    /**
     * Partially update a inventoryItem.
     *
     * @param inventoryItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InventoryItem> partialUpdate(InventoryItem inventoryItem) {
        LOG.debug("Request to partially update InventoryItem : {}", inventoryItem);

        return inventoryItemRepository
            .findById(inventoryItem.getId())
            .map(existingInventoryItem -> {
                updateIfPresent(existingInventoryItem::setReceivedDate, inventoryItem.getReceivedDate());
                updateIfPresent(existingInventoryItem::setManufacturedDate, inventoryItem.getManufacturedDate());
                updateIfPresent(existingInventoryItem::setExpiryDate, inventoryItem.getExpiryDate());
                updateIfPresent(existingInventoryItem::setRetestDate, inventoryItem.getRetestDate());
                updateIfPresent(existingInventoryItem::setContainerId, inventoryItem.getContainerId());
                updateIfPresent(existingInventoryItem::setBatchNo, inventoryItem.getBatchNo());
                updateIfPresent(existingInventoryItem::setMfgBatchNo, inventoryItem.getMfgBatchNo());
                updateIfPresent(existingInventoryItem::setLotNoStr, inventoryItem.getLotNoStr());
                updateIfPresent(existingInventoryItem::setBinNumber, inventoryItem.getBinNumber());
                updateIfPresent(existingInventoryItem::setComments, inventoryItem.getComments());
                updateIfPresent(existingInventoryItem::setQuantityOnHandTotal, inventoryItem.getQuantityOnHandTotal());
                updateIfPresent(existingInventoryItem::setAvailableToPromiseTotal, inventoryItem.getAvailableToPromiseTotal());
                updateIfPresent(existingInventoryItem::setAccountingQuantityTotal, inventoryItem.getAccountingQuantityTotal());
                updateIfPresent(existingInventoryItem::setOldQuantityOnHand, inventoryItem.getOldQuantityOnHand());
                updateIfPresent(existingInventoryItem::setOldAvailableToPromise, inventoryItem.getOldAvailableToPromise());
                updateIfPresent(existingInventoryItem::setSerialNumber, inventoryItem.getSerialNumber());
                updateIfPresent(existingInventoryItem::setSoftIdentifier, inventoryItem.getSoftIdentifier());
                updateIfPresent(existingInventoryItem::setActivationNumber, inventoryItem.getActivationNumber());
                updateIfPresent(existingInventoryItem::setActivationValidThru, inventoryItem.getActivationValidThru());
                updateIfPresent(existingInventoryItem::setUnitCost, inventoryItem.getUnitCost());

                return existingInventoryItem;
            })
            .map(inventoryItemRepository::save);
    }

    /**
     * Get all the inventoryItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<InventoryItem> findAllWithEagerRelationships(Pageable pageable) {
        return inventoryItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one inventoryItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InventoryItem> findOne(Long id) {
        LOG.debug("Request to get InventoryItem : {}", id);
        return inventoryItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the inventoryItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InventoryItem : {}", id);
        inventoryItemRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
