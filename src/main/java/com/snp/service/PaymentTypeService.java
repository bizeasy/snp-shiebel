package com.snp.service;

import com.snp.domain.PaymentType;
import com.snp.repository.PaymentTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PaymentType}.
 */
@Service
@Transactional
public class PaymentTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentTypeService.class);

    private final PaymentTypeRepository paymentTypeRepository;

    public PaymentTypeService(PaymentTypeRepository paymentTypeRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
    }

    /**
     * Save a paymentType.
     *
     * @param paymentType the entity to save.
     * @return the persisted entity.
     */
    public PaymentType save(PaymentType paymentType) {
        LOG.debug("Request to save PaymentType : {}", paymentType);
        return paymentTypeRepository.save(paymentType);
    }

    /**
     * Update a paymentType.
     *
     * @param paymentType the entity to save.
     * @return the persisted entity.
     */
    public PaymentType update(PaymentType paymentType) {
        LOG.debug("Request to update PaymentType : {}", paymentType);
        return paymentTypeRepository.save(paymentType);
    }

    /**
     * Partially update a paymentType.
     *
     * @param paymentType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentType> partialUpdate(PaymentType paymentType) {
        LOG.debug("Request to partially update PaymentType : {}", paymentType);

        return paymentTypeRepository
            .findById(paymentType.getId())
            .map(existingPaymentType -> {
                updateIfPresent(existingPaymentType::setName, paymentType.getName());
                updateIfPresent(existingPaymentType::setDescription, paymentType.getDescription());

                return existingPaymentType;
            })
            .map(paymentTypeRepository::save);
    }

    /**
     * Get one paymentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentType> findOne(Long id) {
        LOG.debug("Request to get PaymentType : {}", id);
        return paymentTypeRepository.findById(id);
    }

    /**
     * Delete the paymentType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentType : {}", id);
        paymentTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
