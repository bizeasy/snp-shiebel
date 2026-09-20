package com.snp.service;

import com.snp.domain.CustRequestItem;
import com.snp.repository.CustRequestItemRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CustRequestItem}.
 */
@Service
@Transactional
public class CustRequestItemService {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestItemService.class);

    private final CustRequestItemRepository custRequestItemRepository;

    public CustRequestItemService(CustRequestItemRepository custRequestItemRepository) {
        this.custRequestItemRepository = custRequestItemRepository;
    }

    /**
     * Save a custRequestItem.
     *
     * @param custRequestItem the entity to save.
     * @return the persisted entity.
     */
    public CustRequestItem save(CustRequestItem custRequestItem) {
        LOG.debug("Request to save CustRequestItem : {}", custRequestItem);
        return custRequestItemRepository.save(custRequestItem);
    }

    /**
     * Update a custRequestItem.
     *
     * @param custRequestItem the entity to save.
     * @return the persisted entity.
     */
    public CustRequestItem update(CustRequestItem custRequestItem) {
        LOG.debug("Request to update CustRequestItem : {}", custRequestItem);
        return custRequestItemRepository.save(custRequestItem);
    }

    /**
     * Partially update a custRequestItem.
     *
     * @param custRequestItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustRequestItem> partialUpdate(CustRequestItem custRequestItem) {
        LOG.debug("Request to partially update CustRequestItem : {}", custRequestItem);

        return custRequestItemRepository
            .findById(custRequestItem.getId())
            .map(existingCustRequestItem -> {
                updateIfPresent(existingCustRequestItem::setSequenceNo, custRequestItem.getSequenceNo());
                updateIfPresent(existingCustRequestItem::setPriority, custRequestItem.getPriority());
                updateIfPresent(existingCustRequestItem::setRequiredByDate, custRequestItem.getRequiredByDate());
                updateIfPresent(existingCustRequestItem::setQuantity, custRequestItem.getQuantity());
                updateIfPresent(existingCustRequestItem::setSelectedAmount, custRequestItem.getSelectedAmount());
                updateIfPresent(existingCustRequestItem::setMaxAmount, custRequestItem.getMaxAmount());
                updateIfPresent(existingCustRequestItem::setDescription, custRequestItem.getDescription());
                updateIfPresent(existingCustRequestItem::setStory, custRequestItem.getStory());

                return existingCustRequestItem;
            })
            .map(custRequestItemRepository::save);
    }

    /**
     * Get all the custRequestItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CustRequestItem> findAllWithEagerRelationships(Pageable pageable) {
        return custRequestItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one custRequestItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustRequestItem> findOne(Long id) {
        LOG.debug("Request to get CustRequestItem : {}", id);
        return custRequestItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the custRequestItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustRequestItem : {}", id);
        custRequestItemRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
