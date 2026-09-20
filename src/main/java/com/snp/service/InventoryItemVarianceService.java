package com.snp.service;

import com.snp.domain.InventoryItemVariance;
import com.snp.repository.InventoryItemVarianceRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.InventoryItemVariance}.
 */
@Service
@Transactional
public class InventoryItemVarianceService {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemVarianceService.class);

    private final InventoryItemVarianceRepository inventoryItemVarianceRepository;

    public InventoryItemVarianceService(InventoryItemVarianceRepository inventoryItemVarianceRepository) {
        this.inventoryItemVarianceRepository = inventoryItemVarianceRepository;
    }

    /**
     * Save a inventoryItemVariance.
     *
     * @param inventoryItemVariance the entity to save.
     * @return the persisted entity.
     */
    public InventoryItemVariance save(InventoryItemVariance inventoryItemVariance) {
        LOG.debug("Request to save InventoryItemVariance : {}", inventoryItemVariance);
        return inventoryItemVarianceRepository.save(inventoryItemVariance);
    }

    /**
     * Update a inventoryItemVariance.
     *
     * @param inventoryItemVariance the entity to save.
     * @return the persisted entity.
     */
    public InventoryItemVariance update(InventoryItemVariance inventoryItemVariance) {
        LOG.debug("Request to update InventoryItemVariance : {}", inventoryItemVariance);
        return inventoryItemVarianceRepository.save(inventoryItemVariance);
    }

    /**
     * Partially update a inventoryItemVariance.
     *
     * @param inventoryItemVariance the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InventoryItemVariance> partialUpdate(InventoryItemVariance inventoryItemVariance) {
        LOG.debug("Request to partially update InventoryItemVariance : {}", inventoryItemVariance);

        return inventoryItemVarianceRepository
            .findById(inventoryItemVariance.getId())
            .map(existingInventoryItemVariance -> {
                updateIfPresent(existingInventoryItemVariance::setVarianceReason, inventoryItemVariance.getVarianceReason());
                updateIfPresent(existingInventoryItemVariance::setAtpVar, inventoryItemVariance.getAtpVar());
                updateIfPresent(existingInventoryItemVariance::setQohVar, inventoryItemVariance.getQohVar());
                updateIfPresent(existingInventoryItemVariance::setComments, inventoryItemVariance.getComments());

                return existingInventoryItemVariance;
            })
            .map(inventoryItemVarianceRepository::save);
    }

    /**
     * Get one inventoryItemVariance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InventoryItemVariance> findOne(Long id) {
        LOG.debug("Request to get InventoryItemVariance : {}", id);
        return inventoryItemVarianceRepository.findById(id);
    }

    /**
     * Delete the inventoryItemVariance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InventoryItemVariance : {}", id);
        inventoryItemVarianceRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
