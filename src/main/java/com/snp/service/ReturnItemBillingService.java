package com.snp.service;

import com.snp.domain.ReturnItemBilling;
import com.snp.repository.ReturnItemBillingRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ReturnItemBilling}.
 */
@Service
@Transactional
public class ReturnItemBillingService {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnItemBillingService.class);

    private final ReturnItemBillingRepository returnItemBillingRepository;

    public ReturnItemBillingService(ReturnItemBillingRepository returnItemBillingRepository) {
        this.returnItemBillingRepository = returnItemBillingRepository;
    }

    /**
     * Save a returnItemBilling.
     *
     * @param returnItemBilling the entity to save.
     * @return the persisted entity.
     */
    public ReturnItemBilling save(ReturnItemBilling returnItemBilling) {
        LOG.debug("Request to save ReturnItemBilling : {}", returnItemBilling);
        return returnItemBillingRepository.save(returnItemBilling);
    }

    /**
     * Update a returnItemBilling.
     *
     * @param returnItemBilling the entity to save.
     * @return the persisted entity.
     */
    public ReturnItemBilling update(ReturnItemBilling returnItemBilling) {
        LOG.debug("Request to update ReturnItemBilling : {}", returnItemBilling);
        return returnItemBillingRepository.save(returnItemBilling);
    }

    /**
     * Partially update a returnItemBilling.
     *
     * @param returnItemBilling the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReturnItemBilling> partialUpdate(ReturnItemBilling returnItemBilling) {
        LOG.debug("Request to partially update ReturnItemBilling : {}", returnItemBilling);

        return returnItemBillingRepository
            .findById(returnItemBilling.getId())
            .map(existingReturnItemBilling -> {
                updateIfPresent(existingReturnItemBilling::setQuantity, returnItemBilling.getQuantity());
                updateIfPresent(existingReturnItemBilling::setAmount, returnItemBilling.getAmount());

                return existingReturnItemBilling;
            })
            .map(returnItemBillingRepository::save);
    }

    /**
     * Get one returnItemBilling by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReturnItemBilling> findOne(Long id) {
        LOG.debug("Request to get ReturnItemBilling : {}", id);
        return returnItemBillingRepository.findById(id);
    }

    /**
     * Delete the returnItemBilling by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReturnItemBilling : {}", id);
        returnItemBillingRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
