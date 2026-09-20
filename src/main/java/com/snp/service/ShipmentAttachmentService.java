package com.snp.service;

import com.snp.domain.ShipmentAttachment;
import com.snp.repository.ShipmentAttachmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ShipmentAttachment}.
 */
@Service
@Transactional
public class ShipmentAttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentAttachmentService.class);

    private final ShipmentAttachmentRepository shipmentAttachmentRepository;

    public ShipmentAttachmentService(ShipmentAttachmentRepository shipmentAttachmentRepository) {
        this.shipmentAttachmentRepository = shipmentAttachmentRepository;
    }

    /**
     * Save a shipmentAttachment.
     *
     * @param shipmentAttachment the entity to save.
     * @return the persisted entity.
     */
    public ShipmentAttachment save(ShipmentAttachment shipmentAttachment) {
        LOG.debug("Request to save ShipmentAttachment : {}", shipmentAttachment);
        return shipmentAttachmentRepository.save(shipmentAttachment);
    }

    /**
     * Update a shipmentAttachment.
     *
     * @param shipmentAttachment the entity to save.
     * @return the persisted entity.
     */
    public ShipmentAttachment update(ShipmentAttachment shipmentAttachment) {
        LOG.debug("Request to update ShipmentAttachment : {}", shipmentAttachment);
        return shipmentAttachmentRepository.save(shipmentAttachment);
    }

    /**
     * Partially update a shipmentAttachment.
     *
     * @param shipmentAttachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentAttachment> partialUpdate(ShipmentAttachment shipmentAttachment) {
        LOG.debug("Request to partially update ShipmentAttachment : {}", shipmentAttachment);

        return shipmentAttachmentRepository
            .findById(shipmentAttachment.getId())
            .map(existingShipmentAttachment -> {
                updateIfPresent(existingShipmentAttachment::setName, shipmentAttachment.getName());

                return existingShipmentAttachment;
            })
            .map(shipmentAttachmentRepository::save);
    }

    /**
     * Get all the shipmentAttachments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ShipmentAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return shipmentAttachmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one shipmentAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentAttachment> findOne(Long id) {
        LOG.debug("Request to get ShipmentAttachment : {}", id);
        return shipmentAttachmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the shipmentAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentAttachment : {}", id);
        shipmentAttachmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
