package com.snp.service;

import com.snp.domain.ReturnItem;
import com.snp.repository.ReturnItemRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ReturnItem}.
 */
@Service
@Transactional
public class ReturnItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnItemService.class);

    private final ReturnItemRepository returnItemRepository;

    public ReturnItemService(ReturnItemRepository returnItemRepository) {
        this.returnItemRepository = returnItemRepository;
    }

    /**
     * Save a returnItem.
     *
     * @param returnItem the entity to save.
     * @return the persisted entity.
     */
    public ReturnItem save(ReturnItem returnItem) {
        LOG.debug("Request to save ReturnItem : {}", returnItem);
        return returnItemRepository.save(returnItem);
    }

    /**
     * Update a returnItem.
     *
     * @param returnItem the entity to save.
     * @return the persisted entity.
     */
    public ReturnItem update(ReturnItem returnItem) {
        LOG.debug("Request to update ReturnItem : {}", returnItem);
        return returnItemRepository.save(returnItem);
    }

    /**
     * Partially update a returnItem.
     *
     * @param returnItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReturnItem> partialUpdate(ReturnItem returnItem) {
        LOG.debug("Request to partially update ReturnItem : {}", returnItem);

        return returnItemRepository
            .findById(returnItem.getId())
            .map(existingReturnItem -> {
                updateIfPresent(existingReturnItem::setSequenceNo, returnItem.getSequenceNo());
                updateIfPresent(existingReturnItem::setDescription, returnItem.getDescription());
                updateIfPresent(existingReturnItem::setReturnQuantity, returnItem.getReturnQuantity());
                updateIfPresent(existingReturnItem::setReceivedQuantity, returnItem.getReceivedQuantity());
                updateIfPresent(existingReturnItem::setReturnPrice, returnItem.getReturnPrice());
                updateIfPresent(existingReturnItem::setReturnItemPurpose, returnItem.getReturnItemPurpose());

                return existingReturnItem;
            })
            .map(returnItemRepository::save);
    }

    /**
     * Get all the returnItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReturnItem> findAllWithEagerRelationships(Pageable pageable) {
        return returnItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one returnItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReturnItem> findOne(Long id) {
        LOG.debug("Request to get ReturnItem : {}", id);
        return returnItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the returnItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReturnItem : {}", id);
        returnItemRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
