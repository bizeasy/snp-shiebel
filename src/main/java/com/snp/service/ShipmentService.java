package com.snp.service;

import com.snp.domain.Shipment;
import com.snp.repository.ShipmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Shipment}.
 */
@Service
@Transactional
public class ShipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentService.class);

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    /**
     * Save a shipment.
     *
     * @param shipment the entity to save.
     * @return the persisted entity.
     */
    public Shipment save(Shipment shipment) {
        LOG.debug("Request to save Shipment : {}", shipment);
        return shipmentRepository.save(shipment);
    }

    /**
     * Update a shipment.
     *
     * @param shipment the entity to save.
     * @return the persisted entity.
     */
    public Shipment update(Shipment shipment) {
        LOG.debug("Request to update Shipment : {}", shipment);
        return shipmentRepository.save(shipment);
    }

    /**
     * Partially update a shipment.
     *
     * @param shipment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Shipment> partialUpdate(Shipment shipment) {
        LOG.debug("Request to partially update Shipment : {}", shipment);

        return shipmentRepository
            .findById(shipment.getId())
            .map(existingShipment -> {
                updateIfPresent(existingShipment::setEstimatedShipDate, shipment.getEstimatedShipDate());
                updateIfPresent(existingShipment::setEstimatedReadyDate, shipment.getEstimatedReadyDate());
                updateIfPresent(existingShipment::setEstimatedArrivalDate, shipment.getEstimatedArrivalDate());
                updateIfPresent(existingShipment::setEstimatedShipCost, shipment.getEstimatedShipCost());

                return existingShipment;
            })
            .map(shipmentRepository::save);
    }

    /**
     * Get all the shipments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Shipment> findAllWithEagerRelationships(Pageable pageable) {
        return shipmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one shipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Shipment> findOne(Long id) {
        LOG.debug("Request to get Shipment : {}", id);
        return shipmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the shipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Shipment : {}", id);
        shipmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
