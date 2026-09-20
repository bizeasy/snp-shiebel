package com.snp.service;

import com.snp.domain.PaymentMethod;
import com.snp.repository.PaymentMethodRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PaymentMethod}.
 */
@Service
@Transactional
public class PaymentMethodService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMethodService.class);

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    /**
     * Save a paymentMethod.
     *
     * @param paymentMethod the entity to save.
     * @return the persisted entity.
     */
    public PaymentMethod save(PaymentMethod paymentMethod) {
        LOG.debug("Request to save PaymentMethod : {}", paymentMethod);
        return paymentMethodRepository.save(paymentMethod);
    }

    /**
     * Update a paymentMethod.
     *
     * @param paymentMethod the entity to save.
     * @return the persisted entity.
     */
    public PaymentMethod update(PaymentMethod paymentMethod) {
        LOG.debug("Request to update PaymentMethod : {}", paymentMethod);
        return paymentMethodRepository.save(paymentMethod);
    }

    /**
     * Partially update a paymentMethod.
     *
     * @param paymentMethod the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentMethod> partialUpdate(PaymentMethod paymentMethod) {
        LOG.debug("Request to partially update PaymentMethod : {}", paymentMethod);

        return paymentMethodRepository
            .findById(paymentMethod.getId())
            .map(existingPaymentMethod -> {
                updateIfPresent(existingPaymentMethod::setName, paymentMethod.getName());
                updateIfPresent(existingPaymentMethod::setDescription, paymentMethod.getDescription());
                updateIfPresent(existingPaymentMethod::setFromDate, paymentMethod.getFromDate());
                updateIfPresent(existingPaymentMethod::setThruDate, paymentMethod.getThruDate());

                return existingPaymentMethod;
            })
            .map(paymentMethodRepository::save);
    }

    /**
     * Get all the paymentMethods with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PaymentMethod> findAllWithEagerRelationships(Pageable pageable) {
        return paymentMethodRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one paymentMethod by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentMethod> findOne(Long id) {
        LOG.debug("Request to get PaymentMethod : {}", id);
        return paymentMethodRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the paymentMethod by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentMethod : {}", id);
        paymentMethodRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
