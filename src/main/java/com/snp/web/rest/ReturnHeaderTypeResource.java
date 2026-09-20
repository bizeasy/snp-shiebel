package com.snp.web.rest;

import com.snp.domain.ReturnHeaderType;
import com.snp.repository.ReturnHeaderTypeRepository;
import com.snp.service.ReturnHeaderTypeQueryService;
import com.snp.service.ReturnHeaderTypeService;
import com.snp.service.criteria.ReturnHeaderTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ReturnHeaderType}.
 */
@RestController
@RequestMapping("/api/return-header-types")
public class ReturnHeaderTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnHeaderTypeResource.class);

    private static final String ENTITY_NAME = "returnHeaderType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ReturnHeaderTypeService returnHeaderTypeService;

    private final ReturnHeaderTypeRepository returnHeaderTypeRepository;

    private final ReturnHeaderTypeQueryService returnHeaderTypeQueryService;

    public ReturnHeaderTypeResource(
        ReturnHeaderTypeService returnHeaderTypeService,
        ReturnHeaderTypeRepository returnHeaderTypeRepository,
        ReturnHeaderTypeQueryService returnHeaderTypeQueryService
    ) {
        this.returnHeaderTypeService = returnHeaderTypeService;
        this.returnHeaderTypeRepository = returnHeaderTypeRepository;
        this.returnHeaderTypeQueryService = returnHeaderTypeQueryService;
    }

    /**
     * {@code POST  /return-header-types} : Create a new returnHeaderType.
     *
     * @param returnHeaderType the returnHeaderType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new returnHeaderType, or with status {@code 400 (Bad Request)} if the returnHeaderType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReturnHeaderType> createReturnHeaderType(@Valid @RequestBody ReturnHeaderType returnHeaderType)
        throws URISyntaxException {
        LOG.debug("REST request to save ReturnHeaderType : {}", returnHeaderType);
        if (returnHeaderType.getId() != null) {
            throw new BadRequestAlertException("A new returnHeaderType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        returnHeaderType = returnHeaderTypeService.save(returnHeaderType);
        return ResponseEntity.created(new URI("/api/return-header-types/" + returnHeaderType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, returnHeaderType.getId().toString()))
            .body(returnHeaderType);
    }

    /**
     * {@code PUT  /return-header-types/:id} : Updates an existing returnHeaderType.
     *
     * @param id the id of the returnHeaderType to save.
     * @param returnHeaderType the returnHeaderType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnHeaderType,
     * or with status {@code 400 (Bad Request)} if the returnHeaderType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the returnHeaderType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReturnHeaderType> updateReturnHeaderType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReturnHeaderType returnHeaderType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReturnHeaderType : {}, {}", id, returnHeaderType);
        if (returnHeaderType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnHeaderType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnHeaderTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        returnHeaderType = returnHeaderTypeService.update(returnHeaderType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnHeaderType.getId().toString()))
            .body(returnHeaderType);
    }

    /**
     * {@code PATCH  /return-header-types/:id} : Partial updates given fields of an existing returnHeaderType, field will ignore if it is null
     *
     * @param id the id of the returnHeaderType to save.
     * @param returnHeaderType the returnHeaderType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnHeaderType,
     * or with status {@code 400 (Bad Request)} if the returnHeaderType is not valid,
     * or with status {@code 404 (Not Found)} if the returnHeaderType is not found,
     * or with status {@code 500 (Internal Server Error)} if the returnHeaderType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReturnHeaderType> partialUpdateReturnHeaderType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReturnHeaderType returnHeaderType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ReturnHeaderType : {}, {}", id, returnHeaderType);
        if (returnHeaderType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnHeaderType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnHeaderTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReturnHeaderType> result = returnHeaderTypeService.partialUpdate(returnHeaderType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnHeaderType.getId().toString())
        );
    }

    /**
     * {@code GET  /return-header-types} : get all the Return Header Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Return Header Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReturnHeaderType>> getAllReturnHeaderTypes(ReturnHeaderTypeCriteria criteria) {
        LOG.debug("REST request to get ReturnHeaderTypes by criteria: {}", criteria);

        List<ReturnHeaderType> entityList = returnHeaderTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /return-header-types/count} : count all the returnHeaderTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReturnHeaderTypes(ReturnHeaderTypeCriteria criteria) {
        LOG.debug("REST request to count ReturnHeaderTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(returnHeaderTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /return-header-types/:id} : get the "id" returnHeaderType.
     *
     * @param id the id of the returnHeaderType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the returnHeaderType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReturnHeaderType> getReturnHeaderType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReturnHeaderType : {}", id);
        Optional<ReturnHeaderType> returnHeaderType = returnHeaderTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(returnHeaderType);
    }

    /**
     * {@code DELETE  /return-header-types/:id} : delete the "id" returnHeaderType.
     *
     * @param id the id of the returnHeaderType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturnHeaderType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReturnHeaderType : {}", id);
        returnHeaderTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
