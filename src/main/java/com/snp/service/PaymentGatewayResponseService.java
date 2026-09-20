package com.snp.service;

import com.snp.domain.PaymentGatewayResponse;
import com.snp.repository.PaymentGatewayResponseRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PaymentGatewayResponse}.
 */
@Service
@Transactional
public class PaymentGatewayResponseService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayResponseService.class);

    private final PaymentGatewayResponseRepository paymentGatewayResponseRepository;

    public PaymentGatewayResponseService(PaymentGatewayResponseRepository paymentGatewayResponseRepository) {
        this.paymentGatewayResponseRepository = paymentGatewayResponseRepository;
    }

    /**
     * Save a paymentGatewayResponse.
     *
     * @param paymentGatewayResponse the entity to save.
     * @return the persisted entity.
     */
    public PaymentGatewayResponse save(PaymentGatewayResponse paymentGatewayResponse) {
        LOG.debug("Request to save PaymentGatewayResponse : {}", paymentGatewayResponse);
        return paymentGatewayResponseRepository.save(paymentGatewayResponse);
    }

    /**
     * Update a paymentGatewayResponse.
     *
     * @param paymentGatewayResponse the entity to save.
     * @return the persisted entity.
     */
    public PaymentGatewayResponse update(PaymentGatewayResponse paymentGatewayResponse) {
        LOG.debug("Request to update PaymentGatewayResponse : {}", paymentGatewayResponse);
        return paymentGatewayResponseRepository.save(paymentGatewayResponse);
    }

    /**
     * Partially update a paymentGatewayResponse.
     *
     * @param paymentGatewayResponse the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentGatewayResponse> partialUpdate(PaymentGatewayResponse paymentGatewayResponse) {
        LOG.debug("Request to partially update PaymentGatewayResponse : {}", paymentGatewayResponse);

        return paymentGatewayResponseRepository
            .findById(paymentGatewayResponse.getId())
            .map(existingPaymentGatewayResponse -> {
                updateIfPresent(existingPaymentGatewayResponse::setReferenceNumber, paymentGatewayResponse.getReferenceNumber());
                updateIfPresent(existingPaymentGatewayResponse::setAmount, paymentGatewayResponse.getAmount());
                updateIfPresent(existingPaymentGatewayResponse::setGatewayMessage, paymentGatewayResponse.getGatewayMessage());

                return existingPaymentGatewayResponse;
            })
            .map(paymentGatewayResponseRepository::save);
    }

    /**
     * Get all the paymentGatewayResponses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PaymentGatewayResponse> findAllWithEagerRelationships(Pageable pageable) {
        return paymentGatewayResponseRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one paymentGatewayResponse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentGatewayResponse> findOne(Long id) {
        LOG.debug("Request to get PaymentGatewayResponse : {}", id);
        return paymentGatewayResponseRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the paymentGatewayResponse by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentGatewayResponse : {}", id);
        paymentGatewayResponseRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
