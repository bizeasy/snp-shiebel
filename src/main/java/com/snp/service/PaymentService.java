package com.snp.service;

import com.snp.domain.Payment;
import com.snp.repository.PaymentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Payment}.
 */
@Service
@Transactional
public class PaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Save a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    public Payment save(Payment payment) {
        LOG.debug("Request to save Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    /**
     * Update a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    public Payment update(Payment payment) {
        LOG.debug("Request to update Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    /**
     * Partially update a payment.
     *
     * @param payment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Payment> partialUpdate(Payment payment) {
        LOG.debug("Request to partially update Payment : {}", payment);

        return paymentRepository
            .findById(payment.getId())
            .map(existingPayment -> {
                updateIfPresent(existingPayment::setPaymentRefNumber, payment.getPaymentRefNumber());
                updateIfPresent(existingPayment::setEffectiveDate, payment.getEffectiveDate());
                updateIfPresent(existingPayment::setPaymentDate, payment.getPaymentDate());
                updateIfPresent(existingPayment::setAmount, payment.getAmount());
                updateIfPresent(existingPayment::setActualAmount, payment.getActualAmount());
                updateIfPresent(existingPayment::setPaymentStatus, payment.getPaymentStatus());
                updateIfPresent(existingPayment::setMihpayId, payment.getMihpayId());
                updateIfPresent(existingPayment::setTxnId, payment.getTxnId());
                updateIfPresent(existingPayment::setEmail, payment.getEmail());
                updateIfPresent(existingPayment::setPhone, payment.getPhone());
                updateIfPresent(existingPayment::setProductInfo, payment.getProductInfo());

                return existingPayment;
            })
            .map(paymentRepository::save);
    }

    /**
     * Get all the payments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Payment> findAllWithEagerRelationships(Pageable pageable) {
        return paymentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Payment> findOne(Long id) {
        LOG.debug("Request to get Payment : {}", id);
        return paymentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
