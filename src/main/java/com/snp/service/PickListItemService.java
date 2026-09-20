package com.snp.service;

import com.snp.domain.PickListItem;
import com.snp.repository.PickListItemRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PickListItem}.
 */
@Service
@Transactional
public class PickListItemService {

    private static final Logger LOG = LoggerFactory.getLogger(PickListItemService.class);

    private final PickListItemRepository pickListItemRepository;

    public PickListItemService(PickListItemRepository pickListItemRepository) {
        this.pickListItemRepository = pickListItemRepository;
    }

    /**
     * Save a pickListItem.
     *
     * @param pickListItem the entity to save.
     * @return the persisted entity.
     */
    public PickListItem save(PickListItem pickListItem) {
        LOG.debug("Request to save PickListItem : {}", pickListItem);
        return pickListItemRepository.save(pickListItem);
    }

    /**
     * Update a pickListItem.
     *
     * @param pickListItem the entity to save.
     * @return the persisted entity.
     */
    public PickListItem update(PickListItem pickListItem) {
        LOG.debug("Request to update PickListItem : {}", pickListItem);
        return pickListItemRepository.save(pickListItem);
    }

    /**
     * Partially update a pickListItem.
     *
     * @param pickListItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PickListItem> partialUpdate(PickListItem pickListItem) {
        LOG.debug("Request to partially update PickListItem : {}", pickListItem);

        return pickListItemRepository
            .findById(pickListItem.getId())
            .map(existingPickListItem -> {
                updateIfPresent(existingPickListItem::setQuantity, pickListItem.getQuantity());

                return existingPickListItem;
            })
            .map(pickListItemRepository::save);
    }

    /**
     * Get all the pickListItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PickListItem> findAllWithEagerRelationships(Pageable pageable) {
        return pickListItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one pickListItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PickListItem> findOne(Long id) {
        LOG.debug("Request to get PickListItem : {}", id);
        return pickListItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the pickListItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PickListItem : {}", id);
        pickListItemRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
