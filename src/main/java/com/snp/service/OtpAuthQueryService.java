package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.OtpAuth;
import com.snp.repository.OtpAuthRepository;
import com.snp.service.criteria.OtpAuthCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OtpAuth} entities in the database.
 * The main input is a {@link OtpAuthCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OtpAuth} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OtpAuthQueryService extends QueryService<OtpAuth> {

    private static final Logger LOG = LoggerFactory.getLogger(OtpAuthQueryService.class);

    private final OtpAuthRepository otpAuthRepository;

    public OtpAuthQueryService(OtpAuthRepository otpAuthRepository) {
        this.otpAuthRepository = otpAuthRepository;
    }

    /**
     * Return a {@link Page} of {@link OtpAuth} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OtpAuth> findByCriteria(OtpAuthCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OtpAuth> specification = createSpecification(criteria);
        return otpAuthRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OtpAuthCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OtpAuth> specification = createSpecification(criteria);
        return otpAuthRepository.count(specification);
    }

    /**
     * Function to convert {@link OtpAuthCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OtpAuth> createSpecification(OtpAuthCriteria criteria) {
        Specification<OtpAuth> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(OtpAuth_.party, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), OtpAuth_.id),
                    buildStringSpecification(criteria.getOtp(), OtpAuth_.otp),
                    buildSpecification(criteria.getOtpSent(), OtpAuth_.otpSent),
                    buildSpecification(criteria.getVerificationSuccess(), OtpAuth_.verificationSuccess),
                    buildSpecification(criteria.getOtpExpired(), OtpAuth_.otpExpired),
                    buildRangeSpecification(criteria.getOtpSentTime(), OtpAuth_.otpSentTime),
                    buildRangeSpecification(criteria.getSentCounter(), OtpAuth_.sentCounter),
                    buildRangeSpecification(criteria.getFailCounter(), OtpAuth_.failCounter),
                    buildRangeSpecification(criteria.getOtpResetCounter(), OtpAuth_.otpResetCounter),
                    buildRangeSpecification(criteria.getMaxResend(), OtpAuth_.maxResend),
                    buildRangeSpecification(criteria.getMaxReset(), OtpAuth_.maxReset),
                    buildRangeSpecification(criteria.getMaxFailures(), OtpAuth_.maxFailures),
                    buildRangeSpecification(criteria.getOtpActiveTime(), OtpAuth_.otpActiveTime),
                    buildSpecification(criteria.getPartyId(), root -> root.join(OtpAuth_.party, JoinType.LEFT).get(Party_.id))
                )
            );
        }
        return specification;
    }
}
