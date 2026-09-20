package com.snp.service;

import com.snp.domain.PaymentApplication;
import com.snp.repository.PaymentApplicationRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PaymentApplication}.
 */
@Service
@Transactional
public class PaymentApplicationService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentApplicationService.class);

    private final PaymentApplicationRepository paymentApplicationRepository;

    public PaymentApplicationService(PaymentApplicationRepository paymentApplicationRepository) {
        this.paymentApplicationRepository = paymentApplicationRepository;
    }

    /**
     * Save a paymentApplication.
     *
     * @param paymentApplication the entity to save.
     * @return the persisted entity.
     */
    public PaymentApplication save(PaymentApplication paymentApplication) {
        LOG.debug("Request to save PaymentApplication : {}", paymentApplication);
        return paymentApplicationRepository.save(paymentApplication);
    }

    /**
     * Update a paymentApplication.
     *
     * @param paymentApplication the entity to save.
     * @return the persisted entity.
     */
    public PaymentApplication update(PaymentApplication paymentApplication) {
        LOG.debug("Request to update PaymentApplication : {}", paymentApplication);
        return paymentApplicationRepository.save(paymentApplication);
    }

    /**
     * Partially update a paymentApplication.
     *
     * @param paymentApplication the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentApplication> partialUpdate(PaymentApplication paymentApplication) {
        LOG.debug("Request to partially update PaymentApplication : {}", paymentApplication);

        return paymentApplicationRepository
            .findById(paymentApplication.getId())
            .map(existingPaymentApplication -> {
                updateIfPresent(existingPaymentApplication::setAmountApplied, paymentApplication.getAmountApplied());

                return existingPaymentApplication;
            })
            .map(paymentApplicationRepository::save);
    }

    /**
     * Get one paymentApplication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentApplication> findOne(Long id) {
        LOG.debug("Request to get PaymentApplication : {}", id);
        return paymentApplicationRepository.findById(id);
    }

    /**
     * Delete the paymentApplication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentApplication : {}", id);
        paymentApplicationRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
