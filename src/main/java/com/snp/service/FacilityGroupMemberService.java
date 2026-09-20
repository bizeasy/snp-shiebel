package com.snp.service;

import com.snp.domain.FacilityGroupMember;
import com.snp.repository.FacilityGroupMemberRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.FacilityGroupMember}.
 */
@Service
@Transactional
public class FacilityGroupMemberService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityGroupMemberService.class);

    private final FacilityGroupMemberRepository facilityGroupMemberRepository;

    public FacilityGroupMemberService(FacilityGroupMemberRepository facilityGroupMemberRepository) {
        this.facilityGroupMemberRepository = facilityGroupMemberRepository;
    }

    /**
     * Save a facilityGroupMember.
     *
     * @param facilityGroupMember the entity to save.
     * @return the persisted entity.
     */
    public FacilityGroupMember save(FacilityGroupMember facilityGroupMember) {
        LOG.debug("Request to save FacilityGroupMember : {}", facilityGroupMember);
        return facilityGroupMemberRepository.save(facilityGroupMember);
    }

    /**
     * Update a facilityGroupMember.
     *
     * @param facilityGroupMember the entity to save.
     * @return the persisted entity.
     */
    public FacilityGroupMember update(FacilityGroupMember facilityGroupMember) {
        LOG.debug("Request to update FacilityGroupMember : {}", facilityGroupMember);
        return facilityGroupMemberRepository.save(facilityGroupMember);
    }

    /**
     * Partially update a facilityGroupMember.
     *
     * @param facilityGroupMember the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacilityGroupMember> partialUpdate(FacilityGroupMember facilityGroupMember) {
        LOG.debug("Request to partially update FacilityGroupMember : {}", facilityGroupMember);

        return facilityGroupMemberRepository
            .findById(facilityGroupMember.getId())
            .map(existingFacilityGroupMember -> {
                updateIfPresent(existingFacilityGroupMember::setFromDate, facilityGroupMember.getFromDate());
                updateIfPresent(existingFacilityGroupMember::setThruDate, facilityGroupMember.getThruDate());
                updateIfPresent(existingFacilityGroupMember::setSequenceNo, facilityGroupMember.getSequenceNo());

                return existingFacilityGroupMember;
            })
            .map(facilityGroupMemberRepository::save);
    }

    /**
     * Get all the facilityGroupMembers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FacilityGroupMember> findAllWithEagerRelationships(Pageable pageable) {
        return facilityGroupMemberRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one facilityGroupMember by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacilityGroupMember> findOne(Long id) {
        LOG.debug("Request to get FacilityGroupMember : {}", id);
        return facilityGroupMemberRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the facilityGroupMember by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FacilityGroupMember : {}", id);
        facilityGroupMemberRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
