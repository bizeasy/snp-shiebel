package com.snp.service;

import com.snp.domain.PaymentMethodType;
import com.snp.repository.PaymentMethodTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PaymentMethodType}.
 */
@Service
@Transactional
public class PaymentMethodTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMethodTypeService.class);

    private final PaymentMethodTypeRepository paymentMethodTypeRepository;

    public PaymentMethodTypeService(PaymentMethodTypeRepository paymentMethodTypeRepository) {
        this.paymentMethodTypeRepository = paymentMethodTypeRepository;
    }

    /**
     * Save a paymentMethodType.
     *
     * @param paymentMethodType the entity to save.
     * @return the persisted entity.
     */
    public PaymentMethodType save(PaymentMethodType paymentMethodType) {
        LOG.debug("Request to save PaymentMethodType : {}", paymentMethodType);
        return paymentMethodTypeRepository.save(paymentMethodType);
    }

    /**
     * Update a paymentMethodType.
     *
     * @param paymentMethodType the entity to save.
     * @return the persisted entity.
     */
    public PaymentMethodType update(PaymentMethodType paymentMethodType) {
        LOG.debug("Request to update PaymentMethodType : {}", paymentMethodType);
        return paymentMethodTypeRepository.save(paymentMethodType);
    }

    /**
     * Partially update a paymentMethodType.
     *
     * @param paymentMethodType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentMethodType> partialUpdate(PaymentMethodType paymentMethodType) {
        LOG.debug("Request to partially update PaymentMethodType : {}", paymentMethodType);

        return paymentMethodTypeRepository
            .findById(paymentMethodType.getId())
            .map(existingPaymentMethodType -> {
                updateIfPresent(existingPaymentMethodType::setName, paymentMethodType.getName());
                updateIfPresent(existingPaymentMethodType::setDescription, paymentMethodType.getDescription());

                return existingPaymentMethodType;
            })
            .map(paymentMethodTypeRepository::save);
    }

    /**
     * Get one paymentMethodType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentMethodType> findOne(Long id) {
        LOG.debug("Request to get PaymentMethodType : {}", id);
        return paymentMethodTypeRepository.findById(id);
    }

    /**
     * Delete the paymentMethodType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentMethodType : {}", id);
        paymentMethodTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
