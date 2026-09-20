package com.snp.service;

import com.snp.domain.PickList;
import com.snp.repository.PickListRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PickList}.
 */
@Service
@Transactional
public class PickListService {

    private static final Logger LOG = LoggerFactory.getLogger(PickListService.class);

    private final PickListRepository pickListRepository;

    public PickListService(PickListRepository pickListRepository) {
        this.pickListRepository = pickListRepository;
    }

    /**
     * Save a pickList.
     *
     * @param pickList the entity to save.
     * @return the persisted entity.
     */
    public PickList save(PickList pickList) {
        LOG.debug("Request to save PickList : {}", pickList);
        return pickListRepository.save(pickList);
    }

    /**
     * Update a pickList.
     *
     * @param pickList the entity to save.
     * @return the persisted entity.
     */
    public PickList update(PickList pickList) {
        LOG.debug("Request to update PickList : {}", pickList);
        return pickListRepository.save(pickList);
    }

    /**
     * Partially update a pickList.
     *
     * @param pickList the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PickList> partialUpdate(PickList pickList) {
        LOG.debug("Request to partially update PickList : {}", pickList);

        return pickListRepository
            .findById(pickList.getId())
            .map(existingPickList -> {
                updateIfPresent(existingPickList::setDescription, pickList.getDescription());
                updateIfPresent(existingPickList::setPickListDate, pickList.getPickListDate());

                return existingPickList;
            })
            .map(pickListRepository::save);
    }

    /**
     * Get all the pickLists with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PickList> findAllWithEagerRelationships(Pageable pageable) {
        return pickListRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one pickList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PickList> findOne(Long id) {
        LOG.debug("Request to get PickList : {}", id);
        return pickListRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the pickList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PickList : {}", id);
        pickListRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
