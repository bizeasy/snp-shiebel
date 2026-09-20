package com.snp.web.rest;

import com.snp.domain.CustomTimePeriod;
import com.snp.repository.CustomTimePeriodRepository;
import com.snp.service.CustomTimePeriodQueryService;
import com.snp.service.CustomTimePeriodService;
import com.snp.service.criteria.CustomTimePeriodCriteria;
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
 * REST controller for managing {@link com.snp.domain.CustomTimePeriod}.
 */
@RestController
@RequestMapping("/api/custom-time-periods")
public class CustomTimePeriodResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomTimePeriodResource.class);

    private static final String ENTITY_NAME = "customTimePeriod";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CustomTimePeriodService customTimePeriodService;

    private final CustomTimePeriodRepository customTimePeriodRepository;

    private final CustomTimePeriodQueryService customTimePeriodQueryService;

    public CustomTimePeriodResource(
        CustomTimePeriodService customTimePeriodService,
        CustomTimePeriodRepository customTimePeriodRepository,
        CustomTimePeriodQueryService customTimePeriodQueryService
    ) {
        this.customTimePeriodService = customTimePeriodService;
        this.customTimePeriodRepository = customTimePeriodRepository;
        this.customTimePeriodQueryService = customTimePeriodQueryService;
    }

    /**
     * {@code POST  /custom-time-periods} : Create a new customTimePeriod.
     *
     * @param customTimePeriod the customTimePeriod to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customTimePeriod, or with status {@code 400 (Bad Request)} if the customTimePeriod already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomTimePeriod> createCustomTimePeriod(@Valid @RequestBody CustomTimePeriod customTimePeriod)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomTimePeriod : {}", customTimePeriod);
        if (customTimePeriod.getId() != null) {
            throw new BadRequestAlertException("A new customTimePeriod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customTimePeriod = customTimePeriodService.save(customTimePeriod);
        return ResponseEntity.created(new URI("/api/custom-time-periods/" + customTimePeriod.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customTimePeriod.getId().toString()))
            .body(customTimePeriod);
    }

    /**
     * {@code PUT  /custom-time-periods/:id} : Updates an existing customTimePeriod.
     *
     * @param id the id of the customTimePeriod to save.
     * @param customTimePeriod the customTimePeriod to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customTimePeriod,
     * or with status {@code 400 (Bad Request)} if the customTimePeriod is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customTimePeriod couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomTimePeriod> updateCustomTimePeriod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomTimePeriod customTimePeriod
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomTimePeriod : {}, {}", id, customTimePeriod);
        if (customTimePeriod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customTimePeriod.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customTimePeriodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customTimePeriod = customTimePeriodService.update(customTimePeriod);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customTimePeriod.getId().toString()))
            .body(customTimePeriod);
    }

    /**
     * {@code PATCH  /custom-time-periods/:id} : Partial updates given fields of an existing customTimePeriod, field will ignore if it is null
     *
     * @param id the id of the customTimePeriod to save.
     * @param customTimePeriod the customTimePeriod to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customTimePeriod,
     * or with status {@code 400 (Bad Request)} if the customTimePeriod is not valid,
     * or with status {@code 404 (Not Found)} if the customTimePeriod is not found,
     * or with status {@code 500 (Internal Server Error)} if the customTimePeriod couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomTimePeriod> partialUpdateCustomTimePeriod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomTimePeriod customTimePeriod
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CustomTimePeriod : {}, {}", id, customTimePeriod);
        if (customTimePeriod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customTimePeriod.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customTimePeriodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomTimePeriod> result = customTimePeriodService.partialUpdate(customTimePeriod);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customTimePeriod.getId().toString())
        );
    }

    /**
     * {@code GET  /custom-time-periods} : get all the Custom Time Periods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Custom Time Periods in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomTimePeriod>> getAllCustomTimePeriods(CustomTimePeriodCriteria criteria) {
        LOG.debug("REST request to get CustomTimePeriods by criteria: {}", criteria);

        List<CustomTimePeriod> entityList = customTimePeriodQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /custom-time-periods/count} : count all the customTimePeriods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomTimePeriods(CustomTimePeriodCriteria criteria) {
        LOG.debug("REST request to count CustomTimePeriods by criteria: {}", criteria);
        return ResponseEntity.ok().body(customTimePeriodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /custom-time-periods/:id} : get the "id" customTimePeriod.
     *
     * @param id the id of the customTimePeriod to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customTimePeriod, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomTimePeriod> getCustomTimePeriod(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomTimePeriod : {}", id);
        Optional<CustomTimePeriod> customTimePeriod = customTimePeriodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customTimePeriod);
    }

    /**
     * {@code DELETE  /custom-time-periods/:id} : delete the "id" customTimePeriod.
     *
     * @param id the id of the customTimePeriod to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomTimePeriod(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomTimePeriod : {}", id);
        customTimePeriodService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
