package com.snp.service;

import com.snp.domain.ItemIssuance;
import com.snp.repository.ItemIssuanceRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ItemIssuance}.
 */
@Service
@Transactional
public class ItemIssuanceService {

    private static final Logger LOG = LoggerFactory.getLogger(ItemIssuanceService.class);

    private final ItemIssuanceRepository itemIssuanceRepository;

    public ItemIssuanceService(ItemIssuanceRepository itemIssuanceRepository) {
        this.itemIssuanceRepository = itemIssuanceRepository;
    }

    /**
     * Save a itemIssuance.
     *
     * @param itemIssuance the entity to save.
     * @return the persisted entity.
     */
    public ItemIssuance save(ItemIssuance itemIssuance) {
        LOG.debug("Request to save ItemIssuance : {}", itemIssuance);
        return itemIssuanceRepository.save(itemIssuance);
    }

    /**
     * Update a itemIssuance.
     *
     * @param itemIssuance the entity to save.
     * @return the persisted entity.
     */
    public ItemIssuance update(ItemIssuance itemIssuance) {
        LOG.debug("Request to update ItemIssuance : {}", itemIssuance);
        return itemIssuanceRepository.save(itemIssuance);
    }

    /**
     * Partially update a itemIssuance.
     *
     * @param itemIssuance the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ItemIssuance> partialUpdate(ItemIssuance itemIssuance) {
        LOG.debug("Request to partially update ItemIssuance : {}", itemIssuance);

        return itemIssuanceRepository
            .findById(itemIssuance.getId())
            .map(existingItemIssuance -> {
                updateIfPresent(existingItemIssuance::setMessage, itemIssuance.getMessage());
                updateIfPresent(existingItemIssuance::setIssuedDate, itemIssuance.getIssuedDate());
                updateIfPresent(existingItemIssuance::setIssuedBy, itemIssuance.getIssuedBy());
                updateIfPresent(existingItemIssuance::setQuantity, itemIssuance.getQuantity());
                updateIfPresent(existingItemIssuance::setCancelQuantity, itemIssuance.getCancelQuantity());

                return existingItemIssuance;
            })
            .map(itemIssuanceRepository::save);
    }

    /**
     * Get one itemIssuance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemIssuance> findOne(Long id) {
        LOG.debug("Request to get ItemIssuance : {}", id);
        return itemIssuanceRepository.findById(id);
    }

    /**
     * Delete the itemIssuance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ItemIssuance : {}", id);
        itemIssuanceRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
