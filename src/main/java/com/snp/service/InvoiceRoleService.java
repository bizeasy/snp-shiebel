package com.snp.service;

import com.snp.domain.InvoiceRole;
import com.snp.repository.InvoiceRoleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.InvoiceRole}.
 */
@Service
@Transactional
public class InvoiceRoleService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceRoleService.class);

    private final InvoiceRoleRepository invoiceRoleRepository;

    public InvoiceRoleService(InvoiceRoleRepository invoiceRoleRepository) {
        this.invoiceRoleRepository = invoiceRoleRepository;
    }

    /**
     * Save a invoiceRole.
     *
     * @param invoiceRole the entity to save.
     * @return the persisted entity.
     */
    public InvoiceRole save(InvoiceRole invoiceRole) {
        LOG.debug("Request to save InvoiceRole : {}", invoiceRole);
        return invoiceRoleRepository.save(invoiceRole);
    }

    /**
     * Update a invoiceRole.
     *
     * @param invoiceRole the entity to save.
     * @return the persisted entity.
     */
    public InvoiceRole update(InvoiceRole invoiceRole) {
        LOG.debug("Request to update InvoiceRole : {}", invoiceRole);
        return invoiceRoleRepository.save(invoiceRole);
    }

    /**
     * Partially update a invoiceRole.
     *
     * @param invoiceRole the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceRole> partialUpdate(InvoiceRole invoiceRole) {
        LOG.debug("Request to partially update InvoiceRole : {}", invoiceRole);

        return invoiceRoleRepository.findById(invoiceRole.getId()).map(invoiceRoleRepository::save);
    }

    /**
     * Get all the invoiceRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<InvoiceRole> findAllWithEagerRelationships(Pageable pageable) {
        return invoiceRoleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one invoiceRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceRole> findOne(Long id) {
        LOG.debug("Request to get InvoiceRole : {}", id);
        return invoiceRoleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the invoiceRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceRole : {}", id);
        invoiceRoleRepository.deleteById(id);
    }
}
