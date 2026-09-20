package com.snp.service;

import com.snp.domain.ShipmentStatus;
import com.snp.repository.ShipmentStatusRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ShipmentStatus}.
 */
@Service
@Transactional
public class ShipmentStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentStatusService.class);

    private final ShipmentStatusRepository shipmentStatusRepository;

    public ShipmentStatusService(ShipmentStatusRepository shipmentStatusRepository) {
        this.shipmentStatusRepository = shipmentStatusRepository;
    }

    /**
     * Save a shipmentStatus.
     *
     * @param shipmentStatus the entity to save.
     * @return the persisted entity.
     */
    public ShipmentStatus save(ShipmentStatus shipmentStatus) {
        LOG.debug("Request to save ShipmentStatus : {}", shipmentStatus);
        return shipmentStatusRepository.save(shipmentStatus);
    }

    /**
     * Update a shipmentStatus.
     *
     * @param shipmentStatus the entity to save.
     * @return the persisted entity.
     */
    public ShipmentStatus update(ShipmentStatus shipmentStatus) {
        LOG.debug("Request to update ShipmentStatus : {}", shipmentStatus);
        return shipmentStatusRepository.save(shipmentStatus);
    }

    /**
     * Partially update a shipmentStatus.
     *
     * @param shipmentStatus the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentStatus> partialUpdate(ShipmentStatus shipmentStatus) {
        LOG.debug("Request to partially update ShipmentStatus : {}", shipmentStatus);

        return shipmentStatusRepository
            .findById(shipmentStatus.getId())
            .map(existingShipmentStatus -> {
                updateIfPresent(existingShipmentStatus::setStatusDateTime, shipmentStatus.getStatusDateTime());
                updateIfPresent(existingShipmentStatus::setSequenceNo, shipmentStatus.getSequenceNo());

                return existingShipmentStatus;
            })
            .map(shipmentStatusRepository::save);
    }

    /**
     * Get all the shipmentStatuses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ShipmentStatus> findAllWithEagerRelationships(Pageable pageable) {
        return shipmentStatusRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one shipmentStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentStatus> findOne(Long id) {
        LOG.debug("Request to get ShipmentStatus : {}", id);
        return shipmentStatusRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the shipmentStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentStatus : {}", id);
        shipmentStatusRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
