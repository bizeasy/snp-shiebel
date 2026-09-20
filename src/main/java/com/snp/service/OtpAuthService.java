package com.snp.service;

import com.snp.domain.OtpAuth;
import com.snp.repository.OtpAuthRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.OtpAuth}.
 */
@Service
@Transactional
public class OtpAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(OtpAuthService.class);

    private final OtpAuthRepository otpAuthRepository;

    public OtpAuthService(OtpAuthRepository otpAuthRepository) {
        this.otpAuthRepository = otpAuthRepository;
    }

    /**
     * Save a otpAuth.
     *
     * @param otpAuth the entity to save.
     * @return the persisted entity.
     */
    public OtpAuth save(OtpAuth otpAuth) {
        LOG.debug("Request to save OtpAuth : {}", otpAuth);
        return otpAuthRepository.save(otpAuth);
    }

    /**
     * Update a otpAuth.
     *
     * @param otpAuth the entity to save.
     * @return the persisted entity.
     */
    public OtpAuth update(OtpAuth otpAuth) {
        LOG.debug("Request to update OtpAuth : {}", otpAuth);
        return otpAuthRepository.save(otpAuth);
    }

    /**
     * Partially update a otpAuth.
     *
     * @param otpAuth the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OtpAuth> partialUpdate(OtpAuth otpAuth) {
        LOG.debug("Request to partially update OtpAuth : {}", otpAuth);

        return otpAuthRepository
            .findById(otpAuth.getId())
            .map(existingOtpAuth -> {
                updateIfPresent(existingOtpAuth::setOtp, otpAuth.getOtp());
                updateIfPresent(existingOtpAuth::setOtpSent, otpAuth.getOtpSent());
                updateIfPresent(existingOtpAuth::setVerificationSuccess, otpAuth.getVerificationSuccess());
                updateIfPresent(existingOtpAuth::setOtpExpired, otpAuth.getOtpExpired());
                updateIfPresent(existingOtpAuth::setOtpSentTime, otpAuth.getOtpSentTime());
                updateIfPresent(existingOtpAuth::setSentCounter, otpAuth.getSentCounter());
                updateIfPresent(existingOtpAuth::setFailCounter, otpAuth.getFailCounter());
                updateIfPresent(existingOtpAuth::setOtpResetCounter, otpAuth.getOtpResetCounter());
                updateIfPresent(existingOtpAuth::setMaxResend, otpAuth.getMaxResend());
                updateIfPresent(existingOtpAuth::setMaxReset, otpAuth.getMaxReset());
                updateIfPresent(existingOtpAuth::setMaxFailures, otpAuth.getMaxFailures());
                updateIfPresent(existingOtpAuth::setOtpActiveTime, otpAuth.getOtpActiveTime());

                return existingOtpAuth;
            })
            .map(otpAuthRepository::save);
    }

    /**
     * Get one otpAuth by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OtpAuth> findOne(Long id) {
        LOG.debug("Request to get OtpAuth : {}", id);
        return otpAuthRepository.findById(id);
    }

    /**
     * Delete the otpAuth by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OtpAuth : {}", id);
        otpAuthRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
