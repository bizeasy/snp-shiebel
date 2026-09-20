package com.snp.web.rest;

import com.snp.domain.ContactMechType;
import com.snp.repository.ContactMechTypeRepository;
import com.snp.service.ContactMechTypeQueryService;
import com.snp.service.ContactMechTypeService;
import com.snp.service.criteria.ContactMechTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ContactMechType}.
 */
@RestController
@RequestMapping("/api/contact-mech-types")
public class ContactMechTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContactMechTypeResource.class);

    private static final String ENTITY_NAME = "contactMechType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ContactMechTypeService contactMechTypeService;

    private final ContactMechTypeRepository contactMechTypeRepository;

    private final ContactMechTypeQueryService contactMechTypeQueryService;

    public ContactMechTypeResource(
        ContactMechTypeService contactMechTypeService,
        ContactMechTypeRepository contactMechTypeRepository,
        ContactMechTypeQueryService contactMechTypeQueryService
    ) {
        this.contactMechTypeService = contactMechTypeService;
        this.contactMechTypeRepository = contactMechTypeRepository;
        this.contactMechTypeQueryService = contactMechTypeQueryService;
    }

    /**
     * {@code POST  /contact-mech-types} : Create a new contactMechType.
     *
     * @param contactMechType the contactMechType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactMechType, or with status {@code 400 (Bad Request)} if the contactMechType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContactMechType> createContactMechType(@Valid @RequestBody ContactMechType contactMechType)
        throws URISyntaxException {
        LOG.debug("REST request to save ContactMechType : {}", contactMechType);
        if (contactMechType.getId() != null) {
            throw new BadRequestAlertException("A new contactMechType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contactMechType = contactMechTypeService.save(contactMechType);
        return ResponseEntity.created(new URI("/api/contact-mech-types/" + contactMechType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contactMechType.getId().toString()))
            .body(contactMechType);
    }

    /**
     * {@code PUT  /contact-mech-types/:id} : Updates an existing contactMechType.
     *
     * @param id the id of the contactMechType to save.
     * @param contactMechType the contactMechType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactMechType,
     * or with status {@code 400 (Bad Request)} if the contactMechType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactMechType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactMechType> updateContactMechType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactMechType contactMechType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContactMechType : {}, {}", id, contactMechType);
        if (contactMechType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactMechType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactMechTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contactMechType = contactMechTypeService.update(contactMechType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactMechType.getId().toString()))
            .body(contactMechType);
    }

    /**
     * {@code PATCH  /contact-mech-types/:id} : Partial updates given fields of an existing contactMechType, field will ignore if it is null
     *
     * @param id the id of the contactMechType to save.
     * @param contactMechType the contactMechType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactMechType,
     * or with status {@code 400 (Bad Request)} if the contactMechType is not valid,
     * or with status {@code 404 (Not Found)} if the contactMechType is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactMechType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactMechType> partialUpdateContactMechType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactMechType contactMechType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ContactMechType : {}, {}", id, contactMechType);
        if (contactMechType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactMechType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactMechTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactMechType> result = contactMechTypeService.partialUpdate(contactMechType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactMechType.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-mech-types} : get all the Contact Mech Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Contact Mech Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContactMechType>> getAllContactMechTypes(ContactMechTypeCriteria criteria) {
        LOG.debug("REST request to get ContactMechTypes by criteria: {}", criteria);

        List<ContactMechType> entityList = contactMechTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /contact-mech-types/count} : count all the contactMechTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countContactMechTypes(ContactMechTypeCriteria criteria) {
        LOG.debug("REST request to count ContactMechTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(contactMechTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contact-mech-types/:id} : get the "id" contactMechType.
     *
     * @param id the id of the contactMechType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactMechType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactMechType> getContactMechType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContactMechType : {}", id);
        Optional<ContactMechType> contactMechType = contactMechTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactMechType);
    }

    /**
     * {@code DELETE  /contact-mech-types/:id} : delete the "id" contactMechType.
     *
     * @param id the id of the contactMechType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactMechType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContactMechType : {}", id);
        contactMechTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
