package com.snp.service;

import com.snp.domain.ShipmentMethodType;
import com.snp.repository.ShipmentMethodTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ShipmentMethodType}.
 */
@Service
@Transactional
public class ShipmentMethodTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMethodTypeService.class);

    private final ShipmentMethodTypeRepository shipmentMethodTypeRepository;

    public ShipmentMethodTypeService(ShipmentMethodTypeRepository shipmentMethodTypeRepository) {
        this.shipmentMethodTypeRepository = shipmentMethodTypeRepository;
    }

    /**
     * Save a shipmentMethodType.
     *
     * @param shipmentMethodType the entity to save.
     * @return the persisted entity.
     */
    public ShipmentMethodType save(ShipmentMethodType shipmentMethodType) {
        LOG.debug("Request to save ShipmentMethodType : {}", shipmentMethodType);
        return shipmentMethodTypeRepository.save(shipmentMethodType);
    }

    /**
     * Update a shipmentMethodType.
     *
     * @param shipmentMethodType the entity to save.
     * @return the persisted entity.
     */
    public ShipmentMethodType update(ShipmentMethodType shipmentMethodType) {
        LOG.debug("Request to update ShipmentMethodType : {}", shipmentMethodType);
        return shipmentMethodTypeRepository.save(shipmentMethodType);
    }

    /**
     * Partially update a shipmentMethodType.
     *
     * @param shipmentMethodType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentMethodType> partialUpdate(ShipmentMethodType shipmentMethodType) {
        LOG.debug("Request to partially update ShipmentMethodType : {}", shipmentMethodType);

        return shipmentMethodTypeRepository
            .findById(shipmentMethodType.getId())
            .map(existingShipmentMethodType -> {
                updateIfPresent(existingShipmentMethodType::setName, shipmentMethodType.getName());
                updateIfPresent(existingShipmentMethodType::setDescription, shipmentMethodType.getDescription());
                updateIfPresent(existingShipmentMethodType::setSequenceNo, shipmentMethodType.getSequenceNo());

                return existingShipmentMethodType;
            })
            .map(shipmentMethodTypeRepository::save);
    }

    /**
     * Get one shipmentMethodType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentMethodType> findOne(Long id) {
        LOG.debug("Request to get ShipmentMethodType : {}", id);
        return shipmentMethodTypeRepository.findById(id);
    }

    /**
     * Delete the shipmentMethodType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentMethodType : {}", id);
        shipmentMethodTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
