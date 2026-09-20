package com.snp.service;

import com.snp.domain.ShipmentReceipt;
import com.snp.repository.ShipmentReceiptRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ShipmentReceipt}.
 */
@Service
@Transactional
public class ShipmentReceiptService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentReceiptService.class);

    private final ShipmentReceiptRepository shipmentReceiptRepository;

    public ShipmentReceiptService(ShipmentReceiptRepository shipmentReceiptRepository) {
        this.shipmentReceiptRepository = shipmentReceiptRepository;
    }

    /**
     * Save a shipmentReceipt.
     *
     * @param shipmentReceipt the entity to save.
     * @return the persisted entity.
     */
    public ShipmentReceipt save(ShipmentReceipt shipmentReceipt) {
        LOG.debug("Request to save ShipmentReceipt : {}", shipmentReceipt);
        return shipmentReceiptRepository.save(shipmentReceipt);
    }

    /**
     * Update a shipmentReceipt.
     *
     * @param shipmentReceipt the entity to save.
     * @return the persisted entity.
     */
    public ShipmentReceipt update(ShipmentReceipt shipmentReceipt) {
        LOG.debug("Request to update ShipmentReceipt : {}", shipmentReceipt);
        return shipmentReceiptRepository.save(shipmentReceipt);
    }

    /**
     * Partially update a shipmentReceipt.
     *
     * @param shipmentReceipt the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentReceipt> partialUpdate(ShipmentReceipt shipmentReceipt) {
        LOG.debug("Request to partially update ShipmentReceipt : {}", shipmentReceipt);

        return shipmentReceiptRepository
            .findById(shipmentReceipt.getId())
            .map(existingShipmentReceipt -> {
                updateIfPresent(existingShipmentReceipt::setSequenceNo, shipmentReceipt.getSequenceNo());
                updateIfPresent(existingShipmentReceipt::setQuantity, shipmentReceipt.getQuantity());
                updateIfPresent(existingShipmentReceipt::setQuantityAccepted, shipmentReceipt.getQuantityAccepted());
                updateIfPresent(existingShipmentReceipt::setQuantityRejected, shipmentReceipt.getQuantityRejected());
                updateIfPresent(existingShipmentReceipt::setReceivedDate, shipmentReceipt.getReceivedDate());
                updateIfPresent(existingShipmentReceipt::setMessage, shipmentReceipt.getMessage());

                return existingShipmentReceipt;
            })
            .map(shipmentReceiptRepository::save);
    }

    /**
     * Get all the shipmentReceipts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ShipmentReceipt> findAllWithEagerRelationships(Pageable pageable) {
        return shipmentReceiptRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one shipmentReceipt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentReceipt> findOne(Long id) {
        LOG.debug("Request to get ShipmentReceipt : {}", id);
        return shipmentReceiptRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the shipmentReceipt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentReceipt : {}", id);
        shipmentReceiptRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
