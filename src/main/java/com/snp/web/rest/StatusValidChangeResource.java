package com.snp.web.rest;

import com.snp.domain.StatusValidChange;
import com.snp.repository.StatusValidChangeRepository;
import com.snp.service.StatusValidChangeQueryService;
import com.snp.service.StatusValidChangeService;
import com.snp.service.criteria.StatusValidChangeCriteria;
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
 * REST controller for managing {@link com.snp.domain.StatusValidChange}.
 */
@RestController
@RequestMapping("/api/status-valid-changes")
public class StatusValidChangeResource {

    private static final Logger LOG = LoggerFactory.getLogger(StatusValidChangeResource.class);

    private static final String ENTITY_NAME = "statusValidChange";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final StatusValidChangeService statusValidChangeService;

    private final StatusValidChangeRepository statusValidChangeRepository;

    private final StatusValidChangeQueryService statusValidChangeQueryService;

    public StatusValidChangeResource(
        StatusValidChangeService statusValidChangeService,
        StatusValidChangeRepository statusValidChangeRepository,
        StatusValidChangeQueryService statusValidChangeQueryService
    ) {
        this.statusValidChangeService = statusValidChangeService;
        this.statusValidChangeRepository = statusValidChangeRepository;
        this.statusValidChangeQueryService = statusValidChangeQueryService;
    }

    /**
     * {@code POST  /status-valid-changes} : Create a new statusValidChange.
     *
     * @param statusValidChange the statusValidChange to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new statusValidChange, or with status {@code 400 (Bad Request)} if the statusValidChange already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StatusValidChange> createStatusValidChange(@Valid @RequestBody StatusValidChange statusValidChange)
        throws URISyntaxException {
        LOG.debug("REST request to save StatusValidChange : {}", statusValidChange);
        if (statusValidChange.getId() != null) {
            throw new BadRequestAlertException("A new statusValidChange cannot already have an ID", ENTITY_NAME, "idexists");
        }
        statusValidChange = statusValidChangeService.save(statusValidChange);
        return ResponseEntity.created(new URI("/api/status-valid-changes/" + statusValidChange.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, statusValidChange.getId().toString()))
            .body(statusValidChange);
    }

    /**
     * {@code PUT  /status-valid-changes/:id} : Updates an existing statusValidChange.
     *
     * @param id the id of the statusValidChange to save.
     * @param statusValidChange the statusValidChange to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statusValidChange,
     * or with status {@code 400 (Bad Request)} if the statusValidChange is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statusValidChange couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StatusValidChange> updateStatusValidChange(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StatusValidChange statusValidChange
    ) throws URISyntaxException {
        LOG.debug("REST request to update StatusValidChange : {}, {}", id, statusValidChange);
        if (statusValidChange.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statusValidChange.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statusValidChangeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        statusValidChange = statusValidChangeService.update(statusValidChange);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statusValidChange.getId().toString()))
            .body(statusValidChange);
    }

    /**
     * {@code PATCH  /status-valid-changes/:id} : Partial updates given fields of an existing statusValidChange, field will ignore if it is null
     *
     * @param id the id of the statusValidChange to save.
     * @param statusValidChange the statusValidChange to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statusValidChange,
     * or with status {@code 400 (Bad Request)} if the statusValidChange is not valid,
     * or with status {@code 404 (Not Found)} if the statusValidChange is not found,
     * or with status {@code 500 (Internal Server Error)} if the statusValidChange couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StatusValidChange> partialUpdateStatusValidChange(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StatusValidChange statusValidChange
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update StatusValidChange : {}, {}", id, statusValidChange);
        if (statusValidChange.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statusValidChange.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statusValidChangeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StatusValidChange> result = statusValidChangeService.partialUpdate(statusValidChange);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statusValidChange.getId().toString())
        );
    }

    /**
     * {@code GET  /status-valid-changes} : get all the Status Valid Changes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Status Valid Changes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StatusValidChange>> getAllStatusValidChanges(StatusValidChangeCriteria criteria) {
        LOG.debug("REST request to get StatusValidChanges by criteria: {}", criteria);

        List<StatusValidChange> entityList = statusValidChangeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /status-valid-changes/count} : count all the statusValidChanges.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countStatusValidChanges(StatusValidChangeCriteria criteria) {
        LOG.debug("REST request to count StatusValidChanges by criteria: {}", criteria);
        return ResponseEntity.ok().body(statusValidChangeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /status-valid-changes/:id} : get the "id" statusValidChange.
     *
     * @param id the id of the statusValidChange to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statusValidChange, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StatusValidChange> getStatusValidChange(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StatusValidChange : {}", id);
        Optional<StatusValidChange> statusValidChange = statusValidChangeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(statusValidChange);
    }

    /**
     * {@code DELETE  /status-valid-changes/:id} : delete the "id" statusValidChange.
     *
     * @param id the id of the statusValidChange to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatusValidChange(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StatusValidChange : {}", id);
        statusValidChangeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
