package com.snp.service;

import com.snp.domain.CustRequest;
import com.snp.repository.CustRequestRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CustRequest}.
 */
@Service
@Transactional
public class CustRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestService.class);

    private final CustRequestRepository custRequestRepository;

    public CustRequestService(CustRequestRepository custRequestRepository) {
        this.custRequestRepository = custRequestRepository;
    }

    /**
     * Save a custRequest.
     *
     * @param custRequest the entity to save.
     * @return the persisted entity.
     */
    public CustRequest save(CustRequest custRequest) {
        LOG.debug("Request to save CustRequest : {}", custRequest);
        return custRequestRepository.save(custRequest);
    }

    /**
     * Update a custRequest.
     *
     * @param custRequest the entity to save.
     * @return the persisted entity.
     */
    public CustRequest update(CustRequest custRequest) {
        LOG.debug("Request to update CustRequest : {}", custRequest);
        return custRequestRepository.save(custRequest);
    }

    /**
     * Partially update a custRequest.
     *
     * @param custRequest the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustRequest> partialUpdate(CustRequest custRequest) {
        LOG.debug("Request to partially update CustRequest : {}", custRequest);

        return custRequestRepository
            .findById(custRequest.getId())
            .map(existingCustRequest -> {
                updateIfPresent(existingCustRequest::setName, custRequest.getName());
                updateIfPresent(existingCustRequest::setDescription, custRequest.getDescription());
                updateIfPresent(existingCustRequest::setPriority, custRequest.getPriority());
                updateIfPresent(existingCustRequest::setCustRequestDate, custRequest.getCustRequestDate());
                updateIfPresent(existingCustRequest::setResponseRequiredDate, custRequest.getResponseRequiredDate());
                updateIfPresent(existingCustRequest::setOpenDateTime, custRequest.getOpenDateTime());
                updateIfPresent(existingCustRequest::setClosedDateTime, custRequest.getClosedDateTime());
                updateIfPresent(existingCustRequest::setInternalComment, custRequest.getInternalComment());
                updateIfPresent(existingCustRequest::setBilled, custRequest.getBilled());
                updateIfPresent(existingCustRequest::setCustEstimatedDurationDays, custRequest.getCustEstimatedDurationDays());

                return existingCustRequest;
            })
            .map(custRequestRepository::save);
    }

    /**
     * Get all the custRequests with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CustRequest> findAllWithEagerRelationships(Pageable pageable) {
        return custRequestRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one custRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustRequest> findOne(Long id) {
        LOG.debug("Request to get CustRequest : {}", id);
        return custRequestRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the custRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustRequest : {}", id);
        custRequestRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
