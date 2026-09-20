package com.snp.web.rest;

import com.snp.domain.ContactMech;
import com.snp.repository.ContactMechRepository;
import com.snp.service.ContactMechQueryService;
import com.snp.service.ContactMechService;
import com.snp.service.criteria.ContactMechCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.snp.domain.ContactMech}.
 */
@RestController
@RequestMapping("/api/contact-meches")
public class ContactMechResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContactMechResource.class);

    private static final String ENTITY_NAME = "contactMech";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ContactMechService contactMechService;

    private final ContactMechRepository contactMechRepository;

    private final ContactMechQueryService contactMechQueryService;

    public ContactMechResource(
        ContactMechService contactMechService,
        ContactMechRepository contactMechRepository,
        ContactMechQueryService contactMechQueryService
    ) {
        this.contactMechService = contactMechService;
        this.contactMechRepository = contactMechRepository;
        this.contactMechQueryService = contactMechQueryService;
    }

    /**
     * {@code POST  /contact-meches} : Create a new contactMech.
     *
     * @param contactMech the contactMech to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactMech, or with status {@code 400 (Bad Request)} if the contactMech already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContactMech> createContactMech(@Valid @RequestBody ContactMech contactMech) throws URISyntaxException {
        LOG.debug("REST request to save ContactMech : {}", contactMech);
        if (contactMech.getId() != null) {
            throw new BadRequestAlertException("A new contactMech cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contactMech = contactMechService.save(contactMech);
        return ResponseEntity.created(new URI("/api/contact-meches/" + contactMech.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contactMech.getId().toString()))
            .body(contactMech);
    }

    /**
     * {@code PUT  /contact-meches/:id} : Updates an existing contactMech.
     *
     * @param id the id of the contactMech to save.
     * @param contactMech the contactMech to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactMech,
     * or with status {@code 400 (Bad Request)} if the contactMech is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactMech couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactMech> updateContactMech(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactMech contactMech
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContactMech : {}, {}", id, contactMech);
        if (contactMech.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactMech.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactMechRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contactMech = contactMechService.update(contactMech);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactMech.getId().toString()))
            .body(contactMech);
    }

    /**
     * {@code PATCH  /contact-meches/:id} : Partial updates given fields of an existing contactMech, field will ignore if it is null
     *
     * @param id the id of the contactMech to save.
     * @param contactMech the contactMech to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactMech,
     * or with status {@code 400 (Bad Request)} if the contactMech is not valid,
     * or with status {@code 404 (Not Found)} if the contactMech is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactMech couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactMech> partialUpdateContactMech(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactMech contactMech
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ContactMech : {}, {}", id, contactMech);
        if (contactMech.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactMech.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactMechRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactMech> result = contactMechService.partialUpdate(contactMech);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactMech.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-meches} : get all the Contact Meches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Contact Meches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContactMech>> getAllContactMeches(
        ContactMechCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ContactMeches by criteria: {}", criteria);

        Page<ContactMech> page = contactMechQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contact-meches/count} : count all the contactMeches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countContactMeches(ContactMechCriteria criteria) {
        LOG.debug("REST request to count ContactMeches by criteria: {}", criteria);
        return ResponseEntity.ok().body(contactMechQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contact-meches/:id} : get the "id" contactMech.
     *
     * @param id the id of the contactMech to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactMech, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactMech> getContactMech(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContactMech : {}", id);
        Optional<ContactMech> contactMech = contactMechService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactMech);
    }

    /**
     * {@code DELETE  /contact-meches/:id} : delete the "id" contactMech.
     *
     * @param id the id of the contactMech to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactMech(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContactMech : {}", id);
        contactMechService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
