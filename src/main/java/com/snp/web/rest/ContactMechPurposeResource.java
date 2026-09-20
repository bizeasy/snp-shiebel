package com.snp.web.rest;

import com.snp.domain.ContactMechPurpose;
import com.snp.repository.ContactMechPurposeRepository;
import com.snp.service.ContactMechPurposeQueryService;
import com.snp.service.ContactMechPurposeService;
import com.snp.service.criteria.ContactMechPurposeCriteria;
import com.snp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.snp.domain.ContactMechPurpose}.
 */
@RestController
@RequestMapping("/api/contact-mech-purposes")
public class ContactMechPurposeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContactMechPurposeResource.class);

    private static final String ENTITY_NAME = "contactMechPurpose";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ContactMechPurposeService contactMechPurposeService;

    private final ContactMechPurposeRepository contactMechPurposeRepository;

    private final ContactMechPurposeQueryService contactMechPurposeQueryService;

    public ContactMechPurposeResource(
        ContactMechPurposeService contactMechPurposeService,
        ContactMechPurposeRepository contactMechPurposeRepository,
        ContactMechPurposeQueryService contactMechPurposeQueryService
    ) {
        this.contactMechPurposeService = contactMechPurposeService;
        this.contactMechPurposeRepository = contactMechPurposeRepository;
        this.contactMechPurposeQueryService = contactMechPurposeQueryService;
    }

    /**
     * {@code POST  /contact-mech-purposes} : Create a new contactMechPurpose.
     *
     * @param contactMechPurpose the contactMechPurpose to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactMechPurpose, or with status {@code 400 (Bad Request)} if the contactMechPurpose already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContactMechPurpose> createContactMechPurpose(@Valid @RequestBody ContactMechPurpose contactMechPurpose)
        throws URISyntaxException {
        LOG.debug("REST request to save ContactMechPurpose : {}", contactMechPurpose);
        if (contactMechPurpose.getId() != null) {
            throw new BadRequestAlertException("A new contactMechPurpose cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contactMechPurpose = contactMechPurposeService.save(contactMechPurpose);
        return ResponseEntity.created(new URI("/api/contact-mech-purposes/" + contactMechPurpose.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contactMechPurpose.getId().toString()))
            .body(contactMechPurpose);
    }

    /**
     * {@code PUT  /contact-mech-purposes/:id} : Updates an existing contactMechPurpose.
     *
     * @param id the id of the contactMechPurpose to save.
     * @param contactMechPurpose the contactMechPurpose to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactMechPurpose,
     * or with status {@code 400 (Bad Request)} if the contactMechPurpose is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactMechPurpose couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactMechPurpose> updateContactMechPurpose(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactMechPurpose contactMechPurpose
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContactMechPurpose : {}, {}", id, contactMechPurpose);
        if (contactMechPurpose.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactMechPurpose.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactMechPurposeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contactMechPurpose = contactMechPurposeService.update(contactMechPurpose);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactMechPurpose.getId().toString()))
            .body(contactMechPurpose);
    }

    /**
     * {@code PATCH  /contact-mech-purposes/:id} : Partial updates given fields of an existing contactMechPurpose, field will ignore if it is null
     *
     * @param id the id of the contactMechPurpose to save.
     * @param contactMechPurpose the contactMechPurpose to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactMechPurpose,
     * or with status {@code 400 (Bad Request)} if the contactMechPurpose is not valid,
     * or with status {@code 404 (Not Found)} if the contactMechPurpose is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactMechPurpose couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactMechPurpose> partialUpdateContactMechPurpose(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactMechPurpose contactMechPurpose
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ContactMechPurpose : {}, {}", id, contactMechPurpose);
        if (contactMechPurpose.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactMechPurpose.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactMechPurposeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactMechPurpose> result = contactMechPurposeService.partialUpdate(contactMechPurpose);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactMechPurpose.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-mech-purposes} : get all the Contact Mech Purposes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Contact Mech Purposes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContactMechPurpose>> getAllContactMechPurposes(ContactMechPurposeCriteria criteria) {
        LOG.debug("REST request to get ContactMechPurposes by criteria: {}", criteria);

        List<ContactMechPurpose> entityList = contactMechPurposeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /contact-mech-purposes/count} : count all the contactMechPurposes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countContactMechPurposes(ContactMechPurposeCriteria criteria) {
        LOG.debug("REST request to count ContactMechPurposes by criteria: {}", criteria);
        return ResponseEntity.ok().body(contactMechPurposeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contact-mech-purposes/:id} : get the "id" contactMechPurpose.
     *
     * @param id the id of the contactMechPurpose to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactMechPurpose, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactMechPurpose> getContactMechPurpose(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContactMechPurpose : {}", id);
        Optional<ContactMechPurpose> contactMechPurpose = contactMechPurposeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactMechPurpose);
    }

    /**
     * {@code DELETE  /contact-mech-purposes/:id} : delete the "id" contactMechPurpose.
     *
     * @param id the id of the contactMechPurpose to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactMechPurpose(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContactMechPurpose : {}", id);
        contactMechPurposeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
