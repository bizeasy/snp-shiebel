package com.snp.service;

import com.snp.domain.ShipmentItem;
import com.snp.repository.ShipmentItemRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ShipmentItem}.
 */
@Service
@Transactional
public class ShipmentItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentItemService.class);

    private final ShipmentItemRepository shipmentItemRepository;

    public ShipmentItemService(ShipmentItemRepository shipmentItemRepository) {
        this.shipmentItemRepository = shipmentItemRepository;
    }

    /**
     * Save a shipmentItem.
     *
     * @param shipmentItem the entity to save.
     * @return the persisted entity.
     */
    public ShipmentItem save(ShipmentItem shipmentItem) {
        LOG.debug("Request to save ShipmentItem : {}", shipmentItem);
        return shipmentItemRepository.save(shipmentItem);
    }

    /**
     * Update a shipmentItem.
     *
     * @param shipmentItem the entity to save.
     * @return the persisted entity.
     */
    public ShipmentItem update(ShipmentItem shipmentItem) {
        LOG.debug("Request to update ShipmentItem : {}", shipmentItem);
        return shipmentItemRepository.save(shipmentItem);
    }

    /**
     * Partially update a shipmentItem.
     *
     * @param shipmentItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentItem> partialUpdate(ShipmentItem shipmentItem) {
        LOG.debug("Request to partially update ShipmentItem : {}", shipmentItem);

        return shipmentItemRepository
            .findById(shipmentItem.getId())
            .map(existingShipmentItem -> {
                updateIfPresent(existingShipmentItem::setSequenceNo, shipmentItem.getSequenceNo());
                updateIfPresent(existingShipmentItem::setQuantity, shipmentItem.getQuantity());
                updateIfPresent(existingShipmentItem::setMessage, shipmentItem.getMessage());

                return existingShipmentItem;
            })
            .map(shipmentItemRepository::save);
    }

    /**
     * Get all the shipmentItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ShipmentItem> findAllWithEagerRelationships(Pageable pageable) {
        return shipmentItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one shipmentItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentItem> findOne(Long id) {
        LOG.debug("Request to get ShipmentItem : {}", id);
        return shipmentItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the shipmentItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentItem : {}", id);
        shipmentItemRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
