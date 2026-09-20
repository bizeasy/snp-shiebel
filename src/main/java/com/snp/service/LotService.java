package com.snp.service;

import com.snp.domain.Lot;
import com.snp.repository.LotRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Lot}.
 */
@Service
@Transactional
public class LotService {

    private static final Logger LOG = LoggerFactory.getLogger(LotService.class);

    private final LotRepository lotRepository;

    public LotService(LotRepository lotRepository) {
        this.lotRepository = lotRepository;
    }

    /**
     * Save a lot.
     *
     * @param lot the entity to save.
     * @return the persisted entity.
     */
    public Lot save(Lot lot) {
        LOG.debug("Request to save Lot : {}", lot);
        return lotRepository.save(lot);
    }

    /**
     * Update a lot.
     *
     * @param lot the entity to save.
     * @return the persisted entity.
     */
    public Lot update(Lot lot) {
        LOG.debug("Request to update Lot : {}", lot);
        return lotRepository.save(lot);
    }

    /**
     * Partially update a lot.
     *
     * @param lot the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Lot> partialUpdate(Lot lot) {
        LOG.debug("Request to partially update Lot : {}", lot);

        return lotRepository
            .findById(lot.getId())
            .map(existingLot -> {
                updateIfPresent(existingLot::setCreationDate, lot.getCreationDate());
                updateIfPresent(existingLot::setQuantity, lot.getQuantity());
                updateIfPresent(existingLot::setExpirationDate, lot.getExpirationDate());
                updateIfPresent(existingLot::setRetestDate, lot.getRetestDate());

                return existingLot;
            })
            .map(lotRepository::save);
    }

    /**
     * Get one lot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Lot> findOne(Long id) {
        LOG.debug("Request to get Lot : {}", id);
        return lotRepository.findById(id);
    }

    /**
     * Delete the lot by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Lot : {}", id);
        lotRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
