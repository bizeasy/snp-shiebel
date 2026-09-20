package com.snp.web.rest;

import com.snp.domain.SystemParameters;
import com.snp.repository.SystemParametersRepository;
import com.snp.service.SystemParametersQueryService;
import com.snp.service.SystemParametersService;
import com.snp.service.criteria.SystemParametersCriteria;
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
 * REST controller for managing {@link com.snp.domain.SystemParameters}.
 */
@RestController
@RequestMapping("/api/system-parameters")
public class SystemParametersResource {

    private static final Logger LOG = LoggerFactory.getLogger(SystemParametersResource.class);

    private static final String ENTITY_NAME = "systemParameters";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final SystemParametersService systemParametersService;

    private final SystemParametersRepository systemParametersRepository;

    private final SystemParametersQueryService systemParametersQueryService;

    public SystemParametersResource(
        SystemParametersService systemParametersService,
        SystemParametersRepository systemParametersRepository,
        SystemParametersQueryService systemParametersQueryService
    ) {
        this.systemParametersService = systemParametersService;
        this.systemParametersRepository = systemParametersRepository;
        this.systemParametersQueryService = systemParametersQueryService;
    }

    /**
     * {@code POST  /system-parameters} : Create a new systemParameters.
     *
     * @param systemParameters the systemParameters to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemParameters, or with status {@code 400 (Bad Request)} if the systemParameters already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SystemParameters> createSystemParameters(@Valid @RequestBody SystemParameters systemParameters)
        throws URISyntaxException {
        LOG.debug("REST request to save SystemParameters : {}", systemParameters);
        if (systemParameters.getId() != null) {
            throw new BadRequestAlertException("A new systemParameters cannot already have an ID", ENTITY_NAME, "idexists");
        }
        systemParameters = systemParametersService.save(systemParameters);
        return ResponseEntity.created(new URI("/api/system-parameters/" + systemParameters.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, systemParameters.getId().toString()))
            .body(systemParameters);
    }

    /**
     * {@code PUT  /system-parameters/:id} : Updates an existing systemParameters.
     *
     * @param id the id of the systemParameters to save.
     * @param systemParameters the systemParameters to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemParameters,
     * or with status {@code 400 (Bad Request)} if the systemParameters is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemParameters couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SystemParameters> updateSystemParameters(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemParameters systemParameters
    ) throws URISyntaxException {
        LOG.debug("REST request to update SystemParameters : {}, {}", id, systemParameters);
        if (systemParameters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemParameters.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemParametersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        systemParameters = systemParametersService.update(systemParameters);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemParameters.getId().toString()))
            .body(systemParameters);
    }

    /**
     * {@code PATCH  /system-parameters/:id} : Partial updates given fields of an existing systemParameters, field will ignore if it is null
     *
     * @param id the id of the systemParameters to save.
     * @param systemParameters the systemParameters to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemParameters,
     * or with status {@code 400 (Bad Request)} if the systemParameters is not valid,
     * or with status {@code 404 (Not Found)} if the systemParameters is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemParameters couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemParameters> partialUpdateSystemParameters(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemParameters systemParameters
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update SystemParameters : {}, {}", id, systemParameters);
        if (systemParameters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemParameters.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemParametersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemParameters> result = systemParametersService.partialUpdate(systemParameters);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemParameters.getId().toString())
        );
    }

    /**
     * {@code GET  /system-parameters} : get all the System Parameters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of System Parameters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SystemParameters>> getAllSystemParameterses(SystemParametersCriteria criteria) {
        LOG.debug("REST request to get SystemParameterses by criteria: {}", criteria);

        List<SystemParameters> entityList = systemParametersQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /system-parameters/count} : count all the systemParameterses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSystemParameterses(SystemParametersCriteria criteria) {
        LOG.debug("REST request to count SystemParameterses by criteria: {}", criteria);
        return ResponseEntity.ok().body(systemParametersQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /system-parameters/:id} : get the "id" systemParameters.
     *
     * @param id the id of the systemParameters to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemParameters, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SystemParameters> getSystemParameters(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SystemParameters : {}", id);
        Optional<SystemParameters> systemParameters = systemParametersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemParameters);
    }

    /**
     * {@code DELETE  /system-parameters/:id} : delete the "id" systemParameters.
     *
     * @param id the id of the systemParameters to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemParameters(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SystemParameters : {}", id);
        systemParametersService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
