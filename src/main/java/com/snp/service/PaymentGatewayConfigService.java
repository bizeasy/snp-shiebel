package com.snp.service;

import com.snp.domain.PaymentGatewayConfig;
import com.snp.repository.PaymentGatewayConfigRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PaymentGatewayConfig}.
 */
@Service
@Transactional
public class PaymentGatewayConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayConfigService.class);

    private final PaymentGatewayConfigRepository paymentGatewayConfigRepository;

    public PaymentGatewayConfigService(PaymentGatewayConfigRepository paymentGatewayConfigRepository) {
        this.paymentGatewayConfigRepository = paymentGatewayConfigRepository;
    }

    /**
     * Save a paymentGatewayConfig.
     *
     * @param paymentGatewayConfig the entity to save.
     * @return the persisted entity.
     */
    public PaymentGatewayConfig save(PaymentGatewayConfig paymentGatewayConfig) {
        LOG.debug("Request to save PaymentGatewayConfig : {}", paymentGatewayConfig);
        return paymentGatewayConfigRepository.save(paymentGatewayConfig);
    }

    /**
     * Update a paymentGatewayConfig.
     *
     * @param paymentGatewayConfig the entity to save.
     * @return the persisted entity.
     */
    public PaymentGatewayConfig update(PaymentGatewayConfig paymentGatewayConfig) {
        LOG.debug("Request to update PaymentGatewayConfig : {}", paymentGatewayConfig);
        return paymentGatewayConfigRepository.save(paymentGatewayConfig);
    }

    /**
     * Partially update a paymentGatewayConfig.
     *
     * @param paymentGatewayConfig the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentGatewayConfig> partialUpdate(PaymentGatewayConfig paymentGatewayConfig) {
        LOG.debug("Request to partially update PaymentGatewayConfig : {}", paymentGatewayConfig);

        return paymentGatewayConfigRepository
            .findById(paymentGatewayConfig.getId())
            .map(existingPaymentGatewayConfig -> {
                updateIfPresent(existingPaymentGatewayConfig::setName, paymentGatewayConfig.getName());
                updateIfPresent(existingPaymentGatewayConfig::setAuthUrl, paymentGatewayConfig.getAuthUrl());
                updateIfPresent(existingPaymentGatewayConfig::setReleaseUrl, paymentGatewayConfig.getReleaseUrl());
                updateIfPresent(existingPaymentGatewayConfig::setRefundUrl, paymentGatewayConfig.getRefundUrl());

                return existingPaymentGatewayConfig;
            })
            .map(paymentGatewayConfigRepository::save);
    }

    /**
     * Get one paymentGatewayConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentGatewayConfig> findOne(Long id) {
        LOG.debug("Request to get PaymentGatewayConfig : {}", id);
        return paymentGatewayConfigRepository.findById(id);
    }

    /**
     * Delete the paymentGatewayConfig by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentGatewayConfig : {}", id);
        paymentGatewayConfigRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
