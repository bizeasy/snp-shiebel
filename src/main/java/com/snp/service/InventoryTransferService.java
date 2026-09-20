package com.snp.service;

import com.snp.domain.InventoryTransfer;
import com.snp.repository.InventoryTransferRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.InventoryTransfer}.
 */
@Service
@Transactional
public class InventoryTransferService {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryTransferService.class);

    private final InventoryTransferRepository inventoryTransferRepository;

    public InventoryTransferService(InventoryTransferRepository inventoryTransferRepository) {
        this.inventoryTransferRepository = inventoryTransferRepository;
    }

    /**
     * Save a inventoryTransfer.
     *
     * @param inventoryTransfer the entity to save.
     * @return the persisted entity.
     */
    public InventoryTransfer save(InventoryTransfer inventoryTransfer) {
        LOG.debug("Request to save InventoryTransfer : {}", inventoryTransfer);
        return inventoryTransferRepository.save(inventoryTransfer);
    }

    /**
     * Update a inventoryTransfer.
     *
     * @param inventoryTransfer the entity to save.
     * @return the persisted entity.
     */
    public InventoryTransfer update(InventoryTransfer inventoryTransfer) {
        LOG.debug("Request to update InventoryTransfer : {}", inventoryTransfer);
        return inventoryTransferRepository.save(inventoryTransfer);
    }

    /**
     * Partially update a inventoryTransfer.
     *
     * @param inventoryTransfer the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InventoryTransfer> partialUpdate(InventoryTransfer inventoryTransfer) {
        LOG.debug("Request to partially update InventoryTransfer : {}", inventoryTransfer);

        return inventoryTransferRepository
            .findById(inventoryTransfer.getId())
            .map(existingInventoryTransfer -> {
                updateIfPresent(existingInventoryTransfer::setSentDate, inventoryTransfer.getSentDate());
                updateIfPresent(existingInventoryTransfer::setReceivedDate, inventoryTransfer.getReceivedDate());
                updateIfPresent(existingInventoryTransfer::setComments, inventoryTransfer.getComments());

                return existingInventoryTransfer;
            })
            .map(inventoryTransferRepository::save);
    }

    /**
     * Get all the inventoryTransfers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<InventoryTransfer> findAllWithEagerRelationships(Pageable pageable) {
        return inventoryTransferRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one inventoryTransfer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InventoryTransfer> findOne(Long id) {
        LOG.debug("Request to get InventoryTransfer : {}", id);
        return inventoryTransferRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the inventoryTransfer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InventoryTransfer : {}", id);
        inventoryTransferRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
