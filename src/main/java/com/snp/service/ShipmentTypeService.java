package com.snp.service;

import com.snp.domain.ShipmentType;
import com.snp.repository.ShipmentTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ShipmentType}.
 */
@Service
@Transactional
public class ShipmentTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentTypeService.class);

    private final ShipmentTypeRepository shipmentTypeRepository;

    public ShipmentTypeService(ShipmentTypeRepository shipmentTypeRepository) {
        this.shipmentTypeRepository = shipmentTypeRepository;
    }

    /**
     * Save a shipmentType.
     *
     * @param shipmentType the entity to save.
     * @return the persisted entity.
     */
    public ShipmentType save(ShipmentType shipmentType) {
        LOG.debug("Request to save ShipmentType : {}", shipmentType);
        return shipmentTypeRepository.save(shipmentType);
    }

    /**
     * Update a shipmentType.
     *
     * @param shipmentType the entity to save.
     * @return the persisted entity.
     */
    public ShipmentType update(ShipmentType shipmentType) {
        LOG.debug("Request to update ShipmentType : {}", shipmentType);
        return shipmentTypeRepository.save(shipmentType);
    }

    /**
     * Partially update a shipmentType.
     *
     * @param shipmentType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentType> partialUpdate(ShipmentType shipmentType) {
        LOG.debug("Request to partially update ShipmentType : {}", shipmentType);

        return shipmentTypeRepository
            .findById(shipmentType.getId())
            .map(existingShipmentType -> {
                updateIfPresent(existingShipmentType::setName, shipmentType.getName());
                updateIfPresent(existingShipmentType::setDescription, shipmentType.getDescription());

                return existingShipmentType;
            })
            .map(shipmentTypeRepository::save);
    }

    /**
     * Get one shipmentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentType> findOne(Long id) {
        LOG.debug("Request to get ShipmentType : {}", id);
        return shipmentTypeRepository.findById(id);
    }

    /**
     * Delete the shipmentType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentType : {}", id);
        shipmentTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
